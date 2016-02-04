package org.outofrange.crowdsupport.hibernate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

@Converter(autoApply = true)
public class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, Date> {
    @Override
    public Date convertToDatabaseColumn(ZonedDateTime value) {
        if (value == null){
            return null;
        }
        return Date.from(value.toInstant());
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(Date value) {
        if (value == null) {
            return null;
        }

        return ZonedDateTime.ofInstant(value.toInstant(), ZoneOffset.UTC);
    }
} 