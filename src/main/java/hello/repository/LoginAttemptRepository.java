package hello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import hello.domain.LoginAttempt;


@RepositoryRestResource(collectionResourceRel = "loginattempt", path = "loginattempts")
public interface LoginAttemptRepository  extends JpaRepository<LoginAttempt, Long>, LoginAttemptRepositoryCustom, JpaSpecificationExecutor<LoginAttempt> {
    
    @Override
    @RestResource(exported = false)
    <S extends LoginAttempt> S save(S entity);
}
