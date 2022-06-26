package eist.eistbaer.reservationsystem.restaurant.priceCategory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PriceCategoryConverter implements AttributeConverter<PriceCategory, Integer> {
    @Override
    public Integer convertToDatabaseColumn(PriceCategory attribute) {
        if (attribute == null) return null;
        return attribute.num();
    }

    @Override
    public PriceCategory convertToEntityAttribute(Integer dbData) {
        return PriceCategory.of(dbData);
    }
}
