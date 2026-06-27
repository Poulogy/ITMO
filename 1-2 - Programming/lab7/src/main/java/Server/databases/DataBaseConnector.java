package Server.databases;

import Server.ServerInitConfig;
import Server.interfaces.DataBConnectable;
import Server.interfaces.*;

import java.sql.*;
import java.util.Arrays;


/**
 * Класс для соединения с базой данных.
 */
public class DataBaseConnector implements DataBConnectable {

    /**
     * Имя базы данных.
     */
    private final String dbName =  "studs"; // studs

    /**
     * Имя пользователя базы данных.
     */
    private final String user =  "s465755"; // sXXXXXX

    /**
     * Пароль пользователя базы данных.
     */
    private final String pass =  "qdsSQU3b1ziKzUeo"; // пароль из ./pgpass

    /**
     * URL-адрес для подключения к базе данных.
     */
    private final String dbUrl = "jdbc:postgresql://localhost:5432/" + dbName;

    /**
     * Конструктор класса DataBaseConnector.
     */
    public DataBaseConnector() {
        try {
            Class.forName("org.postgresql.Driver");
            initializeDB();
        } catch (ClassNotFoundException e) {
            ServerInitConfig.logger.severe("DB driver is not found");
            System.exit(1);
        } catch (SQLException e) {
            ServerInitConfig.logger.warning("Table initialization error" + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Обрабатывает запросы без возвращаемого значения.
     * @param queryBody          тело запроса.
     * @throws Exception         в случае ошибки при работе с базой данных.
     */
    public void handleQuery(SQLConsumer<Connection> queryBody) throws Exception {
        try (Connection connection = DriverManager.getConnection(dbUrl, user, pass)) {
            queryBody.accept(connection);
        } catch (SQLException e) {
            throw new Exception("DB creation error: " + Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Обрабатывает запросы с возвращаемым значением.
     * @param queryBody      тело запроса.
     * @param <T>            тип возвращаемого значения.
     * @return результат выполнения запроса.
     * @throws Exception     в случае ошибки при работе с базой данных.
     */
    public <T> T handleQuery(SQLFunction<Connection, T> queryBody) throws Exception {
        try (Connection connection = DriverManager.getConnection(dbUrl, user, pass)) {
            return queryBody.apply(connection);
        } catch (SQLException e) {
            throw new Exception("DB creation error: " + e.getMessage());
        }
    }

    /**
     * Создает таблицы в базе данных, если они не существуют.
     * @throws SQLException в случае ошибки при создании таблиц.
     */
    private void initializeDB() throws SQLException {

        Connection connection = DriverManager.getConnection(dbUrl, user, pass);

        Statement statement = connection.createStatement();

        String dtBaseCreation = """
            CREATE SEQUENCE IF NOT EXISTS flats_id_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
            CREATE SEQUENCE IF NOT EXISTS users_id_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
            
            CREATE TABLE IF NOT EXISTS users (
                                                id bigint NOT NULL PRIMARY KEY DEFAULT nextval('users_id_seq'),
                                                login varchar(255) NOT NULL,
                                                password varchar(255) NOT NULL
                );
   
            CREATE TABLE IF NOT EXISTS flats (
                                                id bigint NOT NULL PRIMARY KEY DEFAULT nextval('flats_id_seq'),
                                                x float NOT NULL CHECK(x > -360),
                                                y bigint NOT NULL CHECK(y > -118),
                                                numberOfRooms int NOT NULL CHECK(numberOfRooms > 0 AND numberOfRooms <= 14 ),
                                                numberOfBathrooms bigint NOT NULL CHECK(numberOfBathrooms > 0),
                                                transport varchar(30) CHECK(transport in ('NONE', 'LITTLE', 'NORMAL', 'ENOUGH', 'FEW') OR transport IS NULL),
                                                houseName text,
                                                houseYear int,
                                                numberOfFlatsOnFloor bigint,
                                                creation_date date NOT NULL,
                                                name TEXT NOT NULL,
                                                area float NOT NULL CHECK(area > 0 AND area <= 539),
                                                owner_id bigint NOT NULL REFERENCES users (id)
            );
            """;
        statement.execute(dtBaseCreation);

        connection.close();
    }
}