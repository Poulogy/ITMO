package Common.system;

import Common.interfaces.DataReceivable;
import Common.modules.Flat;

import java.io.Serializable;

public class Request implements Serializable, DataReceivable {

    /**
     * Имя команды.
     */
    private final String commandName;

    /**
     * Числовой аргумент команды.
     */
    private Long IdArgument;

    /**
     * Аргумент команды - объект Flat.
     */
    private Flat flat;

    /**
     * Конструктор класса, принимающий имя команды.
     * @param commandName      имя команды
     */
    public Request(String commandName) {
        this.commandName = commandName;
    }

    /**
     * Конструктор класса, принимающий имя команды и числовой аргумент.
     * @param commandName       имя команды
     * @param IdArgument        числовой аргумент команды
     */
    public Request(String commandName, Long IdArgument) {
        this.commandName = commandName;
        this.IdArgument = IdArgument;
    }

    /**
     * Конструктор класса, принимающий имя команды и объект Organization.
     * @param commandName   имя команды
     * @param flat          объект организации
     */
    public Request(String commandName, Flat flat) {
        this.commandName = commandName;
        this.flat = flat;
    }

    /**
     * Конструктор класса, принимающий имя команды, числовой аргумент и объект Organization.
     * @param commandName       имя команды
     * @param IdArgument        числовой аргумент команды
     * @param flat              объект организации
     */
    public Request(String commandName, Long IdArgument, Flat flat) {
        this.commandName = commandName;
        this.IdArgument = IdArgument;
        this.flat = flat;
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
