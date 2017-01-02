package hello.domain;

import hello.domain.ThirdPartLogin.Provider;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-01-02T12:03:52.283+0800")
@StaticMetamodel(LoginAttempt.class)
public class LoginAttempt_ extends BaseEntity_ {
	public static volatile SingularAttribute<LoginAttempt, String> username;
	public static volatile SingularAttribute<LoginAttempt, String> password;
	public static volatile SingularAttribute<LoginAttempt, String> remoteAddress;
	public static volatile SingularAttribute<LoginAttempt, String> sessionId;
	public static volatile SingularAttribute<LoginAttempt, Provider> provider;
	public static volatile SingularAttribute<LoginAttempt, Boolean> success;
}
