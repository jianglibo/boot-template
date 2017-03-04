package hello.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

public class MenuItem {
	
	private boolean active;
	
	private String id;
	
	private String extraClasses = "";
	
	private String href;
	
	private ShowMenuWhen showWhen = ShowMenuWhen.ALWAYS;
	
	private Set<String> roles = new HashSet<>();
	
	private String domId;
	
	public MenuItem clone() {
		MenuItem mi = new MenuItem(getId(), getHref(), getExtraClasses(),getShowWhen());
		mi.setRoles(getRoles());
		mi.setDomId(getDomId());
		return mi;
	}

	public MenuItem(String id, String href) {
		super();
		this.id = id;
		this.href = href;
	}

	public MenuItem(String id, String href, String extraClasses, ShowMenuWhen showWhen) {
		super();
		this.id = id;
		this.href = href;
		this.extraClasses = extraClasses;
		this.showWhen = showWhen;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getExtraClasses() {
		return extraClasses;
	}
	
	public void addExtraClass(String cn) {
		String ec = getExtraClasses();
		if (ec == null || ec.isEmpty()) {
			setExtraClasses(cn);
		} else {
			setExtraClasses(getExtraClasses() + " " + cn);
		}
	}
	
	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public void setExtraClasses(String extraClasses) {
		this.extraClasses = extraClasses;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	
	public ShowMenuWhen getShowWhen() {
		return showWhen;
	}

	public void setShowWhen(ShowMenuWhen showWhen) {
		this.showWhen = showWhen;
	}

	public static enum ShowMenuWhen {
		ALWAYS, LOGINED, NOT_LOGIN, HAS_ROLE
	}

	public String getDomId() {
		return domId;
	}

	public void setDomId(String domId) {
		this.domId = domId;
	}

	public boolean containsRole(Collection<? extends GrantedAuthority> authorities) {
		for(GrantedAuthority ga : authorities) {
			if (getRoles().contains(ga.getAuthority())) {
				return true;
			}
		}
		return false;
	}
}
