package Server.requestHandler;

import Common.system.*;
import Server.ServerInitConfig;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.function.Supplier;

/**
 * Класс RequestReader представляет собой поставщика запроса, который десериализует полученный буфер данных в объект типа Request.
 */
public class RequestReader implements Supplier<Request> {

    /**
     * Ключ
     */
    private final SelectionKey key;

    /**
     * Создает новый объект типа RequestReader с указанным ключом выборки.
     * @param key ключ выборки
     */
    public RequestReader(SelectionKey key) {
        this.key = key;
    }

    /**
     * Десериализует полученный буфер данных в объект типа Request.
     * @return объект типа Request
     */
    @Override
    public Request get() {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer readBuffer = ByteBuffer.allocate(4096);
        try {
            socketChannel.read(readBuffer);
            return Serializer.deSerializeRequest(readBuffer.array());
        } catch (IOException e) {
            ServerInitConfig.logger.info("Client unconnected");
            try {
                socketChannel.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            return null;
        } catch (ClassNotFoundException e) {
            ServerInitConfig.logger.warning("An attempt to deserialize wrong request");
            return null;
        }
    }
}
