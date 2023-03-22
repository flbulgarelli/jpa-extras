package io.github.flbulgarelli.jpa.extras;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
public class SampleUser {
  @Id
  @GeneratedValue
  private Long id;

  public Long getId() {
    return id;
  }
}
