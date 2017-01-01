package hello.config.userdetail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

import hello.vo.BootUserVo;

public class BootUserVoBuilder {
	
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
	private final BootUserManagerConfigurer<?> nextBuilder;

	BootUserVoBuilder(BootUserManagerConfigurer<?> builder) {
		this.nextBuilder = builder;
	}

	public BootUserManagerConfigurer<?> and() {
		return nextBuilder;
	}

	public BootUserVoBuilder name(String name) {
		Assert.notNull(name, "name cannot be null");
		this.name = name;
		return this;
	}

	public BootUserVoBuilder email(String email) {
		Assert.notNull(displayName, "username cannot be null");
		this.email = email;
		return this;
	}

	public BootUserVoBuilder emailVerified(boolean b) {
		this.emailVerified = b;
		return this;
	}

	public BootUserVoBuilder mobileVerified(boolean b) {
		this.mobileVerified = b;
		return this;
	}

	public BootUserVoBuilder mobile(String mobile) {
		Assert.notNull(displayName, "username cannot be null");
		this.mobile = mobile;
		return this;
	}

	public BootUserVoBuilder displayName(String displayName) {
		Assert.notNull(displayName, "username cannot be null");
		this.displayName = displayName;
		return this;
	}

	public BootUserVoBuilder password(String password) {
		Assert.notNull(password, "password cannot be null");
		this.password = password;
		return this;
	}

//	public BootUserVoBuilder roles(String... roles) {
//		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(roles.length);
//		for (String role : roles) {
//			Assert.isTrue(!role.startsWith("ROLE_"), role + " cannot start with ROLE_ (it is automatically added)");
//			authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
//		}
//		return authorities(authorities);
//	}
//
//	public BootUserVoBuilder authorities(GrantedAuthority... authorities) {
//		return authorities(Arrays.asList(authorities));
//	}
//
	private BootUserVoBuilder authorities(List<? extends GrantedAuthority> authorities) {
		this.authorities = new ArrayList<GrantedAuthority>(authorities);
		return this;
	}

	public BootUserVoBuilder authorities(String... authorities) {
		return authorities(AuthorityUtils.createAuthorityList(authorities));
	}

	public BootUserVoBuilder accountExpired(boolean accountExpired) {
		this.accountExpired = accountExpired;
		return this;
	}

	public BootUserVoBuilder accountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
		return this;
	}

	public BootUserVoBuilder credentialsExpired(boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
		return this;
	}

	public BootUserVoBuilder disabled(boolean disabled) {
		this.disabled = disabled;
		return this;
	}

	public BootUserVo build() {
		return new BootUserVo(name, displayName, email, mobile, password, !disabled, !accountExpired,
				!credentialsExpired, !accountLocked, null, authorities, emailVerified, mobileVerified);
	}
}
