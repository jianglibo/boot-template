package hello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import hello.domain.Foo;

@RepositoryRestResource(collectionResourceRel = "foos", path = "foos")
public interface FooRepository extends JpaRepository<Foo, Long>, FooRepositoryCustom, JpaSpecificationExecutor<Foo> {
    Foo findByName(String rn);
}
