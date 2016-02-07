package org.outofrange.crowdsupport.rest;

import org.flywaydb.core.Flyway;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.CrowdsupportApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CrowdsupportApplication.class)
@WebIntegrationTest
public class RestIntegrationTest {
    protected TestRestTemplate rest;

    @Inject
    protected ModelMapper mapper;

    @Inject
    private Flyway flyway;

    @Value("${local.server.port}")
    private int port;

    protected String base(String url) {
        return "http://localhost:" + port + "/service/v1" + url;
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> getList(String url) {
        return rest.getForObject(url, List.class);
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> getList(String url, Class<T> returnClass) {
        final List list = rest.getForObject(url, List.class);

        return (List<T>) list.stream().map(o -> mapper.map(o, returnClass)).collect(Collectors.toList());
    }

    @Before
    public void setUp() {
        rest = new TestRestTemplate();
    }

    @After
    public void cleanDatabase() {
        flyway.clean();
        flyway.migrate();
    }
}
