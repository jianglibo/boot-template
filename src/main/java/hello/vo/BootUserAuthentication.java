package hello.vo;


import org.springframework.security.authentication.AbstractAuthenticationToken;

public class BootUserAuthentication extends AbstractAuthenticationToken {
	
	private static final long serialVersionUID = -2979276602354404187L;
	
	private BootUserVo bootUserVo;

	public BootUserAuthentication(BootUserVo bootUserVo) {
		super(bootUserVo.getAuthorities());
		this.bootUserVo = bootUserVo;
		super.setAuthenticated(true);
	}

	/**
	 * 
	 */

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public BootUserVo getPrincipal() {
		return bootUserVo;
	}
	
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		if (isAuthenticated) {
			throw new IllegalArgumentException(
					"Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
		}

		super.setAuthenticated(false);
	}
}
