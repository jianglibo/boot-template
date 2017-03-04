package hello.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import hello.controller.HasMenuItemController;
import hello.util.SecurityUtil;

@Component
public class MenuItems implements ApplicationContextAware {
	
	private List<MenuItem> menus = new ArrayList<>();
	
	private ApplicationContext applicationContext;
	
	@PostConstruct
	public void post() {
		Map<String, HasMenuItemController> hasMenuItemObjects =  applicationContext.getBeansOfType(HasMenuItemController.class);
		for(HasMenuItemController hmic: hasMenuItemObjects.values()) {
			if (hmic.getMenuItems() != null) {
				menus.addAll(hmic.getMenuItems());
			}
		}
	}
	
	public List<MenuItem> CloneMenuItems(String activeName) {
		boolean hasLogin = SecurityUtil.getLoginBootUserVo().isPresent();
		return menus.stream().map(MenuItem::clone).filter(mi -> {
			switch (mi.getShowWhen()) {
			case ALWAYS:
				return true;
			case NOT_LOGIN:
				return !hasLogin;
			case LOGINED:
				return hasLogin;
			case HAS_ROLE:
				if (!hasLogin) {
					return false;
				}
				return mi.containsRole(SecurityUtil.getLoginAuthentication().getAuthorities());
			default:
				return false;
			}
			}).map(mi -> {
			if (mi.getId().equals(activeName)) {
				mi.addExtraClass("pure-menu-selected");
			}
			return mi;
		}).collect(Collectors.toList());
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
