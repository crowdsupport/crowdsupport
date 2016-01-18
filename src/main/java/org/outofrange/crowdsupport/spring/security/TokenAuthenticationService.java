package org.outofrange.crowdsupport.spring.security;

import org.outofrange.crowdsupport.dto.UserAuthDto;
import org.outofrange.crowdsupport.service.UserService;
import org.outofrange.crowdsupport.util.CsModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

@Service
public class TokenAuthenticationService {

	private static final String AUTH_HEADER_NAME = "authorization";
	private static final long TEN_DAYS = 1000 * 60 * 60 * 24 * 10;

	private final TokenHandler tokenHandler;

    @Inject
    private CsModelMapper mapper;

    @Inject
    private UserService userService;

	@Inject
	public TokenAuthenticationService(@Value("${crowdsupport.token.secret}") String secret) {
		tokenHandler = new TokenHandler(DatatypeConverter.parseBase64Binary(secret));
	}

	public void addAuthentication(HttpServletResponse response, UserAuthentication authentication) {
		final UserAuthDto user = mapper.map(authentication.getDetails(), UserAuthDto.class);
		user.setExp(System.currentTimeMillis() + TEN_DAYS);
		response.addHeader(AUTH_HEADER_NAME, tokenHandler.createTokenForUser(user));
	}

	public Authentication getAuthentication(HttpServletRequest request) {
		final String token = request.getHeader(AUTH_HEADER_NAME);
		if (token != null) {
			final UserAuthDto user = tokenHandler.parseUserFromToken(token);
			if (user != null) {
				return new UserAuthentication(userService.loadUserByUsername(user.getUsername()));
			}
		}
		return null;
	}
}
