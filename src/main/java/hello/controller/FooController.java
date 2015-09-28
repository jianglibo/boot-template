/**
 * Copyright 2015 Hangzhou NetFrog Inc.
 *
 */
package hello.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.core.invoke.RepositoryInvoker;
import org.springframework.data.rest.core.mapping.ResourceMetadata;
import org.springframework.data.rest.core.mapping.SearchResourceMappings;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.rest.webmvc.ResourceType;
import org.springframework.data.rest.webmvc.support.DefaultedPageable;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import hello.config.RootResourceInformationKnown;
import hello.repository.FooRepository;



/**
 * @author jianglibo@gmail.com
 *         2015年8月19日
 *
 */
@RepositoryRestController
public class FooController extends ShRestControllerBase {

    private static Logger logger = LoggerFactory.getLogger(FooController.class);

    @Autowired
    private FooRepository fooRepo;

    @Value("${spring.data.rest.base-uri}")
    private String apiPrefix;

    /**
     * @param entityLinks
     * @param assembler
     */
    @Autowired
    public FooController(RepositoryEntityLinks entityLinks, PagedResourcesAssembler<Object> assembler) {
        super(entityLinks, assembler);
    }

    @ExceptionHandler({ Exception.class })
    @ResponseBody
    public String handleExp(Exception npe) {
        npe.printStackTrace();
        return npe.getMessage();
    }

	@ResponseBody
	@RequestMapping(value = "/foos", method = RequestMethod.GET)
	public Resources<?> getCollectionResource(final RootResourceInformationKnown resourceInformation,
			DefaultedPageable pageable, Sort sort, PersistentEntityResourceAssembler assembler)
			throws ResourceNotFoundException, HttpRequestMethodNotSupportedException {

		resourceInformation.verifySupportedMethod(HttpMethod.GET, ResourceType.COLLECTION);

		RepositoryInvoker invoker = resourceInformation.getInvoker();

		if (null == invoker) {
			throw new ResourceNotFoundException();
		}

		Iterable<?> results;

		if (pageable.getPageable() != null) {
			results = invoker.invokeFindAll(pageable.getPageable());
		} else {
			results = invoker.invokeFindAll(sort);
		}

		ResourceMetadata metadata = resourceInformation.getResourceMetadata();
		SearchResourceMappings searchMappings = metadata.getSearchResourceMappings();
		List<Link> links = new ArrayList<Link>();

		if (searchMappings.isExported()) {
			links.add(entityLinks.linkFor(metadata.getDomainType()).slash(searchMappings.getPath())
					.withRel(searchMappings.getRel()));
		}

		Link baseLink = entityLinks.linkToPagedResource(resourceInformation.getDomainType(), pageable.isDefault() ? null
				: pageable.getPageable());

		Resources<?> resources = resultToResources(results, assembler, baseLink);
		resources.add(links);
		return resources;
	}

}
