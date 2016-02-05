package org.outofrange.crowdsupport.util;

import com.google.common.base.Preconditions;

public class Validate {
    private Validate() { /* no instantiation */ }

    public static <T> T notNull(T object) {
        return Preconditions.checkNotNull(object);
    }

    public static String notNullOrEmpty(String string) {
        if (notNull(string).isEmpty()) {
            throw new IllegalArgumentException("String " + string + " must not be empty");
        }

        return string;
    }
}