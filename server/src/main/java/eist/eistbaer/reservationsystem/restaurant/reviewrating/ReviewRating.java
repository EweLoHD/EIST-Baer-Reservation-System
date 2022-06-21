package eist.eistbaer.reservationsystem.restaurant.reviewrating;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ReviewRating {
        ONE_STAR(1),
        TWO_STAR(2),
        THREE_STAR(3),
        FOUR_STAR(4),
        FIVE_STAR(5);

        private final int rating;
    ReviewRating(int rating) {
            this.rating = rating;
        }

        public static ReviewRating of(int num) {
            for (ReviewRating c : values()) {
                if (c.rating == num) {
                    return c;
                }
            }
            throw new IllegalArgumentException("No Rating Category with value " + num);
        }

        @JsonCreator
        public static ReviewRating of(Object price) {
            if (price instanceof Integer) {
                return of((int) price);
            } else if (price instanceof String) {
                return valueOf((String) price);
            } else {
                return null;
            }
        }

        @JsonValue
        public int num() {
            return rating;
        }
}
