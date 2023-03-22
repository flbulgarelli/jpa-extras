package com.github.flbulgarelli.jpa.extras;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
public class User {
  @Id
  private Long id;

  public Long getId() {
    return id;
  }
}
