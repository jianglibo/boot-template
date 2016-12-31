/**
 * 2016 jianglibo@gmail.com
 *
 */
package hello.repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import hello.domain.Bar;

/**
 * @author jianglibo@gmail.com
 *         2015年9月28日
 *
 */
public class BarRepositoryImpl extends SimpleJpaRepository<Bar, Long> implements BarRepositoryCustom {

//	private final JpaEntityInformation<Bar, ?> entityInformation;
    
    @Autowired
    public BarRepositoryImpl(EntityManager entityManager) {
        super(Bar.class, entityManager);
//        this.entityInformation = JpaEntityInformationSupport.getMetadata(Bar.class, entityManager);
    }
    
    /* (non-Javadoc)
     * @see org.springframework.data.jpa.repository.support.SimpleJpaRepository#save(java.lang.Object)
     */
    @Override
    @Transactional
    public <S extends Bar> S save(S entity) {
        return super.save(entity);
    }

}
