package hello.config.userdetail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.config.annotation.authentication.ProviderManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.UserDetailsServiceConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

import hello.vo.BootUserVo;

/**
 * 
 * almost copy from org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer
 * 
 * @author jianglibo@gmail.com
 *
 * @param <B>
 * @param <C>
 */

public abstract class AbstractBootUserManagerConfigurer<B extends ProviderManagerBuilder<B>, C extends AbstractBootUserManagerConfigurer<B,C>> extends UserDetailsServiceConfigurer<B, C, BootUserManager> {
  
    private final List<InternalBootUserVoBuilder> personBuilders = new ArrayList<InternalBootUserVoBuilder>();
    
    
    private Logger logger = LoggerFactory.getLogger(AbstractBootUserManagerConfigurer.class);

    protected AbstractBootUserManagerConfigurer(BootUserManager userDetailsManager) {
        super(userDetailsManager);
    }



    @Override
    protected void initUserDetailsService() throws Exception {
        for(InternalBootUserVoBuilder userBuilder : personBuilders) {
            try {
                getUserDetailsService().createUser(userBuilder.build());
            } catch (DataIntegrityViolationException e) {
                logger.info(e.getMessage());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public final InternalBootUserVoBuilder withPerson(String name) {
        InternalBootUserVoBuilder userBuilder = new InternalBootUserVoBuilder((C)this);
        userBuilder.name(name);
        this.personBuilders.add(userBuilder);
        return userBuilder;
    }

    public class InternalBootUserVoBuilder {
        
        private String name;
        private String displayName;
        
        private String email;
        private String mobile;
      
        private boolean emailVerified;
        private boolean mobileVerified;
        
        private String password;
        private List<GrantedAuthority> authorities = Lists.newArrayList();
        private boolean accountExpired;
        private boolean accountLocked;
        private boolean credentialsExpired;
        private boolean disabled;
        private final C builder;

        private InternalBootUserVoBuilder(C builder) {
            this.builder = builder;
        }

        public C and() {
            return builder;
        }

        private InternalBootUserVoBuilder name(String name) {
            Assert.notNull(name, "name cannot be null");
            this.name = name;
            return this;
        }
        
        public InternalBootUserVoBuilder email(String email) {
            Assert.notNull(displayName, "username cannot be null");
            this.email = email;
            return this;
        }
        
        public InternalBootUserVoBuilder emailVerified(boolean b) {
            this.emailVerified = b;
            return this;
        }

        public InternalBootUserVoBuilder mobileVerified(boolean b) {
            this.mobileVerified = b;
            return this;
        }

        public InternalBootUserVoBuilder mobile(String mobile) {
            Assert.notNull(displayName, "username cannot be null");
            this.mobile = mobile;
            return this;
        }
        public InternalBootUserVoBuilder displayName(String displayName) {
            Assert.notNull(displayName, "username cannot be null");
            this.displayName = displayName;
            return this;
        }

        public InternalBootUserVoBuilder password(String password) {
            Assert.notNull(password, "password cannot be null");
            this.password = password;
            return this;
        }

        public InternalBootUserVoBuilder roles(String... roles) {
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(roles.length);
            for(String role : roles) {
                Assert.isTrue(!role.startsWith("ROLE_"), role + " cannot start with ROLE_ (it is automatically added)");
                authorities.add(new SimpleGrantedAuthority("ROLE_"+role));
            }
            return authorities(authorities);
        }

        public InternalBootUserVoBuilder authorities(GrantedAuthority...authorities) {
            return authorities(Arrays.asList(authorities));
        }

        public InternalBootUserVoBuilder authorities(List<? extends GrantedAuthority> authorities) {
            this.authorities = new ArrayList<GrantedAuthority>(authorities);
            return this;
        }

        public InternalBootUserVoBuilder authorities(String... authorities) {
            return authorities(AuthorityUtils.createAuthorityList(authorities));
        }

        public InternalBootUserVoBuilder accountExpired(boolean accountExpired) {
            this.accountExpired = accountExpired;
            return this;
        }

        public InternalBootUserVoBuilder accountLocked(boolean accountLocked) {
            this.accountLocked = accountLocked;
            return this;
        }

        public InternalBootUserVoBuilder credentialsExpired(boolean credentialsExpired) {
            this.credentialsExpired = credentialsExpired;
            return this;
        }


       public InternalBootUserVoBuilder disabled(boolean disabled) {
            this.disabled = disabled;
            return this;
        }

        private BootUserVo build() {
            return new BootUserVo(name, displayName,email, mobile, password, !disabled, !accountExpired,
                    !credentialsExpired, !accountLocked,null, authorities, emailVerified, mobileVerified);
        }
    }

}
