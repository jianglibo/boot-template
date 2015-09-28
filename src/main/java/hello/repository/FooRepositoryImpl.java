/**
 * Copyright 2015 Hangzhou NetFrog Inc.
 *
 */
package hello.repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import hello.domain.Foo;

/**
 * @author jianglibo@gmail.com
 *         2015年9月28日
 *
 */
public class FooRepositoryImpl extends SimpleJpaRepository<Foo, Long> implements FooRepositoryCustom {

	private final JpaEntityInformation<Foo, ?> entityInformation;
    
    @Autowired
    public FooRepositoryImpl(EntityManager entityManager) {
        super(Foo.class, entityManager);
        this.entityInformation = JpaEntityInformationSupport.getMetadata(Foo.class, entityManager);
    }
    
    /* (non-Javadoc)
     * @see org.springframework.data.jpa.repository.support.SimpleJpaRepository#save(java.lang.Object)
     */
    @Override
    @Transactional
    public <S extends Foo> S save(S entity) {
        return super.save(entity);
    }

}
