package eist.eistbaer.reservationsystem.restaurant.openingtime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class OpeningTime {
    @Id
    @GeneratedValue
    private Long id;

    private int dayOfWeek;

    private int fromTime;

    private int toTime;


    public OpeningTime(int dayOfWeek, int fromTime, int toTime){
        this.dayOfWeek = dayOfWeek;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public OpeningTime() {
    }


    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getFromTime() {
        return fromTime;
    }

    public void setFromTime(int from) {
        this.fromTime = from;
    }

    public int getToTime() {
        return toTime;
    }

    public void setToTime(int to) {
        this.toTime = to;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "OpeningTime{" +
                "id=" + id +
                ", dayOfWeek=" + dayOfWeek +
                ", from=" + fromTime +
                ", to=" + toTime +
                '}';
    }
}
