package hello.config;

import javax.annotation.Priority;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "myapp")
@Priority(1)
public class ApplicationConfig {

	public String getOutSideBaseUrl() {
		return null;
	}

}
