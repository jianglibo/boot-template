/**
 * Copyright 2015 Hangzhou NetFrog Inc.
 *
 */
package hello.config;

import java.util.List;

import org.springframework.boot.autoconfigure.data.rest.SpringBootRepositoryRestMvcConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.ResourceMetadataHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import com.google.common.collect.Lists;

/**
 * @author jianglibo@gmail.com
 *         2015年9月28日
 *
 */
@Configuration
public class SpringBootRepositoryRestMvcConfigurationMine extends SpringBootRepositoryRestMvcConfiguration {

    @Bean
    public RootResourceInformationKnownHandlerMethodArgumentResolver repoKnownRequestArgumentResolver() {
        return new RootResourceInformationKnownHandlerMethodArgumentResolver(repositories(), repositoryInvokerFactory(),
                resourceMetadataKnownHandlerMethodArgumentResolver());
    }
    
    @Bean
    public ResourceMetadataKnownHandlerMethodArgumentResolver resourceMetadataKnownHandlerMethodArgumentResolver() {
        return new ResourceMetadataKnownHandlerMethodArgumentResolver(repositories(), resourceMappings(), baseUri());
    }


    // /* (non-Javadoc)
    // * @see org.springframework.data.web.config.HateoasAwareSpringDataWebConfiguration#addArgumentResolvers(java.util.List)
    // */
    // @Override
    // public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    // super.addArgumentResolvers(argumentResolvers);
    // argumentResolvers.add(repoKnownRequestArgumentResolver());
    // }

    /*
     * (non-Javadoc)
     * 深入配置，这里可能产生影响。
     * 
     * @see org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration#defaultMethodArgumentResolvers()
     */
    @Override
    protected List<HandlerMethodArgumentResolver> defaultMethodArgumentResolvers() {
        List<HandlerMethodArgumentResolver> hmars = Lists.newArrayList();
        hmars.add(repoKnownRequestArgumentResolver());
        hmars.addAll(super.defaultMethodArgumentResolvers());
        return hmars;
    }
}
