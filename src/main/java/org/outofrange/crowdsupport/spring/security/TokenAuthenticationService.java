package org.outofrange.crowdsupport.spring.security;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.dto.UserAuthDto;
import org.outofrange.crowdsupport.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenAuthenticationService {

	private static final String AUTH_HEADER_NAME = "authorization";

	private final TokenHandler tokenHandler;
    private final ModelMapper mapper;
    private final UserService userService;

	@Inject
	public TokenAuthenticationService(@Value("${crowdsupport.token.secret}") String secret, ModelMapper mapper,
                                      UserService userService) {
        this.mapper = mapper;
        this.userService = userService;
		tokenHandler = new TokenHandler(DatatypeConverter.parseBase64Binary(secret));
	}

	public void addAuthentication(HttpServletResponse response, UserAuthentication authentication) {
		final UserAuthDto user = mapper.map(authentication.getDetails(), UserAuthDto.class);
		user.setExp(LocalDateTime.now().plusDays(7).toEpochSecond(ZoneOffset.UTC));
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
