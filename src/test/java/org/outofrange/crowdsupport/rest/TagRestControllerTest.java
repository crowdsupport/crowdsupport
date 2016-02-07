
package org.outofrange.crowdsupport.rest;

import org.junit.Test;
import org.outofrange.crowdsupport.dto.TagDto;
import org.outofrange.crowdsupport.model.Tag;
import org.outofrange.crowdsupport.persistence.TagRepository;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TagRestControllerTest extends RestIntegrationTest {
    @Inject
    private TagRepository tagRepository;

    @Test
    public void getAllTagsWithNoTags() {
        final List<Tag> tags = getList(base(), Tag.class);
        assertEquals(0, tags.size());
    }

    @Test
    public void getAllTagsWithSomeTags() {
        final Tag tag1 = new Tag("tagone");
        final Tag tag2 = new Tag("tagtwo");
        tagRepository.save(tag1);
        tagRepository.save(tag2);

        final List<Tag> tags = getList(base(), Tag.class);

        assertEquals(2, tags.size());
        assertTrue(tags.contains(tag1));
        assertTrue(tags.contains(tag2));
    }

    public void createTag() {
        // TODO use exchange and build something for auth
        rest.put(base() + "/tag/newtag", null);
        List<Tag> tags = getList(base(), Tag.class);

        assertEquals(1, tags.size());
        assertTrue(tags.contains(new Tag("newtag")));

        rest.put(base() + "/tag/newtag", null);
        tags = getList(base(), Tag.class);

        assertEquals(1, tags.size());
        assertTrue(tags.contains(new Tag("newtag")));
    }

    private String base() {
        return base("/tag");
    }
}