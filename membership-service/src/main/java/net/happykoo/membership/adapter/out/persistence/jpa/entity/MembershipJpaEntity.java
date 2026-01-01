package net.happykoo.membership.adapter.out.persistence.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "membership")
@Data
@NoArgsConstructor
@ToString
public class MembershipJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String email;

  private String address;

  private boolean isValid;

  private boolean isCorp;

  public MembershipJpaEntity(
      Long id,
      String name,
      String email,
      String address,
      boolean isValid,
      boolean isCorp
  ) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.address = address;
    this.isValid = isValid;
    this.isCorp = isCorp;
  }

  public MembershipJpaEntity(
      String name,
      String email,
      String address,
      boolean isValid,
      boolean isCorp
  ) {
    this(null, name, email, address, isValid, isCorp);
  }
}
