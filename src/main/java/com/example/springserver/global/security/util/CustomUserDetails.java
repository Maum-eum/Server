package com.example.springserver.global.security.util;

import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.center.entity.Admin;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    @Getter
    private final Long id;
    private final String username;
    private final String password;
    private final String role;
//    private final boolean isAccountNonLocked;
//    private final boolean isEnabled;

    public CustomUserDetails(Admin admin) {
        this.id = admin.getId();
        this.username = admin.getUsername();
        this.password = admin.getPassword();
        this.role = "ROLE_ADMIN";
//        this.isAccountNonLocked = admin.getAccountStatus() != AccountStatus.LOCKED;
//        this.isEnabled = admin.getAccountStatus() == AccountStatus.ACTIVE;
    }

    public CustomUserDetails(Caregiver caregiver) {
        this.id = caregiver.getId();
        this.username = caregiver.getUsername();
        this.password = caregiver.getPassword();
        this.role = "ROLE_CAREGIVER";
//        this.isAccountNonLocked = caregiver.getAccountStatus() != AccountStatus.LOCKED;
//        this.isEnabled = caregiver.getAccountStatus() == AccountStatus.ACTIVE;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(() -> role);
        return collection;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

//    @Override
//    public boolean isAccountNonLocked() {
//        return isAccountNonLocked;
//    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

//    @Override
//    public boolean isEnabled() {
//        return isEnabled;
//    }
}
