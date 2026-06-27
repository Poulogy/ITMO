package Server.databases;

import Common.system.*;

import java.util.ArrayList;

/**
 * Менеджер пользователей, отвечающий за регистрацию новых пользователей и авторизацию уже зарегистрированных.
 */
public class UserManagers {

    /**
     * Менеджер базы данных, с которым взаимодействует данный менеджер пользователей.
     */
    private final DatabaseManager dbManager;

    /**
     * Конструктор, инициализирующий менеджер базы данных, с которым будет взаимодействовать данный менеджер пользователей.
     * @param dbManager менеджер базы данных
     */
    public UserManagers(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Регистрирует нового пользователя в базе данных.
     * @param request запрос, содержащий логин и пароль
     * @return ответ с результатом операции (успешно или неуспешно), а также логин
     */
    public Response registerUser(Request request) {
        try {
            if (!dbManager.checkUsersExistence(request.getLogin())) {
                dbManager.addUser(request.getLogin(), request.getPassword());
                ArrayList<String> userData = new ArrayList<>();
                userData.add(request.getLogin());
                userData.add(request.getPassword());
                return new Response("Registration is finished", userData);
            } else {
                ArrayList<String> userData = new ArrayList<>();
                userData.add(request.getLogin());
                return new Response("Username is already used", userData);
            }
        } catch (Exception e) {
            ArrayList<String> userData = new ArrayList<>();
            userData.add(request.getLogin());
            return new Response(e.getMessage(), userData);
        }
    }

    /**
     * Выполняет авторизацию пользователя в системе.
     * @param request запрос, содержащий логин и пароль
     * @return ответ с результатом операции (успешно или неуспешно), а также логин
     */
    public Response logInUser(Request request) {
        try {
            if (dbManager.checkUsersExistence(request.getLogin())) {
                if (dbManager.validateUser(request.getLogin(), request.getPassword())) {
                    ArrayList<String> userData = new ArrayList<>();
                    userData.add(request.getLogin());
                    userData.add(request.getPassword());
                    return new Response("You entered successfully", userData);

                } else {
                    ArrayList<String> userData = new ArrayList<>();
                    userData.add(request.getLogin());
                    return new Response("Password is wrong", userData);
                }
            } else {
                ArrayList<String> userData = new ArrayList<>();
                userData.add(request.getLogin());
                return new Response("There is no user with such username", userData);
            }
        } catch (Exception e) {
            ArrayList<String> userData = new ArrayList<>();
            userData.add(request.getLogin());
            return new Response(e.getMessage(), userData);
        }
    }
}
