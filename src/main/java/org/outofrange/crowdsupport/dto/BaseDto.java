package org.outofrange.crowdsupport.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Base DTO, meant for DTOs representing models inheriting from {@link org.outofrange.crowdsupport.model.BaseEntity}
 */
public class BaseDto {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
