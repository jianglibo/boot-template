package hello.domain;

import hello.domain.BootUser.Gender;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-09-30T15:22:23.937+0800")
@StaticMetamodel(BootUser.class)
public class BootUser_ extends BaseEntity_ {
	public static volatile SingularAttribute<BootUser, String> displayName;
	public static volatile SingularAttribute<BootUser, String> avatar;
	public static volatile SingularAttribute<BootUser, Boolean> emailVerified;
	public static volatile SingularAttribute<BootUser, Boolean> mobileVerified;
	public static volatile SingularAttribute<BootUser, Gender> gender;
	public static volatile SetAttribute<BootUser, ThirdPartLogin> thirdConns;
	public static volatile SingularAttribute<BootUser, String> name;
	public static volatile SingularAttribute<BootUser, String> email;
	public static volatile SingularAttribute<BootUser, String> password;
	public static volatile SingularAttribute<BootUser, String> mobile;
	public static volatile SingularAttribute<BootUser, Boolean> accountNonExpired;
	public static volatile SingularAttribute<BootUser, Boolean> accountNonLocked;
	public static volatile SingularAttribute<BootUser, Boolean> credentialsNonExpired;
	public static volatile SingularAttribute<BootUser, Boolean> enabled;
	public static volatile SingularAttribute<BootUser, String> openId;
	public static volatile SetAttribute<BootUser, Role> roles;
}
