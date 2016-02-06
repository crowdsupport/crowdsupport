package org.outofrange.crowdsupport.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TagTest {
    @Test(expected = NullPointerException.class)
    public void noCreationWithNull() {
        new Tag(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noCreationWithEmpty() {
        new Tag("");
    }

    @Test
    public void noCreationWithAnythingButLetters() {
        final String[] invalidNames = new String[]{"tag1name", "tag name"};

        for (String invalidName : invalidNames) {
            try {
                new Tag(invalidName);
                throw new AssertionError("Constructor didn't threw exception when creating with " + invalidName);
            } catch (IllegalArgumentException e) {
                // that's okay
            }
        }
    }

    @Test
    public void creationWithUppercaseLettersWillGetLowercased() {
        final Tag tag = new Tag("tAgNaMe");

        assertEquals("tagname", tag.getName());
    }

    @Test
    public void creationWithNormalNamesWorks() {
        final String[] validNames = new String[]{"tagname", "tagwithöumlaut"};

        for (String validName : validNames) {
            new Tag(validName);
        }
    }
}