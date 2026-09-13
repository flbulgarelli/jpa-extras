package io.github.flbulgarelli.jpa.extras;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
