package hello.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.rest.core.annotation.RestResource;

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
    
    @OneToMany(mappedBy = "foo")
    @RestResource(exported = false)
    private List<Bar> bars;
    
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Bar> getBars() {
        return bars;
    }

    public void setBars(List<Bar> bars) {
        this.bars = bars;
    }
}
