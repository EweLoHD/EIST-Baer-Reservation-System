export default class DayOfWeek {
    value: Number

    private constructor(value: Number) {
        this.value = value;
    }

    static of(value: Number): DayOfWeek {
        if (value < 1 || value > 7) {
            throw "Invalid value for DayOfWeek! Value has to be between 0-6"
        }
        return new DayOfWeek(value);
    }

    getName(): String {
        switch(this.value) {
            case 1: return "Monday";
            case 2: return "Tuesday";
            case 3: return "Wednesday";
            case 4: return "Thursday";
            case 5: return "Friday";
            case 6: return "Saturday";
            case 7: return "Sunday";
        }

        return "";
    }
}