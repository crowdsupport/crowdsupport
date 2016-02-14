package org.outofrange.crowdsupport.spring.security;

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

class StatelessLoginFilter extends AbstractAuthenticationProcessingFilter {
	private final TokenAuthenticationService tokenAuthenticationService;
	private final UserService userDetailsService;
    private final AuthorityService authorityService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ModelMapper modelMapper;

	protected StatelessLoginFilter(String urlMapping, TokenAuthenticationService tokenAuthenticationService,
                                   UserService userDetailsService, AuthenticationManager authManager,
                                   AuthorityService authorityService, ModelMapper modelMapper) {
		super(new AntPathRequestMatcher(urlMapping));
		this.userDetailsService = userDetailsService;
		this.tokenAuthenticationService = tokenAuthenticationService;
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

		tokenAuthenticationService.addAuthenticationToResponse(response, userAuthentication);

		SecurityContextHolder.getContext().setAuthentication(userAuthentication);
	}
}