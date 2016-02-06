package org.outofrange.crowdsupport.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.outofrange.crowdsupport.model.Tag;
import org.outofrange.crowdsupport.persistence.TagRepository;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class TagServiceImplTest {
    private TagRepository tagRepository;

    private TagServiceImpl tagService;

    private final Tag tag = new Tag("tag");

    @Before
    public void prepare() {
        tagRepository = mock(TagRepository.class);

        tagService = new TagServiceImpl(tagRepository);
    }

    @Test(expected = NullPointerException.class)
    public void creatingTagThrowsExceptionWhenNameNull() {
        tagService.createTag(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void creatingTagThrowsExceptionWhenNameEmpty() {
        tagService.createTag("");
    }

    @Test
    public void creatingTagWorks() {
        when(tagRepository.findOneByName(tag.getName())).thenReturn(Optional.empty());

        tagService.createTag(tag.getName());

        verify(tagRepository).save(tag);
    }

    @Test
    public void creatingTagAgainDoesNothing() {
        when(tagRepository.findOneByName(tag.getName())).thenReturn(Optional.of(tag));

        tagService.createTag(tag.getName());

        verify(tagRepository, never()).save(tag);
    }

    @Test(expected = NullPointerException.class)
    public void deleteThrowsExceptionWhenNameIsNull() {
        tagService.deleteTag(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteThrowsExceptionWhenNameIsEmpty() {
        tagService.deleteTag("");
    }

    @Test
    public void deleteNonExistingTagDoesNothing() {
        when(tagRepository.findOneByName(tag.getName())).thenReturn(Optional.empty());

        tagService.deleteTag(tag.getName());

        verify(tagRepository, never()).delete(tag);
    }

    @Test
    public void deleteExistingTag() {
        when(tagRepository.findOneByName(tag.getName())).thenReturn(Optional.of(tag));

        tagService.deleteTag(tag.getName());

        verify(tagRepository).delete(tag);
    }

    @Test
    public void allTagsReturningEmptyResult() {
        when(tagRepository.findAll()).thenReturn(Collections.emptyList());

        assertTrue(tagService.getAllTags().isEmpty());
    }

    @Test
    public void allTagsReturningMultipleResults() {
        when(tagRepository.findAll()).thenReturn(Collections.singletonList(tag));

        assertThat(1, is(equalTo(tagService.getAllTags().size())));
    }

    @Test(expected = NullPointerException.class)
    public void searchThrowsExceptionWhenNullPassed() {
        tagService.searchForTagLike(null);
    }

    @Test
    public void searchReturnsNoResults() {
        when(tagRepository.findAllByNameContainingIgnoreCase(tag.getName())).thenReturn(Collections.emptyList());

        assertTrue(tagService.searchForTagLike(tag.getName()).isEmpty());
    }

    @Test
    public void searchReturnsMultipleResults() {
        when(tagRepository.findAllByNameContainingIgnoreCase(tag.getName())).thenReturn(Collections.singletonList(tag));

        assertThat(1, is(equalTo(tagService.searchForTagLike(tag.getName()).size())));
    }
}