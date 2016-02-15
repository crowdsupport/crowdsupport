package org.outofrange.crowdsupport.util;

import com.google.common.base.Preconditions;

/**
 * Utility class to allow convenient parameter checks.
 */
public class Validate {
    private Validate() { /* no instantiation */ }

    /**
     * Checks for not null.
     *
     * @param object the object to check
     * @return {@code object}
     * @throws NullPointerException when {@code object} is null
     */
    public static <T> T notNull(T object) {
        return Preconditions.checkNotNull(object);
    }

    /**
     * Checks for not null and non-emptiness.
     *
     * @param string the string to check
     * @return {@code string}
     * @throws NullPointerException     when {@code string} is null
     * @throws IllegalArgumentException when {@code string} is empty
     */
    public static String notNullOrEmpty(String string) {
        if (notNull(string).isEmpty()) {
            throw new IllegalArgumentException("String " + string + " must not be empty");
        }

        return string;
    }

    /**
     * Checks if a string doesn't match a certain regular expression.
     *
     * @param string  the string to check
     * @param pattern the regular expression to use
     * @return {@code string}
     * @throws NullPointerException     when {@code string} or {@code regex} is null
     * @throws IllegalArgumentException when {@code string} or {@code regex} is empty, or when {@code string} matches
     *                                  {@code pattern}
     */
    public static String doesntMatch(String string, String pattern) {
        notNullOrEmpty(string);
        notNullOrEmpty(pattern);

        if (string.matches(pattern)) {
            throw new IllegalArgumentException(string + " must not match regular expression " + pattern);
        }

        return string;
    }

    /**
     * Checks if {@code number} is at least {@code 0}.
     *
     * @param number the number to check
     * @return {@code number}
     * @throws IllegalArgumentException when {@code number < 0}
     */
    public static int greaterOrEqualZero(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Number " + number + " must not be smaller than zero!");
        }

        return number;
    }
}
