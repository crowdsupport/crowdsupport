package org.outofrange.crowdsupport.spring.security;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.service.AuthorityService;
import org.outofrange.crowdsupport.service.UserService;
import org.outofrange.crowdsupport.spring.Config;
import org.outofrange.crowdsupport.spring.security.jwt.StatelessAuthenticationFilter;
import org.outofrange.crowdsupport.spring.security.jwt.StatelessLoginFilter;
import org.outofrange.crowdsupport.spring.security.jwt.RequestTokenService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.inject.Inject;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserService userService;

    @Inject
    private AuthorityService authorityService;

    @Inject
    private Config config;

    @Inject
    private RequestTokenService requestTokenService;

    @Inject
    private ModelMapper mapper;

    public WebSecurityConfig() {
        super(true);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (config.isDebugEnabled()) {
            http.authorizeRequests().antMatchers("/h2-console/**").permitAll().and().
                    headers().frameOptions().disable();
        }

        http
                .headers().cacheControl().disable().and()

                .exceptionHandling().and()
                .anonymous().and()
                .servletApi().and()
                .authorizeRequests()

                //allow anonymous resource requests
                .antMatchers("/").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/r/**").permitAll()

                // allow connections to websocket
                .antMatchers("/ws/**").permitAll()

                //allow anonymous POSTs to login
                .antMatchers(HttpMethod.POST, "/service/v*/user/login").permitAll()
                .antMatchers(HttpMethod.POST, "/service/v*/user").permitAll()
                .antMatchers(HttpMethod.POST, "/service/v*/user/confirmMail").permitAll()

                //allow anonymous GETs to API
                .antMatchers(HttpMethod.GET, "/service/v*/**").permitAll()

                //all other request need to be authenticated
                .anyRequest().hasRole("USER").and()

                // custom JSON based authentication by POST of {"username":"<name>","password":"<password>"} which sets the token header upon authentication
                .addFilterBefore(new StatelessLoginFilter("/service/v1/user/login", requestTokenService, userService,
                        authenticationManager(), authorityService, mapper), UsernamePasswordAuthenticationFilter.class)

                // custom Token based authentication based on the header previously given to the client
                .addFilterBefore(new StatelessAuthenticationFilter(requestTokenService), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }
}
