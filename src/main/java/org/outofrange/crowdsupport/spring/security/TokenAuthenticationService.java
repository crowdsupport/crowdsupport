package org.outofrange.crowdsupport.spring.security;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.dto.UserAuthDto;
import org.outofrange.crowdsupport.service.AuthorityService;
import org.outofrange.crowdsupport.service.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenAuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(TokenAuthenticationService.class);

	private static final String AUTH_HEADER_NAME = "authorization";

    private final ModelMapper mapper;
    private final AuthorityService authorityService;

    private ConfigurationService configurationService;
    private TokenHandler tokenHandler;
    private String secretCache = null;

	@Inject
	public TokenAuthenticationService(ConfigurationService configurationService, ModelMapper mapper,
                                      AuthorityService authorityService) {
        this.configurationService = configurationService;

        this.mapper = mapper;
        this.authorityService = authorityService;
	}

    private TokenHandler getTokenHandler() {
        if (tokenHandler == null) {
            log.info("Creating new token handler");

            secretCache = configurationService.getProperty(ConfigurationService.HMAC_TOKEN_SECRET);
            tokenHandler = new TokenHandler(DatatypeConverter.parseBase64Binary(secretCache));
        } else {
            final String secretFromConfiguration = configurationService.getProperty(ConfigurationService.HMAC_TOKEN_SECRET);

            if (!secretCache.equals(secretFromConfiguration)) {
                log.info("Token change detected, creating new token handler");

                secretCache = secretFromConfiguration;
                tokenHandler = new TokenHandler(DatatypeConverter.parseBase64Binary(secretCache));
            }
        }

        return tokenHandler;
    }

	public void addAuthenticationToResponse(HttpServletResponse response, UserAuthentication authentication) {
		final UserAuthDto user = mapper.map(authentication.getDetails(), UserAuthDto.class);
		user.setExp(LocalDateTime.now().plusDays(7).toEpochSecond(ZoneOffset.UTC));
		response.addHeader(AUTH_HEADER_NAME, getTokenHandler().createTokenForUser(user));
	}

	public Authentication getAuthentication(HttpServletRequest request) {
		final String token = request.getHeader(AUTH_HEADER_NAME);
		if (token != null) {
			final UserAuthDto user = getTokenHandler().parseUserFromToken(token);
			if (user != null) {
                try {
                    return new UserAuthentication(user, authorityService.mapRolesToAuthorities(user.getRoles()));
                } catch (UsernameNotFoundException e) {
                    log.info("Couldn't authenticate unknown user: {}", user.getUsername());
                    return null;
                }
			}
		}
		return null;
	}
}
