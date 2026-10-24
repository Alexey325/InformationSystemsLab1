package com.example.informationsystemslab1.entity;

import com.example.informationsystemslab1.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank
    @Column(nullable = false)
    private String hashPassword;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

}
