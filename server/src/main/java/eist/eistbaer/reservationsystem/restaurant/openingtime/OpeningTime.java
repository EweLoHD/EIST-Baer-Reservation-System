package eist.eistbaer.reservationsystem.restaurant.openingtime;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@JsonPropertyOrder({ "dayOfWeek", "fromTime", "toTime" })
public class OpeningTime {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    private DayOfWeek dayOfWeek;
    private LocalTime fromTime;
    private LocalTime toTime;

    public OpeningTime() {
    }

    public OpeningTime(DayOfWeek dayOfWeek, LocalTime fromTime, LocalTime toTime) {
        this.dayOfWeek = dayOfWeek;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    @JsonGetter("dayOfWeek")
    public int getDayOfWeekNum() {
        return dayOfWeek.getValue();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getFromTime() {
        return fromTime;
    }

    public void setFromTime(LocalTime fromTime) {
        this.fromTime = fromTime;
    }

    public LocalTime getToTime() {
        return toTime;
    }

    public void setToTime(LocalTime toTime) {
        this.toTime = toTime;
    }

    @Override
    public String toString() {
        return "OpeningTime{" +
                "id=" + id +
                ", dayOfWeek=" + dayOfWeek +
                ", fromTime=" + fromTime +
                ", toTime=" + toTime +
                '}';
    }
}
