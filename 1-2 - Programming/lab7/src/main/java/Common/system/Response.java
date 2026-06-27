package Common.system;

import Common.interfaces.DataReceivable;
import Common.modules.Flat;

import java.io.Serializable;
import java.util.*;
/**
 * Класс Response - класс, содержащий информацию для ответа на запрос.
 */
public class Response implements Serializable, DataReceivable {

    /**
     * Сообщение, отправляемое в ответ на запрос.
     */
    private String messageToResponse;

    /**
     * Данные о квартире, отправляемые в ответ на запрос.
     */
    private Flat flat;
    /**
     * Данные коллекции, отправляемые в ответ на запрос.
     */
    private PriorityQueue<Flat> collectionToResponse;

    /**
     * Коллекция элементов, не принадлежащих клиенту
     */
    private PriorityQueue<Flat> alienElements;

    /**
     * Список дополнительной информации, содержащейся в ответе сервера
     */
    private List<String> info;

    /**
     * Конструктор класса Response, принимающий сообщение для ответа.
     * @param messageToResponse    сообщение для ответа
     */
    public Response(String messageToResponse) {
        this.messageToResponse = messageToResponse;
    }

    /**
     * Конструктор класса Response, принимающий сообщение и информацию о квартире для ответа.
     *
     * @param messageToResponse  сообщение для ответа
     * @param flat               информация оквартире  для ответа
     */
    public Response(String messageToResponse, Flat flat) {
        this.messageToResponse = messageToResponse;
        this.flat = flat;
    }

    /**
     * Конструктор класса Response, принимающий сообщение и коллекцию квартир для ответа.
     *
     * @param messageToResponse        сообщение для ответа.
     * @param flats     коллекция квартмир для ответа
     */
    public Response(String messageToResponse, PriorityQueue<Flat> flats) {
        this.messageToResponse = messageToResponse;
        this.collectionToResponse = flats;
    }
    /**
     * Конструктор класса для создания ответа сервера, содержащего только сообщение
     * @param messageToResponse сообщение, которое будет отправлено клиенту
     * @param info список дополнительной информации для клиента
     */
    public Response(String messageToResponse, List<String> info) {
        this.messageToResponse = messageToResponse;
        this.info = info;
    }
    /**
     * Конструктор класса для создания ответа сервера, содержащего сообщение и коллекцию элементов
     * @param messageToResponse     сообщение, которое будет отправлено клиенту
     * @param collectionToResponse  коллекция элементов для отправки клиенту
     * @param alienElements         коллекция элементов, не принадлежащих клиенту
     */
    public Response(String messageToResponse, PriorityQueue<Flat> collectionToResponse, PriorityQueue<Flat> alienElements) {
        this.messageToResponse = messageToResponse;
        this.collectionToResponse = collectionToResponse;
        this.alienElements = alienElements;
    }
    /**
     * Конструктор класса Response, принимающий информацию о квартирах для ответа.
     * @param flat    информация о квартирх для ответа
     */
    public Response(Flat flat) {
        this.flat = flat;
    }

    /**
     * Конструктор класса Response, принимающий коллекцию квартир для ответа.
     * @param collectionToResponse    коллекция картир для ответа
     */
    public Response(PriorityQueue<Flat> collectionToResponse) {
        this.collectionToResponse = collectionToResponse;
    }

    /**
     * Метод, возвращающий сообщение для ответа.
     *
     * @return сообщение для ответа
     */
    public String getMessageToResponse() {
        return messageToResponse;
    }

    /**
     * Метод, возвращающий информацию о квартирах для ответа.
     *
     * @return информация о квартирах для ответа
     */
    public Flat getFlatToResponse() {
        return flat;
    }

    public PriorityQueue<Flat> getCollectionToResponse() {
        return collectionToResponse;
    }

    /**
     * Возвращает список дополнительной информации, содержащейся в ответе сервера
     * @return список дополнительной информации
     */
    public List<String> getInfo() {
        return info;
    }

    /**
     * Возвращает коллекцию элементов, не принадлежащих клиенту
     * @return коллекция элементов
     */
    public PriorityQueue<Flat> getAlienElements() {
        return alienElements;
    }


    /**
     * Метод, возвращающий информацию для отправки.
     * @return информация для отправки
     */
    @Override
    public String getData() {
        return (messageToResponse == null ? "" : (getMessageToResponse()))
                + (flat == null ? "" : ("\nДанные квартиры:\n" +  getFlatToResponse().toString()))
                + (collectionToResponse == null ? "" : ("\nКоллекция:\n" + getCollectionToResponse()));
    }

    /**
     * Представляет ответ, полученный от сервера, в формате, удобном для чтения.
     */
    @Override
    public String toString() {
        return "Ответ[" + messageToResponse + "]";
    }
}