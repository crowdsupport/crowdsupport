package org.outofrange.crowdsupport.util;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public class Reflection {
    private Reflection() { /* no instantiation */ }

    public static void setField(Object target, String fieldName, Object value) {
        Field field = ReflectionUtils.findField(target.getClass(), fieldName);
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, target, value);
    }
}
