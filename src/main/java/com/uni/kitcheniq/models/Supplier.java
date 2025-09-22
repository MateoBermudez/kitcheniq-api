package com.uni.kitcheniq.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "supplier")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Supplier implements AppUserDetails{
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "contact_info", nullable = false)
    private String contactInfo;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("SUPPLIER"));
    }

    @Override
    public String getUsername() {
        return id;
    }

}