package hello.domain;

import hello.domain.UcToken.UcTokenFor;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-01-03T11:06:37.232+0800")
@StaticMetamodel(UcToken.class)
public class UcToken_ extends BaseEntity_ {
	public static volatile SingularAttribute<UcToken, String> tk;
	public static volatile SingularAttribute<UcToken, Long> millis;
	public static volatile SingularAttribute<UcToken, UcTokenFor> tkf;
	public static volatile SingularAttribute<UcToken, String> extra;
	public static volatile SingularAttribute<UcToken, Date> consumeAt;
	public static volatile SingularAttribute<UcToken, Integer> useTimes;
}
