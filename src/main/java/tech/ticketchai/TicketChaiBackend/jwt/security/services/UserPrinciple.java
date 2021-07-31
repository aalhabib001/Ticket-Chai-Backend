package tech.ticketchai.TicketChaiBackend.jwt.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tech.ticketchai.TicketChaiBackend.jwt.model.Role;
import tech.ticketchai.TicketChaiBackend.jwt.model.UserModel;

import java.util.*;
import java.util.stream.Collectors;

public class UserPrinciple implements UserDetails {
    private static final long serialVersionUID = 1L;

    private final String id;

    private final String name;

    private final String username;

    private final String email;
    private Set<Role> roles = new HashSet<>();

    public UserPrinciple(String id, String name, Set<Role> roles,
                         String username, String email, String password,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.authorities = authorities;
    }

    @JsonIgnore
    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    public static UserPrinciple build(UserModel user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())
        ).collect(Collectors.toList());


        return new UserPrinciple(
                user.getId(),
                user.getProfileModel().getName(),
                user.getRoles(),
                user.getUsername(),
                user.getProfileModel().getEmail(),
                user.getPassword(),
                authorities
        );
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPrinciple user = (UserPrinciple) o;
        return Objects.equals(id, user.id);
    }
}
