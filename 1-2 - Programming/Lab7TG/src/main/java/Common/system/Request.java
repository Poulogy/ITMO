package Common.system;

import Common.interfaces.DataReceivable;
import Common.modules.Flat;

import java.io.Serializable;

public class Request implements Serializable, DataReceivable {

    /**
     * Имя команды.
     */
    private String commandName;

    /**
     * Числовой аргумент команды.
     */
    private Long IdArgument;

    /**
     * Аргумент команды - объект Flat.
     */
    private Flat flat;
    /**
     * Логин
     */
    private String login;
    /**
     * Пароль
     */
    private String password;
    /**
     * Тип Запроса (COMMAND, REGISTER, LOGIN)
     */
    private final RequestType type;

    /**
     * Конструктор класса, принимающий имя команды.
     * @param commandName      имя команды
     * @param type             тип запроса
     */
    public Request(String commandName, RequestType type) {
        this.commandName = commandName;
        this.type = type;
    }
    /**
     * Конструктор класса, принимающий имя команды и числовой аргумент.
     * @param commandName       имя команды
     * @param IdArgument        числовой аргумент команды
     * @param type             тип запроса
     */
    public Request(String commandName, Long IdArgument, RequestType type) {
        this.commandName = commandName;
        this.IdArgument = IdArgument;
        this.type = type;
    }
    /**
     * Конструктор класса, принимающий имя команды и объект Квартир
     * @param commandName   имя команды
     * @param flat          объект организации
     * @param type             тип запроса
     */
    public Request(String commandName, Flat flat, RequestType type) {
        this.commandName = commandName;
        this.flat = flat;
        this.type = type;
    }
    /**
     * Конструктор класса, принимающий имя команды, числовой аргумент и объект Organization.
     * @param commandName       имя команды
     * @param IdArgument        числовой аргумент команды
     * @param flat              объект организации
     * @param type             тип запроса
     */
    public Request(String commandName, Long IdArgument, Flat flat, RequestType type) {
        this.commandName = commandName;
        this.IdArgument = IdArgument;
        this.flat = flat;
        this.type = type;
    }
    /**
     * Конструктор класса, принимающий имя команды, числовой аргумент и объект Organization.
     * @param login
     * @param password
     * @param type             тип запроса
     */
    public Request(String login, String password, RequestType type) {
        this.login = login;
        this.password = password;
        this.type = type;
    }

    /**
     * Возвращает имя команды.
     * @return имя команды
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * Возвращает числовой аргумент.
     * @return числовой аргумент команды
     */
    public Long getIDArgument() {
        return IdArgument;
    }

    /**
     * Возвращает объект организации.
     * @return объект организации
     */
    public Flat getFlatArgument() {
        return flat;
    }

    public RequestType getRequestType() {
        return type;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Возвращает строковое представление данных объекта в виде имени команды и соответствующих аргументов.
     */
    @Override
    public String getData(){
        return "Имя команды для отправки: " + commandName
                + (flat == null ? "" : ("\nИнформация о квартире для отправки:\n " + flat))
                + (IdArgument == null ? "" : ("\nЧисловой аргумент (ид) для отправки:\n " + IdArgument));
    }

    /**
     * Возвращает строковое представление объекта в формате "Ответ[имя команды]".
     */
    @Override
    public String toString() {
        return "Ответ[" + commandName + "]" ;
    }
}
