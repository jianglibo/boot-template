/**
 * Copyright 2015 Hangzhou NetFrog Inc.
 *
 */
package hello.config;

import org.springframework.core.MethodParameter;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.invoke.RepositoryInvoker;
import org.springframework.data.rest.core.invoke.RepositoryInvokerFactory;
import org.springframework.data.rest.core.mapping.ResourceMetadata;
import org.springframework.data.rest.webmvc.config.ResourceMetadataHandlerMethodArgumentResolver;
import org.springframework.data.rest.webmvc.config.RootResourceInformationHandlerMethodArgumentResolver;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * copy from RootResourceInformationHandlerMethodArgumentResolver
 * @author jianglibo@gmail.com
 *         2015年9月28日
 *
 */
public class RootResourceInformationKnownHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final Repositories repositories;
    private final RepositoryInvokerFactory invokerFactory;
    private final ResourceMetadataHandlerMethodArgumentResolver resourceMetadataResolver;

    /**
     * Creates a new {@link RootResourceInformationHandlerMethodArgumentResolver} using the given {@link Repositories},
     * {@link RepositoryInvokerFactory} and {@link ResourceMetadataHandlerMethodArgumentResolver}.
     * 
     * @param repositories must not be {@literal null}.
     * @param invokerFactory must not be {@literal null}.
     * @param resourceMetadataResolver must not be {@literal null}.
     */
    public RootResourceInformationKnownHandlerMethodArgumentResolver(Repositories repositories,
            RepositoryInvokerFactory invokerFactory, ResourceMetadataHandlerMethodArgumentResolver resourceMetadataResolver) {

        Assert.notNull(repositories, "Repositories must not be null!");
        Assert.notNull(invokerFactory, "invokerFactory must not be null!");
        Assert.notNull(resourceMetadataResolver, "ResourceMetadataHandlerMethodArgumentResolver must not be null!");

        this.repositories = repositories;
        this.invokerFactory = invokerFactory;
        this.resourceMetadataResolver = resourceMetadataResolver;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#supportsParameter(org.springframework.core.MethodParameter)
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean b = RootResourceInformationKnown.class.equals(parameter.getParameterType());
        return b;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#resolveArgument(org.springframework.core.MethodParameter, org.springframework.web.method.support.ModelAndViewContainer, org.springframework.web.context.request.NativeWebRequest, org.springframework.web.bind.support.WebDataBinderFactory)
     */
    @Override
    public RootResourceInformationKnown resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        ResourceMetadata resourceMetadata = resourceMetadataResolver.resolveArgument(parameter, mavContainer, webRequest,
                binderFactory);

        Class<?> domainType = resourceMetadata.getDomainType();
        RepositoryInvoker repositoryInvoker = invokerFactory.getInvokerFor(domainType);
        PersistentEntity<?, ?> persistentEntity = repositories.getPersistentEntity(domainType);

        // TODO reject if ResourceMetadata cannot be resolved
        return new RootResourceInformationKnown(resourceMetadata, persistentEntity, repositoryInvoker);
    }
}

