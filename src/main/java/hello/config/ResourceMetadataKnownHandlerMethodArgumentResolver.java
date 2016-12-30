/**
 * 2016 jianglibo@gmail.com
 *
 */
package hello.config;

import static org.springframework.util.StringUtils.hasText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.MethodParameter;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.mapping.ResourceMappings;
import org.springframework.data.rest.core.mapping.ResourceMetadata;
import org.springframework.data.rest.webmvc.BaseUri;
import org.springframework.data.rest.webmvc.config.ResourceMetadataHandlerMethodArgumentResolver;
import org.springframework.data.rest.webmvc.util.UriUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author jianglibo@gmail.com
 *         2015年9月28日
 *
 */
public class ResourceMetadataKnownHandlerMethodArgumentResolver extends ResourceMetadataHandlerMethodArgumentResolver{
    
    private static final Pattern ptn = Pattern.compile("/([^/]+)");

	private final Repositories repositories;
	private final ResourceMappings mappings;
	private final BaseUri baseUri;
    
    /**
     * @param repositories
     * @param mappings
     * @param baseUri
     */
    public ResourceMetadataKnownHandlerMethodArgumentResolver(Repositories repositories, ResourceMappings mappings, BaseUri baseUri) {
        super(repositories, mappings, baseUri);
		this.repositories = repositories;
		this.mappings = mappings;
		this.baseUri = baseUri;
    }
    
    /* (non-Javadoc)
     * @see org.springframework.data.rest.webmvc.config.ResourceMetadataHandlerMethodArgumentResolver#resolveArgument(org.springframework.core.MethodParameter, org.springframework.web.method.support.ModelAndViewContainer, org.springframework.web.context.request.NativeWebRequest, org.springframework.web.bind.support.WebDataBinderFactory)
     */
    @Override
    public ResourceMetadata resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        String lookupPath = baseUri.getRepositoryLookupPath(webRequest);
        
        
        
        String repositoryKey = UriUtils.findMappingVariable("repository", parameter.getMethod(), lookupPath);
        
        if (!hasText(repositoryKey)) {
            Matcher m = ptn.matcher(lookupPath);
            if (m.matches()) {
                repositoryKey = m.group(1);
            }
        }

        if (!hasText(repositoryKey)) {
            return null;
        }

        for (Class<?> domainType : repositories) {
            ResourceMetadata mapping = mappings.getMappingFor(domainType);
            if (mapping.getPath().matches(repositoryKey) && mapping.isExported()) {
                return mapping;
            }
        }

        throw new IllegalArgumentException(String.format("Could not resolve repository metadata for %s.", repositoryKey));

    }

}
