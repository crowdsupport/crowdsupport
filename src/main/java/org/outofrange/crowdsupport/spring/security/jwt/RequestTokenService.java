package org.outofrange.crowdsupport.spring.security.jwt;

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

/**
 * This service is capable of adding JSON Web Tokens based on UserAuthentication objects to responses,
 * and read those Authentication objects again from requests.
 */
@Service
public class RequestTokenService {
    private static final Logger log = LoggerFactory.getLogger(RequestTokenService.class);

    private static final String AUTH_HEADER_NAME = "authorization";

    private final ModelMapper mapper;
    private final AuthorityService authorityService;

    private ConfigurationService configurationService;
    private TokenHandler tokenHandler;
    private String secretCache = null;

    @Inject
    public RequestTokenService(ConfigurationService configurationService, ModelMapper mapper,
                               AuthorityService authorityService) {
        this.configurationService = configurationService;

        this.mapper = mapper;
        this.authorityService = authorityService;
    }

    /**
     * Laziliy initializes a new tokenHandler if it hasn't been created before, or if the {@code secret} has changed.
     *
     * @return a TokenHandler
     */
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

    /**
     * Adds the token for a UserAuthentication to a response.
     *
     * @param response       the response to add the token to
     * @param authentication the authentication to use for creating the token
     */
    public void setToResponse(HttpServletResponse response, UserAuthentication authentication) {
        final UserAuthDto user = mapper.map(authentication.getDetails(), UserAuthDto.class);
        user.setExp(LocalDateTime.now().plusDays(7).toEpochSecond(ZoneOffset.UTC));

        response.addHeader(AUTH_HEADER_NAME, getTokenHandler().createTokenForUser(user));
    }

    /**
     * Reads an Authentication from a request by parsing the JSON Web Token in it.
     *
     * @param request the request to look for the JWT
     * @return the parsed Authentication
     */
    public Authentication getFromRequest(HttpServletRequest request) {
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
