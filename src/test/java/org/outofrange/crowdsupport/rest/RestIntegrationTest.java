package org.outofrange.crowdsupport.rest;

import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.CrowdsupportApplication;
import org.outofrange.crowdsupport.service.ConfigurationService;
import org.outofrange.crowdsupport.service.UserService;
import org.outofrange.crowdsupport.util.TestUser;
import org.outofrange.crowdsupport.util.TokenStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;
import java.util.List;
import java.util.stream.Collectors;

@SpringApplicationConfiguration(classes = CrowdsupportApplication.class)
@WebIntegrationTest("server.port:8090")
public class RestIntegrationTest {
    protected TestRestTemplate rest;

    @Inject
    protected ModelMapper mapper;

    @Inject
    private Flyway flyway;

    @Value("${server.port}")
    private int port;

    @Inject
    private ConfigurationService configurationService;

    @Inject
    private UserService userService;

    private TokenStore tokenStore;

    @PostConstruct
    public void init() {
        tokenStore = new TokenStore(DatatypeConverter.parseBase64Binary(
                configurationService.getProperty(ConfigurationService.HMAC_TOKEN_SECRET)));
    }

    protected String base(String url) {
        return "http://localhost:" + port + "/service/v1" + url;
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> getList(String url, Class<T> returnClass) {
        final List list = rest.getForObject(url, List.class);

        return (List<T>) list.stream().map(o -> mapper.map(o, returnClass)).collect(Collectors.toList());
    }

    @Before
    public void setUp() {
        rest = new TestRestTemplate();

        flyway.clean();
        flyway.migrate();
    }

    protected HttpEntity<Void> as(TestUser user) {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokenStore.getTokenForUser(user));

        return new HttpEntity<>(headers);
    }

    protected void createUser(TestUser user) {
        userService.createUser(user.getFullUserDto());
    }
}
