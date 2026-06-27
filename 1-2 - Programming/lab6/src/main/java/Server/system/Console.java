package Server.system;

import Common.modules.Flat;

import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Console extends Thread {
    private static final Scanner scanner = ServerInit.scanner;

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
            ServerInit.logger.info("Console is running.");
            while (running) {
                String line = scanner.nextLine();
                if ("save".equalsIgnoreCase(line)) {
                    ServerInit.dumpManager.writeCollection((PriorityQueue<Flat>) ServerInit.collectionManager.getCollection());
                } else if ("exit".equalsIgnoreCase(line)) {
                    ServerInit.logger.info("Server exiting.");
                    System.exit(0);
                } else {
                    System.out.println("There is no command like this, try 'save' or 'exit'.");
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Exiting.");
            ServerInit.logger.severe("Server exiting.");
            System.exit(1);
        }
    }

}
