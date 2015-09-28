/**
 * Copyright 2015 Hangzhou NetFrog Inc.
 *
 */
package hello.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import hello.domain.Foo;

/**
 * @author jianglibo@gmail.com
 *         2015年9月28日
 *
 */
public interface FooRepositoryCustom {
    <S extends Foo> S save(S entity);

    Page<Foo> findAll(Specification<Foo> spec, Pageable pageable);
}
