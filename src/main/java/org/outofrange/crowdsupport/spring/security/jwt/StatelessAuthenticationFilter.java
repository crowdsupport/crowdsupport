package org.outofrange.crowdsupport.spring.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * This filter will try to read a JSON Web Token from the request and set the authentication in the SecurityContext
 * accordingly.
 * <p/>
 * Has to be after {@link StatelessLoginFilter}
 */
public class StatelessAuthenticationFilter extends GenericFilterBean {
    private final RequestTokenService requestTokenService;

    public StatelessAuthenticationFilter(RequestTokenService requestTokenService) {
        this.requestTokenService = requestTokenService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
            ServletException {
        final Authentication auth = requestTokenService.getFromRequest((HttpServletRequest) req);
        SecurityContextHolder.getContext().setAuthentication(auth);

        chain.doFilter(req, res);
    }
}