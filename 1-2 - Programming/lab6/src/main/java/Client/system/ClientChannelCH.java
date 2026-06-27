package Client.system;

import Common.interfaces.DataReceivable;
import Common.system.Serializer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Класс ClientChannel для обработки ввода/вывода через сетевой канал.
 */
public class ClientChannelCH {
    /**
     * Канал сокета
     */
    private final SocketChannel channel;

    /**
     * Конструктор ClientSocketChannelIO.
     * @param channel    канал SocketChannel
     */
    public ClientChannelCH(SocketChannel channel) {
        this.channel = channel;
    }

    /**
     * Отправляет данные на сервер.
     * @param data         объект типа Data, который нужно отправить.
     */
    public void send(DataReceivable data) {
        try {
            ByteBuffer buffer = Serializer.serializeRequest(data);
            channel.write(buffer);
        } catch(IOException e){
            System.out.println("DataSend error");
        }
    }
    /**
     * Получает данные с сервера.
     * @return объект типа Data
     * IOException ошибка при передаче данных от сервера.
     * ClassNotFoundException не удалось распаковать полученные данные.
     */
    public DataReceivable receive(){
        try {
            ByteBuffer readBuffer = ByteBuffer.allocate(channel.socket().getReceiveBufferSize());
            channel.read(readBuffer);
            return Serializer.deSerializeResponse(readBuffer.array());
        } catch (IOException e) {
            System.out.println("DataReceive error");
        } catch (ClassNotFoundException e){
            System.out.println("Data can not be received");
        }
        return null;
    }
}
