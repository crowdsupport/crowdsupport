package org.outofrange.crowdsupport;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CrowdsupportApplication.class)
@WebIntegrationTest
@Category(IntegrationTest.class)
public class CrowdsupportApplicationTests {
    @Test
	public void contextLoads() {
	}
}
