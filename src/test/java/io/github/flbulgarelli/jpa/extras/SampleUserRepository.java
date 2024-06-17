package io.github.flbulgarelli.jpa.extras;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

public class SampleUserRepository implements WithSimplePersistenceUnit {

  public static SampleUserRepository INSTANCE = new SampleUserRepository();

  public void addUser(SampleUser user) {
    persist(user);
  }

  public SampleUser find(Long id) {
    return find(SampleUser.class, id);
  }

  public SampleUser findByName(String name) {
    return createQuery("select u from SampleUser u where u.name = :name", SampleUser.class)
        .setParameter("name", name)
        .getSingleResult();
  }
}
