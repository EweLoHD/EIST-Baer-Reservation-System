package eist.eistbaer.reservationsystem.restaurant.review;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eist.eistbaer.reservationsystem.restaurant.reviewrating.ReviewRating;
import org.apache.tomcat.jni.Local;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class Review {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Lob
    private String comment;

    private ReviewRating rating;

    @CreationTimestamp
    private LocalDate creationDate;

    public Review() {
    }

    public Review(String comment, ReviewRating rating) {
        this.comment = comment;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ReviewRating getRating() {
        return rating;
    }

    public void setRating(ReviewRating rating) {
        this.rating = rating;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", rating=" + rating +
                ", creationDate=" + creationDate +
                '}';
    }
}
