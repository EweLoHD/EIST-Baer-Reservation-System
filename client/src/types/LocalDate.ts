export default class LocalDate {
    day: number
    month: number
    year: number

    constructor(day: number, month: number, year: number) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    static parse(text: string): LocalDate {
        var split: Array<number> = text.split("-").map(x => parseInt(x));
        return new LocalDate(split[2], split[1], split[0])
    }

    getFormated(): String {
        return this.day + '.' + this.month + '.' + this.year;
    }
}