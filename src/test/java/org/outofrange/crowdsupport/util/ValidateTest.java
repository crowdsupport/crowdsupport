package org.outofrange.crowdsupport.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ValidateTest {
    @Test(expected = NullPointerException.class)
    public void nullThrowsExceptionWhenNull() {
        Validate.notNull(null);
    }

    @Test
    public void nullDoesntThrowExceptionWhenNotNullAndReturnsObject() {
        final String s = "string";
        assertEquals(s, Validate.notNull(s));
    }

    @Test(expected = NullPointerException.class)
    public void nullOrEmptyThrowsExceptionWhenNull() {
        Validate.notNullOrEmpty(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullOrEmptyThrowsExceptionWhenEmpty() {
        Validate.notNullOrEmpty("");
    }

    @Test
    public void nullOrEmptyDoesntThrowExceptionWhenNotEmptyStringAndReturnsString() {
        final String s = "string";
        assertEquals(s, Validate.notNullOrEmpty(s));
    }
}