package org.outofrange.crowdsupport.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class BaseDto {
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
