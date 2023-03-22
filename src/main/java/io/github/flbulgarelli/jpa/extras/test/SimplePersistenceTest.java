package io.github.flbulgarelli.jpa.extras.test;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

/**
 * The simplest mixin you need to include in your test case in order
 * to make in transactional and bound to a persistence context.
 *
 * @author flbulgarelli
 */
public interface SimplePersistenceTest extends PersistenceTest, WithSimplePersistenceUnit {

}
