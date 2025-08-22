package org.example.pms_uc_j2ee.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password") // Excludes the password field from the toString() method for security
@Entity
@Table(name = "Admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Integer adminId;

    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "balance", precision = 10, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;
}
