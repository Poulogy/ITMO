package Server.system;

import Server.ServerInitConfig;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Console extends Thread {
    private static final Scanner scanner = ServerInitConfig.scanner;

    /**
     * Состояние запущенного потока.
     */
    public volatile boolean running = true;

    /**
     * Запускает поток консоли сервера.
     */
    @Override
    public void run() {
        try {
            ServerInitConfig.logger.info("Console started");
            while (ServerInitConfig.isRunning) {
                String line = scanner.nextLine();
                if ("exit".equalsIgnoreCase(line)) {
                    ServerInitConfig.logger.info("Exiting...");
                    System.exit(0);
                } else {
                    System.out.println("There is no such command. Try'exit'.");
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Error exiting ...");
            ServerInitConfig.logger.warning("Server's work is over.");
            System.exit(1);
        } catch (IndexOutOfBoundsException e) {
            ServerInitConfig.logger.warning(e.getMessage());
        }
    }
}
