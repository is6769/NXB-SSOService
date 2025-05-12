package org.example.ssoservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сущность, представляющая пользователя в системе SSO.
 */
@Entity
@Table(name = "app_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * MSISDN (номер телефона) пользователя. Должен быть уникальным.
     */
    @Column(name = "msisdn", nullable = false, unique = true)
    private String msisdn;

    /**
     * Роль пользователя в системе (см. {@link AppRole}).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private AppRole role;

    /**
     * Референсный ID. Может использоваться для связи с другими системами
     * (например, ID абонента в BRT). Может быть {@code null}.
     */
    @Column(name = "reference_id")
    private Long referenceId;

}
