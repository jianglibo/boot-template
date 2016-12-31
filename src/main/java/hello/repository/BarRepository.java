package hello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import hello.domain.Bar;

@RepositoryRestResource(collectionResourceRel = "bars", path = "bars")
public interface BarRepository extends JpaRepository<Bar, Long>, BarRepositoryCustom, JpaSpecificationExecutor<Bar> {
    Bar findByBname(String rn);
}
