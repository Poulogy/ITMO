package Server;

import Common.system.*;
import Server.requestHandler.*;
import Server.system.Console;

import java.io.IOException;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
/**
 * Класс для установки соединения с клиентами.
 */
public class ServerApp {
    /**
     * Селектор для мониторинга каналов ввода-вывода.
     */
    private volatile Selector selector;
    /**
     * Пул потоков для чтения запросов от клиентов.
     */
    private final ForkJoinPool forkJoinPool1 = new ForkJoinPool(10);
    /**
     * Пул потоков для обработки запросов от клиентов.
     */
    private final ForkJoinPool forkJoinPool2 = new ForkJoinPool(10);
    /**
     * Пул потоков для отправки ответов клиентам.
     */
    private final ForkJoinPool forkJoinPool3 = new ForkJoinPool(10);
    /**
     * Множество ключей, которые в данный момент обрабатываются.
     */
    private final Set<SelectionKey> workingKeys =
            Collections.synchronizedSet(new HashSet<>());
    /**
     * Серверный канал для прослушивания входящих соединений.
     */
    private final ServerSocketChannel server;

    public ServerApp(ServerSocketChannel server) {
        this.server = server;
    }

    /**
     * Метод для запуска сервера и обработки запросов от клиентов.
     */
        public void start() {
            Console consoleThread = new Console();
            if (ServerInitConfig.isRunning) {
                consoleThread.start();
                startServer();
            }
            forkJoinPool1.shutdown();
            forkJoinPool2.shutdown();
            forkJoinPool3.shutdown();
        }

        /**
         * Метод для запуска сервера и инициализации селектора.
         */
        private void startServer() {
            ServerInitConfig.logger.info("Server is running.");
            try {
                selector = Selector.open();
                ServerSocketChannel server = initChannel(selector);
                startSelectorLoop(server);
            } catch (IOException e) {
                ServerInitConfig.logger.warning("Problems with IO");
                ServerInitConfig.toggleStatus();
            } catch (ClassNotFoundException e) {
                ServerInitConfig.logger.warning("Attempt to serialize an object that is not serialized.");
                ServerInitConfig.toggleStatus();
            }
        }

        /**
         * Метод для запуска цикла селектора, который прослушивает подключения.
         * @param channel канал серверного сокета, который будет слушать подключения.
         * @throws IOException если произошла ошибка при работе с каналом.
         * @throws ClassNotFoundException если не найден класс.
         */
        private void startSelectorLoop(ServerSocketChannel channel) throws IOException, ClassNotFoundException {
            while (channel.isOpen() && ServerInitConfig.isRunning) {
                if (selector.select(1) != 0) {
                    startIteratorLoop(channel);
                }
            }
        }

        /**
         * Метод для запуска цикла итератора, который обрабатывает готовые к обработке ключи селектора.
         * @param channel канал серверного сокета, который будет слушать подключения.
         * @throws IOException если произошла ошибка при работе с каналом.
         */
        private void startIteratorLoop(ServerSocketChannel channel) throws IOException {
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isValid() && !workingKeys.contains(key)) {
                    if (key.isAcceptable()) {
                        accept(channel);
                    } else if (key.isReadable()) {
                        workingKeys.add(key);
                        ServerInitConfig.logger.info("Client sent request");
                        Supplier<Request> requestReader = new RequestReader(key);
                        Function<Request, Response> requestExecutor = new RequestBuilder();
                        Consumer<Response> responseSender = new ResponseSender(key, workingKeys);
                        CompletableFuture
                                .supplyAsync(requestReader, forkJoinPool1)
                                .thenApplyAsync(requestExecutor, forkJoinPool2)
                                .thenAcceptAsync(responseSender, forkJoinPool3);
                    }
                }
            }
        }

        /**
         * Метод для принятия входящих подключений.
         * @param channel канал серверного сокета, который будет слушать подключения.
         * @throws IOException если произошла ошибка при работе с каналом.
         */
        private void accept(ServerSocketChannel channel) throws IOException {
            SocketChannel socketChannel = channel.accept();
            ServerInitConfig.logger.info("Server received a client connection");
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        }

        /**
         * Метод для инициализации серверного канала.
         * @param selector селектор, который будет использоваться для регистрации канала и слушания событий.
         * @return инициализированный серверный канал.
         * @throws IOException если произошла ошибка при работе с каналом.
         */
        private ServerSocketChannel initChannel(Selector selector) throws IOException {
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);
            return server;
        }
    }