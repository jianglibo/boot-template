/**
 * 2016 jianglibo@gmail.com
 *
 */
package hello.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import hello.domain.Bar;
import hello.domain.Foo;

/**
 * @author jianglibo@gmail.com
 *         2015年9月28日
 *
 */
public interface BarRepositoryCustom {
    <S extends Bar> S save(S entity);

    Page<Bar> findAll(Specification<Bar> spec, Pageable pageable);
}
