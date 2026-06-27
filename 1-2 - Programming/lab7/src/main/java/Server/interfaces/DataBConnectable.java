package Server.interfaces;

import java.sql.Connection;

public interface DataBConnectable {
    /**
     * Обрабатывает запрос, передавая тело запроса в качестве аргумента функции, которая принимает соединение с базой данных.
     * @param queryBody тело запроса, принимающее соединение с бд
     * @throws Exception ошибка при обращении к бд
     */
    void handleQuery(SQLConsumer<Connection> queryBody) throws Exception;

    /**
     * Обрабатывает запрос, передавая тело запроса в качестве аргумента функции, которая принимает соединение с базой данных
     * и возвращает результат запроса.
     * @param queryBody тело запроса, принимающее соединение с базой данных и возвращающее результат запроса.
     * @param <T>       тип результата запроса.
     * @return результат запроса.
     * @throws Exception ошибка при обращении к бд
     */
    <T> T handleQuery(SQLFunction<Connection, T> queryBody) throws Exception;
}
