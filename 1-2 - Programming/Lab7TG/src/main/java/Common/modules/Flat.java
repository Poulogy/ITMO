package Common.modules;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Модуль Flat
 *
 * @author Nazerke
 */
public class Flat implements Comparable<Flat>, Serializable {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public Transport getTransport() {
        return transport;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getName() {
        return name;
    }

    private Coordinates coordinates; //Поле не может быть null
    private int numberOfRooms; //Максимальное значение поля: 14, Значение поля должно быть больше 0
    private long numberOfBathrooms; //Значение поля должно быть больше 0
    private Transport transport; //Поле может быть null
    private House house; //Поле может быть null
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private double area; //Максимальное значение поля: 539, Значение поля должно быть больше 0

    /**
     * конструктор Flat
     * @param id                 ид
     * @param coordinates        координаты
     * @param numberOfRooms      количество комнат
     * @param numberOfBathrooms  количество ванн
     * @param transport          транспорт
     * @param house              дом
     * @param creationDate       дата создания
     * @param name               имя
     * @param area               площадь
     */

    public Flat(Long id, Coordinates coordinates, int numberOfRooms, long numberOfBathrooms, Transport transport, House house, LocalDateTime creationDate, String name, double area) {
        this.id = id;
        this.coordinates = coordinates;
        this.numberOfRooms = numberOfRooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.transport = transport;
        this.house = house;
        this.creationDate = creationDate;
        this.name = name;
        this.area = area;
    }

    /**
     * Получение ид
     * @return ид
     */

    public Long getId() {
        return id;
    }

    /**
     * Установка ид
     * @param id ид
     */

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Установка координат
     * @param coordinates координаты
     */

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Установка количества комнат
     * @param numberOfRooms количество комнат
     */

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    /**
     * Получение количества ванн
     * @return количество ванн
     */

    public long getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    /**
     * Установка количества ванн
     * @param numberOfBathrooms количество ванн
     */

    public void setNumberOfBathrooms(long numberOfBathrooms) {
        this.numberOfBathrooms = numberOfBathrooms;
    }

    /**
     * Установка транспорта
     * @param transport транспорт
     */

    public void setTransport(Transport transport) {
        this.transport = transport;
    }
    /**
     * Установка транспорта
     * @param transport транспорт
     */

    public Transport getTransport(Transport transport) {
        return transport;
    }


    /**
     * Получение дома
     * @return дом
     */
    public House getHouse() {
        return house;
    }

    /**
     * Установка дома
     * @param house дом
     */

    public void setHouse(House house) {
        this.house = house;
    }

    /**
     * Пустой конструктор
     */
    public Flat() {
    }

    /**
     * Установить дату создания
     * @param creationDate дата создания
     */
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Установить имя
     * @param name имя
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Получить площадь
     * @return площадь
     */
    public double getArea() {
        return area;
    }

    /**
     * Установаить площадь
     * @param area площадь
     */
    public void setArea(double area) {
        this.area = area;
    }

    @Override
    public int compareTo(Flat o1) {
        return (int) (this.getArea() - o1.getArea());
    }

    @Override
    public String toString() {
        return "Flat{" +
                "id=" + id +
                ", coordinates=" + coordinates +
                ", numberOfRooms=" + numberOfRooms +
                ", numberOfBathrooms=" + numberOfBathrooms +
                ", transport=" + transport +
                ", house=" + house +
                ", creationDate=" + creationDate +
                ", name='" + name + '\'' +
                ", area=" + area +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Flat flat)) return false;
        return numberOfRooms == flat.numberOfRooms && numberOfBathrooms == flat.numberOfBathrooms && Double.compare(flat.area, area) == 0 && Objects.equals(id, flat.id) && Objects.equals(coordinates, flat.coordinates) && transport == flat.transport && Objects.equals(house, flat.house) && Objects.equals(creationDate, flat.creationDate) && Objects.equals(name, flat.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, coordinates, numberOfRooms, numberOfBathrooms, transport, house, creationDate, name, area);
    }
}
