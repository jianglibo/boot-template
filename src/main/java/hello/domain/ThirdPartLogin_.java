package hello.domain;

import hello.domain.ThirdPartLogin.Provider;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-09-29T10:28:21.170+0800")
@StaticMetamodel(ThirdPartLogin.class)
public class ThirdPartLogin_ extends BaseEntity_ {
	public static volatile SingularAttribute<ThirdPartLogin, Provider> provider;
	public static volatile SingularAttribute<ThirdPartLogin, String> openId;
	public static volatile SingularAttribute<ThirdPartLogin, String> displayName;
	public static volatile SingularAttribute<ThirdPartLogin, String> readableId;
	public static volatile SingularAttribute<ThirdPartLogin, BootUser> bootUser;
}
