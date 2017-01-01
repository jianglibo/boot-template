/**
 * 2016 jianglibo@gmail.com
 *
 */
package hello.config;


import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author jianglibo@gmail.com
 *         2015年9月28日
 *
 */
@Configuration
public class WebMvcConfigMine extends WebMvcConfigurerAdapter{

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
        .addResourceLocations("classpath:/static/");
//        .setCacheControl(CacheControl.maxAge(1000, TimeUnit.DAYS).cachePublic());
	}
}
