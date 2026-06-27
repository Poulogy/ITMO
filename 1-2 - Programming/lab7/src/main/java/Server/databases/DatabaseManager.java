package Server.databases;

import Common.modules.Coordinates;
import Common.modules.Flat;
import Common.modules.House;
import Common.modules.Transport;
import Server.ServerInitConfig;
import Server.interfaces.DataBConnectable;
import Server.system.Encryptor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * DatabaseManager - класс, предоставляющий методы для работы с базой данных.
 */
public class DatabaseManager {
    /**
     *  Класс, предоставляющий подключение к бд
     */
    private final DataBConnectable dbConnector;

    /**
     * Конструктор класса DBManager.
     * @param dbConnector объект, предоставляющий подключение к базе данных.
     */
    public DatabaseManager(DataBConnectable dbConnector) {
        this.dbConnector = dbConnector;
    }
    /**
     * Метод для загрузки коллекции квартир из базы данных.
     * @return объект PriorityQueue с загруженной коллекцией.
     * @throws Exception ошибка при обращении к бд.
     */

    public PriorityQueue<Flat> loadCollection() throws Exception{
        try {
            return dbConnector.handleQuery((Connection connection) -> {
                String selectCollectionQuery = "SELECT * FROM flats";
                Statement statement = connection.createStatement();
                ResultSet collectionSet = statement.executeQuery(selectCollectionQuery);
                PriorityQueue<Flat> resultDeque = new PriorityQueue<>();
                while (collectionSet.next()) {
                    Coordinates coordinates = new Coordinates(
                            collectionSet.getFloat("x"),
                            collectionSet.getLong("y")
                    );
                    Transport transport;
                    if (collectionSet.getString("transport") == null) {
                        transport = null;
                    } else {
                        transport = Transport.valueOf(collectionSet.getString("transport"));
                    }
                    House house;
                    if (collectionSet.getString("houseName") == null) {
                        house = null;
                    } else {
                        house = new House(collectionSet.getString("houseName"),
                                collectionSet.getInt("houseYear"),
                                collectionSet.getLong("numberOfFlatsOnFloor"));
                    }

                    Flat flat = new Flat(
                            collectionSet.getLong("id"),
                            coordinates,
                            collectionSet.getInt("numberOfRooms"),
                            collectionSet.getLong("numberOfBathrooms"),
                            transport,
                            house,
                            collectionSet.getDate("creation_date").toLocalDate().atStartOfDay(),
                            collectionSet.getString("name"),
                            collectionSet.getDouble("area")
                    );
                    resultDeque.add(flat);
                }
                return resultDeque;
            });
        } catch (Exception e) {
            ServerInitConfig.logger.warning("Load Collection Exception" + e);
            throw new Exception("Load Collection Exception");
        }
    }
    /**
     * Метод для добавления новой кв в базу данных.
     * @param flat              квартира, которую нужно добавить.
     * @param username          имя пользователя, которому принадлежит квартира.
     * @return идентификатор новой кв в бд.
     */
    public Long addElement(Flat flat, String username) throws Exception{
        try {
            return dbConnector.handleQuery((Connection connection) -> {
                String addElementQuery = "INSERT INTO flats (creation_date, name, x, y, area, numberOfBathrooms, numberOfRooms, transport, houseYear, houseName, numberOfFlatsOnFloor, owner_id) SELECT ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, id FROM users WHERE users.login = ?;";

                PreparedStatement preparedStatement = connection.prepareStatement(addElementQuery,
                        Statement.RETURN_GENERATED_KEYS);

                Coordinates coordinates = flat.getCoordinates();
                House house = flat.getHouse();
                preparedStatement.setDate(1, Date.valueOf(flat.getCreationDate().toLocalDate()));
                preparedStatement.setString(2, flat.getName());
                preparedStatement.setFloat(3, coordinates.getX());
                preparedStatement.setLong(4, coordinates.getY());
                preparedStatement.setDouble(5, flat.getArea());
                preparedStatement.setLong(6, flat.getNumberOfBathrooms());
                preparedStatement.setInt(7, flat.getNumberOfRooms());
                preparedStatement.setString(8, flat.getTransport() != null ? flat.getTransport().toString() : null);
                preparedStatement.setInt(9, house != null ? house.getYear() : 0);
                preparedStatement.setString(10, house != null ? house.getName() : null);
                preparedStatement.setLong(11, house != null ? house.getNumberOfFlatsOnFloor() : 0);
                preparedStatement.setString(12, username);

                preparedStatement.executeUpdate();
                ResultSet result = preparedStatement.getGeneratedKeys();
                result.next();

                return result.getLong(1);
            });
        }catch (Exception e){
            ServerInitConfig.logger.warning("To DB collection add exception" + e);
            throw new Exception("To DB collection add exception");
        }
    }
    /**
     * Метод для проверки существования кв с заданным идентификатором в базе данных.
     * @param id         идентификатор квартиры
     * @return true, если квартира существует бд, иначе - false
     * @throws Exception ошибка при обращении к бд
     */
    public boolean checkFlatExistence(Long id) throws Exception {
        return dbConnector.handleQuery((Connection connection) -> {
            String existenceQuery = "SELECT COUNT(*) FROM flats WHERE flats.id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(existenceQuery);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("count") > 0;
        });
    }

