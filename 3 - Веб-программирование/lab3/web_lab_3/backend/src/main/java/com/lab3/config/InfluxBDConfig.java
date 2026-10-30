package com.lab3.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.lab3.Exceptions.ServerException;
import com.lab3.module.PointBean;
import com.lab3.module.User;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class InfluxBDConfig {
    private static final Logger logger = Logger.getLogger(InfluxBDConfig.class.getName());

    private InfluxDBClient client;
    String org = System.getenv("INFLUXDB_ORG");

    public InfluxBDConfig() {
    }
    @PostConstruct
    public void init() {
        try{
            String url = System.getenv("INFLUXDB_URL");
            String token = System.getenv("INFLUXDB_TOKEN");
            org = System.getenv("INFLUXDB_ORG");

            if (org == null) org = "P3208";
            if (url == null) url = "http://influxdb:8086";
            if (token == null) token = "H1WwiUyu2DNsmAm-hpQCKoAuKz6v7N0BhFXnk6DWIOsNWtk0CI3HQDhDrL6RiySr6Er_KipmWyTBJPqDfF53Bg==";

            this.client = InfluxDBClientFactory.create(url, token.toCharArray(), org);
            logger.info("InfluxDB client initialized successfully on URL: " + url + " for Org: " + org);

        } catch (Exception e) {
            logger.severe("FATAL: Failed to initialize InfluxDB client. Check connection and credentials! Error: " + e.getMessage());
            this.client = null;
            throw new ServerException("Error in databases");
        }
    }
    public void addPoint(PointBean pointInfo) {
        logger.info("Attempting to write new point for user: " + pointInfo.getUsername());
        try{
            WriteApiBlocking writeApi = client.getWriteApiBlocking();
            String bucket = "Points";
            if (org == null) org = "P3208";

            Point point = Point.measurement("user_points")
                    .addTag("username", pointInfo.getUsername())

                    .addField("r", pointInfo.getR())
                    .addField("x", pointInfo.getX())
                    .addField("y", pointInfo.getY())
                    .addField("isHit", pointInfo.isHit())
                    .addField("scriptTime", pointInfo.getScriptTime())
                    .time(System.currentTimeMillis(), WritePrecision.MS);

            writeApi.writePoint(bucket, org, point);
            logger.info("Successfully wrote point for user: " + pointInfo.getUsername());
        }catch (Exception e){
            logger.severe("Error writing point to InfluxDB for user " + pointInfo.getUsername() + ": " + e.getMessage());
            throw new ServerException("Error in adding points in databases");
        }
    }
    public void registerUser(User user) {
        logger.info("Attempting to register user: " + user.getUsername());
        try{
            WriteApiBlocking writeApi = client.getWriteApiBlocking();
            String bucket = "Points";
            if (org == null) org = "P3208";

            Point point = Point.measurement("user_accounts")

                    .addTag("username", user.getUsername())
                    .addField("hashedPass", user.getPasswordHash())
                    .time(System.currentTimeMillis(), WritePrecision.MS);

            writeApi.writePoint(bucket, org, point);
            logger.info("User registered successfully: " + user.getUsername());
        }catch (Exception e){
            logger.severe("Error registering user " + user.getUsername() + ": " + e.getMessage());
            throw new ServerException("Error in adding user in databases");
        }
    }

    public Optional<User> findUserByUsername(String username) {
        logger.info("Searching for user: " + username);
        try{
            QueryApi queryApi = client.getQueryApi();
            String bucket = "Points";

            String fluxQueryPass = String.format(
                    "from(bucket: \"%s\")\n" +
                            "|> range(start: 0)\n" +
                            "|> filter(fn: (r) => r._measurement == \"user_accounts\")\n" +
                            "|> filter(fn: (r) => r.username == \"%s\")\n" +
                            "|> filter(fn: (r) => r._field == \"hashedPass\")\n" +
                            "|> group()\n" +
                            "|> last()\n" +
                            "|> pivot(rowKey: [\"_time\", \"username\"], columnKey: [\"_field\"], valueColumn: \"_value\")", bucket, username);

            List<FluxTable> tablesPass = queryApi.query(fluxQueryPass);

            if (tablesPass.isEmpty() || tablesPass.get(0).getRecords().isEmpty()) {
                return Optional.empty();
            }
            FluxRecord recordPass = tablesPass.get(0).getRecords().get(0);

            User userToCheck = new User();
            userToCheck.setPasswordHash((String) recordPass.getValueByKey("hashedPass"));
            userToCheck.setUsername(username);

            return Optional.of(userToCheck);
        }catch (Exception e) {
            logger.severe("Error searching for user " + username + ": " + e.getMessage());
            throw new ServerException("Error in finding user in databases");
        }

    }
    public Optional<List<PointBean>> findPointsByUser(String username) {
        logger.info("Fetching points for username: " + username);
        try{
            QueryApi queryApi = client.getQueryApi();

            String fluxQuery = String.format(
                    "from(bucket: \"Points\")\n" +
                            "  |> range(start: -365d)\n" +
                            "  |> filter(fn: (r) => r[\"_measurement\"] == \"user_points\" and r[\"username\"] == \"%s\")\n" +
                            "  |> filter(fn: (r) => \n" +
                            "      r[\"_field\"] == \"x\" or r[\"_field\"] == \"y\" or r[\"_field\"] == \"r\" or r[\"_field\"] == \"isHit\" or r[\"_field\"] == \"scriptTime\")\n" +
                            "  |> pivot(rowKey: [\"_time\", \"username\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                            "  |> sort(columns: [\"_time\"], desc: true)",
                    username);
            List<PointBean> points = new ArrayList<>();
            List<FluxTable> tables = queryApi.query(fluxQuery);

            logger.info("DB data" + tables);

            if (tables.isEmpty() || tables.get(0).getRecords().isEmpty()) {
                return Optional.empty();
            } else {
                for (FluxTable table : tables) {
                    for (FluxRecord record : table.getRecords()) {

                        PointBean point = new PointBean();

                        point.setX((Double) record.getValueByKey("x"));
                        point.setY((Double) record.getValueByKey("y"));
                        point.setR((Double) record.getValueByKey("r"));

                        point.setHit((Boolean) record.getValueByKey("isHit"));

                        point.setScriptTime(((Number) record.getValueByKey("scriptTime")).longValue());

                        point.setUsername((String) record.getValueByKey("username"));

                        point.setServerTime(record.getValueByKey("_time").toString());

                        points.add(point);
                    }
                }
                logger.info("DB data extracted" + points);
                return Optional.of(points);
            }
        } catch (Exception e) {
            logger.severe("Error fetching points for username " + username + ": " + e.getMessage());
            throw new ServerException("Error in getting points");
        }
    }

}