package Common.system;

import Common.interfaces.DataReceivable;
import Common.modules.Flat;

import java.io.Serializable;
import java.util.PriorityQueue;

/**
 * Класс Response - класс, содержащий информацию для ответа на запрос.
 */
public class Response implements Serializable, DataReceivable {

    /**
     * Сообщение, отправляемое в ответ на запрос.
     */
    private String messageToResponse;

    /**
     * Данные об организации, отправляемые в ответ на запрос.
     */
    private Flat flat;

    /**
     * Данные коллекции, отправляемые в ответ на запрос.
     */
    private PriorityQueue<Flat> collectionToResponse;

    /**
     * Конструктор класса Response, принимающий сообщение для ответа.
     * @param messageToResponse    сообщение для ответа
     */
    public Response(String messageToResponse) {
        this.messageToResponse = messageToResponse;
    }

    /**
     * Конструктор класса Response, принимающий сообщение и информацию об организации для ответа.
     *
     * @param messageToResponse  сообщение для ответа
     * @param flat               информация об организации для ответа
     */
    public Response(String messageToResponse, Flat flat) {
        this.messageToResponse = messageToResponse;
        this.flat = flat;
    }

    /**
     * Конструктор класса Response, принимающий сообщение и коллекцию организаций для ответа.
     *
     * @param messageToResponse        сообщение для ответа.
     * @param collectionToResponse     коллекция организаций для ответа
     */
    public Response(String messageToResponse, PriorityQueue<Flat> collectionToResponse) {
        this.messageToResponse = messageToResponse;
        this.collectionToResponse = collectionToResponse;
    }

    /**
     * Конструктор класса Response, принимающий информацию об организации для ответа.
     * @param flat    информация об организации для ответа
     */
    public Response(Flat flat) {
        this.flat = flat;
    }

    /**
     * Конструктор класса Response, принимающий коллекцию организаций для ответа.
     *
     * @param collectionToResponse    коллекция организаций для ответа
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
     * Метод, возвращающий информацию об организации для ответа.
     *
     * @return информация об организации для ответа
     */
    public Flat getFlatToResponse() {
        return flat;
    }

    /**
     * Метод, возвращающий коллекцию организаций для ответа.
     *
     * @return коллекция организаций для ответа
     */
    public PriorityQueue<Flat> getCollectionToResponse() {
        return collectionToResponse;
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