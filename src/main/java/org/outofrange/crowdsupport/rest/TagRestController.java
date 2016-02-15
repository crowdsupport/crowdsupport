package org.outofrange.crowdsupport.rest;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.dto.TagDto;
import org.outofrange.crowdsupport.service.TagService;
import org.outofrange.crowdsupport.spring.api.ApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping(value = "/tag")
@ApiVersion("1")
public class TagRestController extends TypedMappingController<TagDto> {
    private static final Logger log = LoggerFactory.getLogger(TagRestController.class);

    private final TagService tagService;

    @Inject
    public TagRestController(ModelMapper mapper, TagService tagService) {
        super(mapper, TagDto.class);

        this.tagService = tagService;
    }

    @RequestMapping(method = RequestMethod.GET, params = {"!query"})
    public List<TagDto> getAllTags() {
        log.info("Querying all tags");

        return mapToList(tagService.getAllTags());
    }

    @RequestMapping(method = RequestMethod.GET, params = {"query"})
    public List<TagDto> searchTags(@RequestParam String query) {
        log.info("Searching for tags like {}", query);

        return mapToList(tagService.searchForTagLike(query));
    }

    @RequestMapping(value = "/{tagName}", method = RequestMethod.PUT)
    public ResponseEntity<TagDto> createNewTag(@PathVariable String tagName) {
        log.info("Creating new tag {}", tagName);

        final TagDto createdTag = map(tagService.createTag(tagName));
        return created(createdTag);
    }

    @RequestMapping(value = "/{tagName}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteTag(@PathVariable String tagName) {
        log.info("Deleting tag with name {}");

        tagService.deleteTag(tagName);

        return ResponseEntity.ok().build();
    }
}
