/**
 * Copyright 2015 Hangzhou NetFrog Inc.
 *
 */
package hello.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.rest.webmvc.ControllerUtils;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

/**
 * @author jianglibo@gmail.com
 *         2015年8月19日
 *
 */
public class ShRestControllerBase {
    
    protected final RepositoryEntityLinks entityLinks;

    private final PagedResourcesAssembler<Object> pagedResourcesAssembler;
    
    public ShRestControllerBase(RepositoryEntityLinks entityLinks, PagedResourcesAssembler<Object> assembler) {
        this.entityLinks = entityLinks;
        this.pagedResourcesAssembler = assembler;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Resources<?> resultToResources(Object result, PersistentEntityResourceAssembler assembler, Link baseLink) {

        if (result instanceof Page) {
            Page<Object> page = (Page<Object>) result;
            return entitiesToResources(page, assembler, baseLink);
        } else if (result instanceof Iterable) {
            return entitiesToResources((Iterable<Object>) result, assembler);
        } else if (null == result) {
            return new Resources(ControllerUtils.EMPTY_RESOURCE_LIST);
        } else {
            Resource<Object> resource = assembler.toFullResource(result);
            return new Resources(Collections.singletonList(resource));
        }
    }

    protected Resources<? extends Resource<Object>> entitiesToResources(Page<Object> page, PersistentEntityResourceAssembler assembler, Link baseLink) {
        return baseLink == null ? pagedResourcesAssembler.toResource(page, assembler) : pagedResourcesAssembler.toResource(page, assembler, baseLink);
    }

    protected Resources<Resource<Object>> entitiesToResources(Iterable<Object> entities, PersistentEntityResourceAssembler assembler) {

        List<Resource<Object>> resources = new ArrayList<Resource<Object>>();

        for (Object obj : entities) {
            resources.add(obj == null ? null : assembler.toResource(obj));
        }

        return new Resources<Resource<Object>>(resources);
    }
}
