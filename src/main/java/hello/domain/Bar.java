package hello.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "bar")
public class Bar extends BaseEntity {
    /**
     * 
     */
    private static final long serialVersionUID = -4494505129280460004L;
    private String bname;
    
    @ManyToOne
    private Foo foo;

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public Foo getFoo() {
        return foo;
    }

    public void setFoo(Foo foo) {
        this.foo = foo;
    }
    
    
}
