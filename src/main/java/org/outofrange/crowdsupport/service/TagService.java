package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.Tag;

import java.util.List;

public interface TagService {
    /**
     * Creates a new tag. If there is already a tag with the same name, the method will return this one.
     *
     * @param name the name of the tag
     * @return the tag with the passed name
     */
    Tag createTag(String name);

    /**
     * Loads all persisted tags.
     *
     * @return all persisted tags
     */
    List<Tag> getAllTags();

    /**
     * Loads all tags that have {@code query} in their name.
     *
     * @param query the text to look for
     * @return all matching tags
     */
    List<Tag> searchForTagLike(String query);

    /**
     * Deletes a tag if it's persisted, otherwise does nothing.
     *
     * @param name the name of the tag
     */
    void deleteTag(String name);
}
