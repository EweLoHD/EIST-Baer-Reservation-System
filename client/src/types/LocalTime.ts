export default class LocalTime {
    hours: Number
    minutes: Number
    seconds: Number

    constructor(hours: Number,  minutes: Number, seconds: Number) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    static parse(text: String): LocalTime {
        var split: Array<Number> = text.split(':').map(t => parseInt(t));
        return new LocalTime(split[0], split[1], split[2])
    }
}