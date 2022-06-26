package eist.eistbaer.reservationsystem.restaurant.table.tableType;

import javax.persistence.AttributeConverter;

public class TableTypeConverter implements AttributeConverter<TableType, String> {
    @Override
    public String convertToDatabaseColumn(TableType attribute) {
        return attribute.toString();
    }

    @Override
    public TableType convertToEntityAttribute(String dbData) {
        return TableType.valueOf(dbData);
    }
}
