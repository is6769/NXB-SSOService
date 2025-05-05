package org.example.ssoservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;

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

    @Column(name = "msisdn", nullable = false, unique = true)
    private String msisdn;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private AppRole role;

    @Column(name = "reference_id")
    private Long referenceId;

}
