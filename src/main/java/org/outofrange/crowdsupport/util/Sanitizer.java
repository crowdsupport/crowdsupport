package org.outofrange.crowdsupport.util;

public class Sanitizer {
    private Sanitizer() { /* no instantiation */ }

    public static String sanitizeAuthorityName(String name, String prefix) {
        String sanitizedName = Validate.notNullOrEmpty(name).toUpperCase();

        if (sanitizedName.matches(".*[^A-Z_0-9].*")) {
            throw new IllegalArgumentException("Name can only contain letters, numbers and underscore");
        }

        if (prefix != null) {
            if (sanitizedName.length() < prefix.length() ||
                    !sanitizedName.substring(0, prefix.length()).equalsIgnoreCase(prefix)) {
                sanitizedName = prefix + sanitizedName;
            }

            if (sanitizedName.substring(prefix.length()).isEmpty()) {
                throw new IllegalArgumentException("Name must not be " + sanitizedName);
            }
        }

        return sanitizedName;
    }

    public static String sanitizeAuthorityName(String name) {
        return sanitizeAuthorityName(name, null);
    }
}
