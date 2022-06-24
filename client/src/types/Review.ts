import type LocalDate from "./LocalDate";

export default class Review {
    rating: Number
    comment: String
    creationDate: LocalDate

    constructor(rating: Number, comment: String, creationDate: LocalDate) {
        this.rating = rating;
        this.comment = comment;
        this.creationDate = creationDate;
    }
}