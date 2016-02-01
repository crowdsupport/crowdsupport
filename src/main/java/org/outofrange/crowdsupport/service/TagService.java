package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.Tag;

import java.util.List;

public interface TagService {
    Tag createTag(String name);

    List<Tag> getAllTags();

    List<Tag> searchForTagLike(String query);

    void deleteTag(String name);
}
