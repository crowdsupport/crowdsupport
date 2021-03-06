package org.outofrange.crowdsupport.spring.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.outofrange.crowdsupport.dto.UserAuthDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

/**
 * This class is responsible for creating new JSON Web Tokens and parse existing ones.
 */
public final class TokenHandler {
    private static final Logger log = LoggerFactory.getLogger(TokenHandler.class);

    /**
     * The Header to use in tokens.
     */
    private static class Header {
        public String alg = "HS256";
        public String typ = "JWT";
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * The header is converted to JSON and Base64 when loading the TokenHandler
     */
    private static final String HEADER_BASE64 = toBase64(toJSON(new Header()));

    private static final String SEPARATOR = ".";
    private static final String SEPARATOR_SPLITTER = "\\.";

    private final Mac hmac;

    public TokenHandler(byte[] secretKey) {
        final String HMAC_ALGO = "HmacSHA256";

        try {
            hmac = Mac.getInstance(HMAC_ALGO);
            hmac.init(new SecretKeySpec(secretKey, HMAC_ALGO));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException("failed to initialize HMAC: " + e.getMessage(), e);
        }
    }

    /**
     * Parses a token, validates it and returns the mapped user authentication object.
     *
     * @param token the token to parse, starting with "{@code Bearer }"
     * @return the parsed {@link UserAuthDto} object, or {@code null} if the token was invalid
     */
    public UserAuthDto parseUserFromToken(String token) {
        token = token.substring("Bearer ".length());
        final String[] parts = token.split(SEPARATOR_SPLITTER);

        if (parts.length == 3 && parts[0].length() > 0 && parts[1].length() > 0 && parts[2].length() > 0) {
            try {
                final String firstPart = parts[0] + SEPARATOR + parts[1];
                final byte[] userBytes = fromBase64(parts[1]);
                final byte[] hash = fromBase64(parts[2]);

                boolean validHash = Arrays.equals(createHmac(firstPart.getBytes()), hash);
                if (validHash) {
                    final UserAuthDto user = fromJSON(userBytes);
                    if (LocalDateTime.now().isBefore(LocalDateTime.ofEpochSecond(user.getExp(), 0, ZoneOffset.UTC))) {
                        return user;
                    }
                }
            } catch (IllegalArgumentException e) {
                log.warn("Couldn't parse token! " + token, e);
            }
        }

        return null;
    }

    /**
     * Creates a new token for a given user authentication object.
     *
     * @param user the user authentication object to create the token for
     * @return the newly created token (missing the "{@code Bearer }" at the start)
     */
    public String createTokenForUser(UserAuthDto user) {
        final String headerAndUser = HEADER_BASE64 + SEPARATOR + toBase64(toJSON(user));
        final String hash = toBase64(createHmac(headerAndUser.getBytes()));

        return headerAndUser + SEPARATOR + hash;
    }

    private static UserAuthDto fromJSON(final byte[] userBytes) {
        try {
            return MAPPER.readValue(new ByteArrayInputStream(userBytes), UserAuthDto.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static byte[] toJSON(Object object) {
        try {
            return MAPPER.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String toBase64(byte[] content) {
        return DatatypeConverter.printBase64Binary(content);
    }

    private static byte[] fromBase64(String content) {
        return DatatypeConverter.parseBase64Binary(content);
    }

    // synchronized to guard internal hmac object
    private synchronized byte[] createHmac(byte[] content) {
        return hmac.doFinal(content);
    }
}
