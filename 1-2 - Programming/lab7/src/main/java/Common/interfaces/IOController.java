package Common.interfaces;

import java.io.IOException;

/**
 * Интерфейс валидации
 */
public interface IOController {
    /**
     * Отправка данных
     * @param data данные для отправки
     * @throws IOException если произошла ошибка ввода/вывода
     */
    void send(DataReceivable data) throws IOException;

    /**
     * Получение данных
     * @return полученные данные
     * @throws IOException если произошла ошибка ввода/вывода
     * @throws ClassNotFoundException если класс не найден
     */
    DataReceivable receive() throws IOException, ClassNotFoundException;
}