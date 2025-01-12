package com.jeremsdev.validations.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;
    @Column(nullable = false, length = 80)
    private String name;
    @Column(nullable = false, length = 80, unique = true)
    private String email;
    @Column(nullable = false, length = 80, unique = true)
    private String phoneNumber;
    @Column(nullable = false)
    private  String password;

    //relations

    @OneToMany
    @JoinColumn(name = "loan_id")
    private Set<Loan> loans;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
