/**
 * Copyright 2015 Hangzhou NetFrog Inc.
 *
 */
package hello.domain.projection;

import org.springframework.data.rest.core.config.Projection;

import hello.domain.Foo;

/**
 * @author jianglibo@gmail.com
 *         2015年9月28日
 *
 */
@Projection(name = "foo-simple", types = Foo.class)
public interface FooSimple {
    String getName();
}
