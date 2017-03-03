/**
 * 2016 jianglibo@gmail.com
 *
 */
package hello.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.ResourceMetadataHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

/**
 * @author jianglibo@gmail.com
 *         2015年9月28日
 *
 */
@Configuration
public class SpringBootRepositoryRestMvcConfigurationMine /* extends SpringBootRepositoryRestMvcConfiguration */ {

//    @Bean
//    public RootResourceInformationKnownHandlerMethodArgumentResolver repoKnownRequestArgumentResolver() {
//        return new RootResourceInformationKnownHandlerMethodArgumentResolver(repositories(), repositoryInvokerFactory(),
//                resourceMetadataKnownHandlerMethodArgumentResolver());
//    }
//    
//    @Bean
//    public ResourceMetadataKnownHandlerMethodArgumentResolver resourceMetadataKnownHandlerMethodArgumentResolver() {
//        return new ResourceMetadataKnownHandlerMethodArgumentResolver(repositories(), resourceMappings(), baseUri());
//    }


    /*
     * (non-Javadoc)
     * 深入配置，这里可能产生影响。
     * 
     * @see org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration#defaultMethodArgumentResolvers()
     */
//    @Override
//    protected List<HandlerMethodArgumentResolver> defaultMethodArgumentResolvers() {
//        List<HandlerMethodArgumentResolver> hmars = Lists.newArrayList();
//        hmars.add(repoKnownRequestArgumentResolver());
//        hmars.addAll(super.defaultMethodArgumentResolvers());
//        return hmars;
//    }
}
