import DayOfWeek from "./DayOfWeek";
import LocalTime from "./LocalTime";

class OpeningTime {
    dayOfWeek: DayOfWeek
    fromTime: LocalTime
    toTime: LocalTime

    constructor(dayOfWeek: Number, fromTime: String, toTime: String) {
        this.dayOfWeek = DayOfWeek.of(dayOfWeek);
        this.fromTime = LocalTime.parse(fromTime);
        this.toTime = LocalTime.parse(toTime);
    }
}

export default OpeningTime;