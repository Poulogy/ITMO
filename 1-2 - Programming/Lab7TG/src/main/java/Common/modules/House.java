package Common.modules;

import java.io.Serializable;
import java.util.Objects;

/**
 * Модуль Дом
 *
 * @author Nazerke
 */
public class House implements Serializable {
    private String name; //Поле не может быть null

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public Long getNumberOfFlatsOnFloor() {
        return numberOfFlatsOnFloor;
    }

    private int year; //Максимальное значение поля: 153, Значение поля должно быть больше 0
    private Long numberOfFlatsOnFloor; //Значение поля должно быть больше 0

    /**
     * Конструктор дома
     */
    public House() {
    }
    /**
     * Конструктор, задающий параметры адреса
     * @param name -                 имя
     * @param year -                 год
     * @param numberOfFlatsOnFloor - количество клвартир на этаже
     */
    public House(String name, int year, Long numberOfFlatsOnFloor){
        this.name = name;
        this.year = year;
        this.numberOfFlatsOnFloor = numberOfFlatsOnFloor;
    }

    /**
     * Установка имени
     * @param name имя
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Установка года
     * @param year год
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Установка количества квартир на этаже
     * @param numberOfFlatsOnFloor количество квартир на этаже
     */
    public void setNumberOfFlatsOnFloor(Long numberOfFlatsOnFloor) {
        this.numberOfFlatsOnFloor = numberOfFlatsOnFloor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof House house)) return false;
        return year == house.year && Objects.equals(name, house.name) && Objects.equals(numberOfFlatsOnFloor, house.numberOfFlatsOnFloor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, year, numberOfFlatsOnFloor);
    }

    @Override
    public String toString() {
        return "House{" +
                "name='" + name + '\'' +
                ", year=" + year +
                ", numberOfFlatsOnFloor=" + numberOfFlatsOnFloor +
                '}';
    }
}
