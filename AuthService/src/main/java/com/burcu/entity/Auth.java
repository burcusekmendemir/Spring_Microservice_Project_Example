package com.burcu.entity;

import com.burcu.utility.enums.ERole;
import com.burcu.utility.enums.EStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Auth extends BaseEntity  {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String username;
  private String email;
  private String password;
  private String activationCode;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  private ERole role =ERole.USER;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  private EStatus status=EStatus.PENDING;
}
