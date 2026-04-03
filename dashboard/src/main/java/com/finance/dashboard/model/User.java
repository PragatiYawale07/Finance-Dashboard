package com.finance.dashboard.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import java.util.HashSet;

@Entity                     // User entity for storing user info and roles
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // user id, auto generated

    private String username;        // username for login
    private String password;       // password (will be encoded)
    private boolean active = true; //active by default

    @ManyToMany(fetch = FetchType.EAGER)   // many-to-many relationship with Role
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","role_id"})
    )
    private Set<Role> roles = new HashSet<>();    // roles assigned to user
}
// NOTE: I have used this Query to Added UNIQUE constraint on username in DB to avoid duplicate users using pgAdmin database
// ALTER TABLE users ADD CONSTRAINT unique_username UNIQUE (username);