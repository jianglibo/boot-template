package hello.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import hello.domain.BootUser;


@RepositoryRestResource(collectionResourceRel = "people", path = "people")
public interface BootUserRepository extends JpaRepository<BootUser, Long>, JpaSpecificationExecutor<BootUser> {

    BootUser findByEmail(@Param("email") String email);

    BootUser findByMobile(@Param("mobile") String mobile);

    BootUser findByName(@Param("name") String name);

    @Override
    @RestResource(exported = false)
    @Transactional
    public <S extends BootUser> S save(S entity);
    
    @Override
    @RestResource(exported = false)
    public void delete(BootUser entity);

    @Override
    public BootUser findOne(Long personId);

}
