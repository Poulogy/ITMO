package Server.managers;

import Common.interfaces.DataReceivable;
import Common.system.Request;

import java.io.*;
import java.net.*;

/**
 * Класс ServerSocketIO предоставляет методы для управления вводом/выводом данных в сокете на стороне сервера.
 */
public class ServerSocketCH {

    /**
     * Сокет
     */
    private final Socket socket;

    /**
     * Конструктор принимает сокет для дальнейшей работы с ним.
     * @param socket - сокет
     */
    public ServerSocketCH(Socket socket) {
        this.socket = socket;
    }

    /**
     * Метод receive() используется для чтения данных из сокета и десериализации объекта Request.
     * @return объект Request, полученный из сокета.
     * @throws IOException - выбрасывается в случае ошибки ввода/вывода.
     * @throws ClassNotFoundException - выбрасывается, когда не удается найти класс, необходимый для десериализации.
     */
    public DataReceivable receive() throws IOException, ClassNotFoundException {
        ObjectInput objectInput = new ObjectInputStream(socket.getInputStream());
        return (Request) objectInput.readObject();
    }

    /**
     * Метод send() используется для записи данных в сокет в формате объекта.
     * @param data - объект Data, который необходимо записать в сокет.
     * @throws IOException - выбрасывается в случае ошибки ввода/вывода.
     */
    public void send(DataReceivable data) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(data);
        objectOutputStream.flush();
        byteArrayOutputStream.writeTo(outputStream);
        byteArrayOutputStream.close();
    }
}