    /**
     * Метод для удаления квартиры с заданным идентификатором, принадлежащей заданному пользователю, из бд.
     * @param id           идентификатор удаляемой квартиры
     * @param username     логин пользователя, которому принадлежит удаляемая квартира
     * @return true, если квартира была успешно удалена, иначе - false
     * @throws Exception  ошибка при обращении к бд
     */
    public boolean removeById(Long id, String username) throws Exception {
        return dbConnector.handleQuery((Connection connection) -> {
            String remove = "DELETE FROM flats USING users WHERE flats.id = ? AND flats.owner_id = users.id AND users.login = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(remove);
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, username);

            int deletedBands = preparedStatement.executeUpdate();
            return deletedBands > 0;
        });
    }
    /**
     * Метод для обновления информации о квартире с заданным идентификатором, принадлежащей заданному пользователю, в бд
     * @param flat        новые данные о квартире
     * @param id          идентификатор
     * @param username    логин пользователя, которому принадлежит обновляемая информация о квартире
     * @return true, если информация о квартире была успешно обновлена, иначе - false
     * @throws Exception ошибка при обращении к бд
     */
    public boolean updateById(Flat flat, Long id, String username) throws Exception {
        return dbConnector.handleQuery((Connection connection) -> {
            connection.createStatement().execute("BEGIN TRANSACTION;");
            String updateQuery = "UPDATE flats "
                    + "SET creation_date = ?, "
                    + "name = ?, "
                    + "x = ?, "
                    + "y = ?, "
                    + "area = ?, "
                    + "numberOfBathrooms = ?, "
                    + "numberOfRooms = ?, "
                    + "transport = ?, "
                    + "houseYear = ?, "
                    + "houseName = ?, "
                    + "numberOfFlatsOnFloor = ? "
                    + "FROM users "
                    + "WHERE flats.id = ? "
                    + "AND flats.owner_id = users.id AND users.login = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

            Coordinates coordinates = flat.getCoordinates();
            House house = flat.getHouse();
            preparedStatement.setDate(1, Date.valueOf(flat.getCreationDate().toLocalDate()));
            preparedStatement.setString(2, flat.getName());
            preparedStatement.setFloat(3, coordinates.getX());
            preparedStatement.setLong(4, coordinates.getY());
            preparedStatement.setDouble(5, flat.getArea());
            preparedStatement.setLong(6, flat.getNumberOfBathrooms());
            preparedStatement.setInt(7, flat.getNumberOfRooms());
            preparedStatement.setString(8, flat.getTransport() != null ? flat.getTransport().toString() : null);
            preparedStatement.setInt(9, house != null ? house.getYear() : 0);
            preparedStatement.setString(10, house != null ? house.getName() : null);
            preparedStatement.setLong(11, house != null ? house.getNumberOfFlatsOnFloor() : 0);
            preparedStatement.setLong(12, id);
            preparedStatement.setString(13, username);

            int updatedRows = preparedStatement.executeUpdate();
            connection.createStatement().execute("COMMIT;");

            return updatedRows > 0;
        });
    }
    /**
     * Очищает бд от квартир, которые принадлежат указанному пользователю.
     * @param username      логин пользователя, квартиры которого нужно удалить.
     * @return список идентификаторов удаленных квартир.
     * @throws Exception ошибка при работе с бд.
     */
    public List<Long> clear(String username) throws Exception {
        return dbConnector.handleQuery((Connection connection) -> {
            String clearQuery = "DELETE FROM flats USING users WHERE flats.owner_id = users.id AND users.login = ? RETURNING flats.id;";
            PreparedStatement preparedStatement = connection.prepareStatement(clearQuery);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Long> resultingList = new ArrayList<>();
            while (resultSet.next()) {
                resultingList.add(resultSet.getLong("id"));
            }
            return resultingList;
        });
    }



    /**
     * Добавляет нового пользователя в базу данных.
     * @param username   логин пользователя.
     * @param password   пароль пользователя.
     * @throws Exception ошибка при работе с бд.
     */
    public void addUser(String username, String password) throws Exception {
        dbConnector.handleQuery((Connection connection) -> {
            String addUserQuery = "INSERT INTO users (login, password) VALUES (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(addUserQuery,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, Encryptor.encryptString(password));

            preparedStatement.executeUpdate();
        });
    }


    /**
     * Получает пароль пользователя с указанным логином.
     * @param username логин пользователя.
     * @return пароль пользователя или null, если пользователь не найден.
     * @throws Exception ошибка при работе с бд.
     */
    public String getPassword(String username) throws Exception {
        return dbConnector.handleQuery((Connection connection) -> {
            String getPasswordQuery = "SELECT (password) FROM users WHERE users.login = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(getPasswordQuery);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password");
            }
            return null;
        });
    }

    /**
     * Проверяет существование пользователя с указанным логином.
     * @param username логин пользователя.
     * @return true, если пользователь существует, иначе false.
     * @throws Exception при работе с базой данных.
     */
    public boolean checkUsersExistence(String username) throws Exception {
        return dbConnector.handleQuery((Connection connection) -> {
            String existenceQuery = "SELECT COUNT (*) FROM users WHERE users.login = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(existenceQuery);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            return resultSet.getInt("count") > 0;
        });
    }

    /**
     * Метод для получения списка id квартир пользователя
     * @param username логин пользователя
     * @return список id квартир пользователя
     * @throws Exception ошибка при работе с бд
     */
    public List<Long> getIdsOfUsersElements(String username) throws Exception {
        return dbConnector.handleQuery((Connection connection) -> {
            String getIdsQuery = "SELECT flats.id FROM flats, users WHERE flats.owner_id = users.id AND users.login = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(getIdsQuery);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Long> resultingList = new ArrayList<>();
            while (resultSet.next()) {
                resultingList.add(resultSet.getLong("id"));
            }

            return resultingList;
        });
    }

    /**
     * Метод для проверки правильности введенных пользователем данных
     * @param username   логин пользователя
     * @param password   пароль пользователя
     * @return true, если данные верны, false в противном случае
     * @throws Exception ошибка при работе с бд
     */
    public boolean validateUser(String username, String password) throws Exception {
        return dbConnector.handleQuery((Connection connection) ->
                Encryptor.encryptString(password).equals(getPassword(username)));
    }
}