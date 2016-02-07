package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.model.Tag;
import org.outofrange.crowdsupport.persistence.TagRepository;
import org.outofrange.crowdsupport.util.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TagServiceImpl implements org.outofrange.crowdsupport.service.TagService {
    private static final Logger log = LoggerFactory.getLogger(TagServiceImpl.class);

    private final TagRepository tagRepository;

    @Inject
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional(readOnly = false)
    public Tag createTag(String name) {
        log.debug("Creating tag with name {}", name);

        Validate.notNullOrEmpty(name);

        final Optional<Tag> existing = tagRepository.findOneByName(name);
        if (!existing.isPresent()) {
            return tagRepository.save(new Tag(name));
        } else {
            log.debug("Found existing tag: '{}' (won't create a new one)", existing.get());
            return existing.get();
        }
    }

    @Override
    public List<Tag> getAllTags() {
        log.debug("Querying all tags");

        return tagRepository.findAll();
    }

    @Override
    public List<Tag> searchForTagLike(String query) {
        log.debug("Searching for tag like {}", query);

        Validate.notNull(query);

        return tagRepository.findAllByNameContainingIgnoreCase(query);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteTag(String name) {
        log.debug("Delete tag with name {}", name);

        Validate.notNullOrEmpty(name);

        final Optional<Tag> existing = tagRepository.findOneByName(name);
        if (existing.isPresent()) {
            tagRepository.delete(existing.get());
        } else {
            log.debug("Found no tag with name {}, doing nothing", name);
        }
    }
}
