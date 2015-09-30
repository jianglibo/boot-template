package hello.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import hello.config.userdetail.BootUserManager;
import hello.config.userdetail.BootUserManagerConfigurer;


/**
 * 
 * @author jianglibo@gmail.com
 *         2015年9月29日
 *
 */
@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecConfig extends WebSecurityConfigurerAdapter {

    private static Logger logger = LoggerFactory.getLogger(WebSecConfig.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${spring.data.rest.base-uri}")
    private String apiPrefix;

    
    @Autowired
    private BootUserManager bootUserManager;
    /**
     * disable default. then read father class's gethttp method. write all config your self.
     */
    public WebSecConfig() {
        super(false);
    }
    

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // @formatter:off
        BootUserManagerConfigurer<AuthenticationManagerBuilder> pc = auth.apply(new BootUserManagerConfigurer<AuthenticationManagerBuilder>(bootUserManager)).passwordEncoder(passwordEncoder);
        // @formatter:on
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.authorizeRequests()
            .antMatchers(apiPrefix + "/**").permitAll()
            .anyRequest().fullyAuthenticated()
            .and()
            .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }
    
    @Bean
    public ChangeSessionIdAuthenticationStrategy sessionAuthenticationStrategy() {
        return new ChangeSessionIdAuthenticationStrategy();
    }
}
