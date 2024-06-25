package com.example.kahoot.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    private String password;

    @Column(name = "avatarUrl")
    private String avatarUrl;

    private String email;

    @OneToMany(mappedBy = "user")
    private List<QuizResult> quizResults;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // return the authorities that should be granted to the user.
        // For simplicity, let's not grant any authorities to this user.
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        // return true if the account is still active
        // For simplicity, let's return true
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // return true if the user is not locked
        // For simplicity, let's return true
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // return true if the user's credentials are still valid
        // For simplicity, let's return true
        return true;
    }

    @Override
    public boolean isEnabled() {
        // return true if the user is enabled
        // For simplicity, let's return true
        return true;
    }
}