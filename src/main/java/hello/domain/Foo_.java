package hello.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-12-31T09:20:59.652+0800")
@StaticMetamodel(Foo.class)
public class Foo_ extends BaseEntity_ {
	public static volatile ListAttribute<Foo, Bar> bars;
	public static volatile SingularAttribute<Foo, String> name;
}
