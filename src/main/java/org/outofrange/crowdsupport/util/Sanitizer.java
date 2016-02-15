package org.outofrange.crowdsupport.util;

/**
 * This utility class can be used to sanitize input, so it complies to certain constraints.
 */
public class Sanitizer {
    private Sanitizer() { /* no instantiation */ }

    /**
     * This method will validate {@code name}, add the {@code prefix} if necessary and uppercase all characters.
     *
     * @param name   the name to process.
     * @param prefix an optional prefix, can be {@code null}.
     * @return the sanitized authority name.
     * @throws NullPointerException     if {@code name} is null.
     * @throws IllegalArgumentException if {@code name} is empty or contains other characters than letters, numbers
     *                                  or underscores.
     * @see #sanitizeAuthorityName(String)
     */
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

    /**
     * This method will validate {@code name} and uppercase all characters.
     * <p>
     * Effectively the same as calling {@code Sanitizer.sanitizeAuthorityName(name, null)}.
     *
     * @param name the name to process.
     * @return the sanitized authority name.
     * @throws NullPointerException     if {@code name} is null.
     * @throws IllegalArgumentException if {@code name} is empty or contains other characters than letters, numbers
     *                                  or underscores.
     * @see #sanitizeAuthorityName(String, String)
     */
    public static String sanitizeAuthorityName(String name) {
        return sanitizeAuthorityName(name, null);
    }
}
