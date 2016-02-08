package org.outofrange.crowdsupport.util;

import org.outofrange.crowdsupport.spring.security.TokenHandler;

public class TokenStore {
    private final TokenHandler tokenHandler;

    public TokenStore(byte[] secret) {
        tokenHandler = new TokenHandler(secret);
    }

    public String getTokenForUser(TestUser testUser) {
        return tokenHandler.createTokenForUser(testUser.getUserAuthDto());
    }
}
