package org.outofrange.crowdsupport.spring.security.jwt;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.outofrange.crowdsupport.dto.UserAuthDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

/**
 * Authentication object to store a user authentication object and resolved authorities.
 */
public class UserAuthentication implements Authentication {
	private final UserAuthDto userAuthDto;
    private final Set<GrantedAuthority> resolvedAuthorities;

	private boolean authenticated = true;

	public UserAuthentication(UserAuthDto userAuthDto, Set<GrantedAuthority> resolvedAuthorities) {
		this.userAuthDto = userAuthDto;
        this.resolvedAuthorities = resolvedAuthorities;
	}

	@Override
	public String getName() {
		return userAuthDto.getUsername();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return resolvedAuthorities;
	}

	@Override
	public Object getCredentials() {
		return userAuthDto.getPassword();
	}

	@Override
	public UserAuthDto getDetails() {
		return userAuthDto;
	}

	@Override
	public Object getPrincipal() {
		return userAuthDto.getUsername();
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("userAuthDto", userAuthDto)
                .append("resolvedAuthorities", resolvedAuthorities)
                .append("authenticated", authenticated)
                .toString();
    }
}
