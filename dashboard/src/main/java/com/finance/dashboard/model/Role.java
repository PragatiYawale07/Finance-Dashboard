package com.finance.dashboard.model;

import jakarta.persistence.*;
import lombok.*;

@Entity        // Role entity for user roles
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;  // role name like ADMIN, ANALYST, VIEWER
}