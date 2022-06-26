package eist.eistbaer.reservationsystem.restaurant.table.tableType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TableType {
    REGULAR("Regular Table"),
    OUTDOOR("Outdoor Table"),
    HIGH_TOP("High-Top Table"),
    BAR("Bar"),
    PRIVATE_ROOM ("Table in private Room");

    private final String name;

    TableType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }

    @JsonCreator
    public static TableType of(String value) {
        return TableType.valueOf(value);
    }

}
