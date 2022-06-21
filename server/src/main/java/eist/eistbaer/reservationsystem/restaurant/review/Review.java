package eist.eistbaer.reservationsystem.restaurant.review;

import eist.eistbaer.reservationsystem.restaurant.reviewrating.ReviewRating;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Review {
    @Id
    @GeneratedValue
    private Long id;

    private String comment;

    private ReviewRating rating;
}
