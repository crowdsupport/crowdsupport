package org.outofrange.crowdsupport.spring.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.outofrange.crowdsupport.dto.UserAuthDto;

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
import java.util.Date;

public final class TokenHandler {
    private static class Header {
        public String alg = "HS256";
        public String typ = "JWT";
    }

    private static final Header HEADER = new Header();

	private static final String HMAC_ALGO = "HmacSHA256";
	private static final String SEPARATOR = ".";
	private static final String SEPARATOR_SPLITTER = "\\.";

	private final Mac hmac;

	public TokenHandler(byte[] secretKey) {
		try {
			hmac = Mac.getInstance(HMAC_ALGO);
			hmac.init(new SecretKeySpec(secretKey, HMAC_ALGO));
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			throw new IllegalStateException("failed to initialize HMAC: " + e.getMessage(), e);
		}
	}

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
				//log tempering attempt here
			}
		}
		return null;
	}

	public String createTokenForUser(UserAuthDto user) {
        final String headerAndUser = toBase64(toJSON(HEADER)) + SEPARATOR + toBase64(toJSON(user));
		final String hash = toBase64(createHmac(headerAndUser.getBytes()));

        return headerAndUser + SEPARATOR + hash;
	}

	private UserAuthDto fromJSON(final byte[] userBytes) {
		try {
			return new ObjectMapper().readValue(new ByteArrayInputStream(userBytes), UserAuthDto.class);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private byte[] toJSON(Object object) {
		try {
			return new ObjectMapper().writeValueAsBytes(object);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException(e);
		}
	}

	private String toBase64(byte[] content) {
		return DatatypeConverter.printBase64Binary(content);
	}

	private byte[] fromBase64(String content) {
		return DatatypeConverter.parseBase64Binary(content);
	}

	// synchronized to guard internal hmac object
	private synchronized byte[] createHmac(byte[] content) {
		return hmac.doFinal(content);
	}
}
