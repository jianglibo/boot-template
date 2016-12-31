/**
 * 2016 jianglibo@gmail.com
 *
 */
package hello.repository;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import hello.domain.BootUser;

/**
 * @author jianglibo@gmail.com
 *         2015年9月28日
 *
 */
public class BootUserRepositoryImpl extends SimpleJpaRepository<BootUser, Long> implements BootUserRepositoryCustom {

//	private final JpaEntityInformation<BootUser, ?> entityInformation;
    
    @Autowired
    public BootUserRepositoryImpl(EntityManager entityManager) {
        super(BootUser.class, entityManager);
//        this.entityInformation = JpaEntityInformationSupport.getMetadata(BootUser.class, entityManager);
    }
}
