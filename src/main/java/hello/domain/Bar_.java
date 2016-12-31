package hello.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-10-04T13:28:32.700+0800")
@StaticMetamodel(Bar.class)
public class Bar_ extends BaseEntity_ {
	public static volatile SingularAttribute<Bar, String> bname;
	public static volatile SingularAttribute<Bar, Foo> foo;
}
