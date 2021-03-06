package hello.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    
    @Version
    private int version;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    private boolean archived = false;
    
    @PrePersist
    public void createCreatedAt() {
    	setCreatedAt(Date.from(Instant.now()));
    }
    
	@PreUpdate
	public void preUpdate() {
		if (this instanceof HasUpdatedAt) {
			((HasUpdatedAt)this).setUpdatedAt(Date.from(Instant.now()));	
		}
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
