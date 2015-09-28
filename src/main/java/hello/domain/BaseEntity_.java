package hello.domain;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-09-28T12:25:41.721+0800")
@StaticMetamodel(BaseEntity.class)
public class BaseEntity_ {
	public static volatile SingularAttribute<BaseEntity, Long> id;
	public static volatile SingularAttribute<BaseEntity, Integer> version;
	public static volatile SingularAttribute<BaseEntity, Date> createdAt;
	public static volatile SingularAttribute<BaseEntity, Boolean> archived;
}
