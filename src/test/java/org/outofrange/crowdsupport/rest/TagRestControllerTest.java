
package org.outofrange.crowdsupport.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.outofrange.crowdsupport.CrowdsupportApplication;
import org.outofrange.crowdsupport.model.Tag;
import org.outofrange.crowdsupport.persistence.TagRepository;
import org.outofrange.crowdsupport.util.TestUser;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
public class TagRestControllerTest extends RestIntegrationTest {
    @Inject
    private TagRepository tagRepository;

    @Test
    public void getAllTagsWithNoTags() {
        final List<Tag> tags = getList(tagBase(), Tag.class);
        assertEquals(0, tags.size());
    }

    @Test
    public void getAllTagsWithSomeTags() {
        final Tag tag1 = new Tag("tagone");
        final Tag tag2 = new Tag("tagtwo");
        tagRepository.save(tag1);
        tagRepository.save(tag2);

        final List<Tag> tags = getList(tagBase(), Tag.class);

        assertEquals(2, tags.size());
        assertTrue(tags.contains(tag1));
        assertTrue(tags.contains(tag2));

        resetDatabase();
    }

    @Test
    public void createTag() {
        createUser(TestUser.USER);

        rest.put(tagBase() + "/newtag", as(TestUser.USER));
        List<Tag> tags = getList(tagBase(), Tag.class);

        assertEquals(1, tags.size());
        assertTrue(tags.contains(new Tag("newtag")));

        rest.put(tagBase() + "/newtag", as(TestUser.USER));
        tags = getList(tagBase(), Tag.class);

        assertEquals(1, tags.size());
        assertTrue(tags.contains(new Tag("newtag")));

        resetDatabase();
    }

    private String tagBase() {
        return base("/tag");
    }
}