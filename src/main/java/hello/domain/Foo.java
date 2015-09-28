package hello.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "foo")
public class Foo extends BaseEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public Foo(){}
    
    public Foo(String name) {
        setName(name);
    }
    
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
