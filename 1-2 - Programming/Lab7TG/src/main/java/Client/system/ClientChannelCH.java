package Client.system;

import Common.interfaces.*;
import Common.system.Serializer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Класс ClientChannel для обработки ввода/вывода через сетевой канал.
 */
public class ClientChannelCH implements IOController {
    /**
     * Канал сокета
     */
    private final SocketChannel channel;

    /**
     * Конструктор ClientSocketChannelIO.
     *
     * @param channel канал SocketChannel
     */
    public ClientChannelCH(SocketChannel channel) {
        this.channel = channel;
    }

    /**
     * Отправляет данные на сервер.
     *
     * @param data объект типа Data, который нужно отправить.
     * @throws IOException если произошла ошибка при передаче данных на сервер.
     */
    @Override
    public void send(DataReceivable data) throws IOException {
        ByteBuffer buffer = Serializer.serializeRequest(data);
        channel.write(buffer);
    }

    /**
     * Получает данные с сервера.
     *
     * @return объект типа Data, полученный с сервера.
     * @throws IOException            если произошла ошибка при передаче данных от сервера.
     * @throws ClassNotFoundException если не удалось распаковать полученные данные.
     */
    @Override
    public DataReceivable receive() throws IOException, ClassNotFoundException {
        ByteBuffer readBuffer = ByteBuffer.allocate(channel.socket().getReceiveBufferSize());
        channel.read(readBuffer);
        return Serializer.deSerializeResponse(readBuffer.array());
    }
}