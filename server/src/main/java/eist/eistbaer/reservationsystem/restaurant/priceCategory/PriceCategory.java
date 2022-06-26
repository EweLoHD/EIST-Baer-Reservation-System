package eist.eistbaer.reservationsystem.restaurant.priceCategory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PriceCategory {

    ONE(1),
    TWO(2),
    THREE(3);

    private final int price;
    PriceCategory(int price) {
        this.price = price;
    }

    public static PriceCategory of(int num) {
        for (PriceCategory c : values()) {
            if (c.price == num) {
                return c;
            }
        }
        throw new IllegalArgumentException("No Price Category with value " + num);
    }

    @JsonCreator
    public static PriceCategory of(Object price) {
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
        return price;
    }
}
