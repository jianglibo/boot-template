/**
 * Copyright 2015 Hangzhou NetFrog Inc.
 *
 */
package hello.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import hello.domain.BootUser;

/**
 * @author jianglibo@gmail.com
 *         2015年9月28日
 *
 */
public interface BootUserRepositoryCustom {
    <S extends BootUser> S save(S entity);

    Page<BootUser> findAll(Specification<BootUser> spec, Pageable pageable);
}
