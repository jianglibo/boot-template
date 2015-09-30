package hello.config.userdetail;

import org.springframework.security.config.annotation.authentication.ProviderManagerBuilder;


/**
 * 
 * @author jianglibo@gmail.com
 *
 * @param <B>
 */
public class BootUserManagerConfigurer<B extends ProviderManagerBuilder<B>> extends AbstractBootUserManagerConfigurer<B, BootUserManagerConfigurer<B>> {

    public BootUserManagerConfigurer(BootUserManager userDetailsManager) {
        super(userDetailsManager);
    }
}
