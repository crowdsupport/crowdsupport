package org.outofrange.crowdsupport.spring.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.dto.UserAuthDto;
import org.outofrange.crowdsupport.service.AuthorityService;
import org.outofrange.crowdsupport.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This filter is used for authenticating requests. If the request has been authenticated successfully,
 * it will add a JSON Web Token to the response and set the authentication in the SecurityContext.
 * <p/>
 * Has to be before {@link StatelessAuthenticationFilter}.
 */
public class StatelessLoginFilter extends AbstractAuthenticationProcessingFilter {
    private final RequestTokenService requestTokenService;
    private final UserService userDetailsService;
    private final AuthorityService authorityService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ModelMapper modelMapper;

    public StatelessLoginFilter(String urlMapping, RequestTokenService requestTokenService,
                                UserService userDetailsService, AuthenticationManager authManager,
                                AuthorityService authorityService, ModelMapper modelMapper) {
        super(new AntPathRequestMatcher(urlMapping));
        this.userDetailsService = userDetailsService;
        this.requestTokenService = requestTokenService;
        this.authorityService = authorityService;
        this.modelMapper = modelMapper;

        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        final UserAuthDto user = objectMapper.readValue(request.getInputStream(), UserAuthDto.class);
        final UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword());

        return getAuthenticationManager().authenticate(loginToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) throws IOException, ServletException {
        final UserAuthDto userAuthDto = modelMapper.map(userDetailsService.loadUserByUsername(authentication.getName()), UserAuthDto.class);
        final UserAuthentication userAuthentication = new UserAuthentication(userAuthDto, authorityService.mapRolesToAuthorities(userAuthDto.getRoles()));

        requestTokenService.setToResponse(response, userAuthentication);

        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
    }
}