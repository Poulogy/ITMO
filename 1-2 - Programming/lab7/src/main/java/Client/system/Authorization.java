package Client.system;

import Client.ClientInit;
import Common.system.*;

import java.io.Console;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Модуль авторизации пользователя в приложении.
 */
public class Authorization {
    /**
     * Сканер
     */
    private final Scanner scanner;

    /**
     * Считывание с консоли
     */
    Console console = System.console();

    /**
     * Режим авторизации
     */
    private boolean authorizationDone = false;

    /**
     * Создает новый объект модуля авторизации пользователя с помощью переданного объекта сканера.
     *
     * @param scanner объект сканера, используемый для ввода данных пользователя.
     */
    public Authorization(Scanner scanner) {
        this.scanner = scanner;
    }


    /**
     * Запрашивает у пользователя информацию о необходимости регистрации в приложении и возвращает соответствующий запрос.
     *
     * @return запрос на регистрацию или вход в систему, созданный на основе ответа пользователя.
     */
    public Request askForRegistration() {
        System.out.println("Do you have an account [y/n]");
        while (true) {
            try {
                String s = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
                if ("y".equals(s)) {
                    return loginUser();
                } else if ("n".equals(s)) {
                    return registerUser();
                } else {
                    System.out.println("You've entered unacceptable symbol");
                }
            } catch (NoSuchElementException e) {
                System.out.println("Error exitting");
                System.exit(1);
            }
        }
    }

    /**
     * Проверяет результат запроса на регистрацию и выводит соответствующее сообщение.
     *
     * @param response объект ответа, содержащий информацию о результате запроса на регистрацию.
     */
    public void validateRegistration(Response response) {
        List<String> usersInfo = response.getInfo();
        if (usersInfo.size() == 2) {
            ClientInit.login = usersInfo.get(0);
            ClientInit.password = usersInfo.get(1);
            System.out.println(response.getMessageToResponse());
            setAuthorizationDone(true);
        } else {
            System.out.println(response.getMessageToResponse());
        }
    }

    /**
     * Запрашивает у пользователя данные для регистрации нового аккаунта и создает на их основе запрос на регистрацию.
     *
     * @return запрос на регистрацию нового аккаунта.
     * @throws NoSuchElementException если ввод пользователя был завершен преждевременно.
     */
    private Request registerUser() throws NoSuchElementException {
        System.out.println("Welcome to registration!");
        String login;
        String password;
        while (true) {
            System.out.println("Enter username (must be 7 symbols).");
            while (true) {
                login = scanner.nextLine().trim();
                if (login.length() < 7) {
                    System.out.println("Username is short, try again");
                    continue;
                }
                break;
            }
            System.out.println("Enter password (must be 5 symbols).");
            while (true) {
                try {
                    password = scanner.nextLine().trim();
                    if (password.length() < 5) {
                        System.out.println("Password too short, try again");
                        continue;
                    }
                    return new Request(login, password, RequestType.REGISTER);
                } catch (NullPointerException e) {
                    System.out.println("Console errors");
                    System.exit(1);
                }
            }
        }
    }

    /**
     * Метод для авторизации пользователя в приложении.
     * @return возвращает объект типа Request, содержащий данные пользователя, если авторизация прошла успешно
     * @throws NoSuchElementException если ввод данных пользователя был прерван
     */
    private Request loginUser() throws NoSuchElementException {
        System.out.println("Добро пожаловать в авторизацию!");
        String login;
        String password;
        while (true) {
            System.out.println("Введите имя пользователя, которое вы будете использовать для работы с приложением (должно содержать не менее 5 символов).");
            while (true) {
                login = scanner.nextLine().trim();
                if (login.length() < 5) {
                    System.out.println("Имя пользователя слишком короткое, попробуйте еще раз.");
                    continue;
                }
                break;
            }
            System.out.println("Введите пароль, который вы будете использовать для работы с приложением (должно содержать не менее 5 символов).");
            while (true) {
                try {
                    password = scanner.nextLine().trim();
                    if (password.length() < 5) {
                        System.out.println("Пароль слишком короткий, попробуйте еще раз.\"");
                        continue;
                    }
                    return new Request(login, password, RequestType.LOGIN);
                } catch (NullPointerException e) {
                    System.out.println("Ошибка при работе с консолью.");
                    System.exit(1);
                }
            }
        }
    }

    /**
     * Метод, возвращающий информацию о том, выполнена ли авторизация в приложении.
     * @return true, если авторизация была выполнена успешно, иначе false.
     */
    public boolean isAuthorizationDone() {
        return authorizationDone;
    }

    /**
     * Метод, устанавливающий флаг выполнения авторизации в приложении.
     * @param authorizationDone флаг выполнения авторизации.
     */
    public void setAuthorizationDone(boolean authorizationDone) {
        this.authorizationDone = authorizationDone;
    }

}