package Common.system;


import Common.interfaces.DataReceivable;

import java.io.*;
import java.nio.*;

/**
 * Класс для сереализации и десереализации объектов запросов и ответов
 */
public class Serializer {
    /**
     * Метод для сереализации объекта запроса
     * @param request       объект запроса
     * @return буфер байтов с сериализованным запросом
     * @throws IOException    если произошла ошибка ввода-вывода при сериализации
     */
    public static ByteBuffer serializeRequest(DataReceivable request) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(request);
        objectOutputStream.flush();
        ByteBuffer bufToSend = ByteBuffer.wrap(byteArrayOutputStream.toByteArray()); // Создаем байтовый буфер на основе массива байт из байтового массива
        objectOutputStream.close();
        byteArrayOutputStream.close();
        return bufToSend;
    }

    /**
     * Метод для десериализации объекта ответа
     * @param acceptedBuf              буфер байтов с сериализованным ответом
     * @return объект ответа
     * @throws IOException             если произошла ошибка ввода-вывода при десериализации
     * @throws ClassNotFoundException  если класс объекта не найден при десериализации
     */
    public static Response deSerializeResponse(byte[] acceptedBuf) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(acceptedBuf);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Response response = (Response) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return response;
    }
}