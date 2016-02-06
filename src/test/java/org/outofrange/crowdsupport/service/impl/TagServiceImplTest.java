package org.outofrange.crowdsupport.service.impl;

import org.junit.Test;
import org.outofrange.crowdsupport.persistence.TagRepository;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

public class TagServiceImplTest {
    private TagRepository tagRepository;

    private TagServiceImpl tagService;

    public void prepare() {
        tagRepository = mock(TagRepository.class);

        tagService = new TagServiceImpl(tagRepository);
    }

    @Test
    public void creatingTagThrowsExceptionWhenNameNull() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void creatingTagThrowsExceptionWhenNameEmpty() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void creatingTagWorks() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void creatingTagThrowsExceptionWhenAlreadyThere() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void deleteThrowsExceptionWhenNameIsNull() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void deleteThrowsExceptionWhenNameIsEmpty() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void deleteNonExistingTag() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void deleteExistingTag() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void allTagsReturningsEmptyResult() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void allTagsReturningMultipleResults() {
        throw new AssertionError("Not yet implemented");
    }

    @Test(expected = NullPointerException.class)
    public void searchThrowsExceptionWhenNullPassed() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void searchReturnsNoResults() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void searchReturnsMultipleResults() {
        throw new AssertionError("Not yet implemented");
    }
}