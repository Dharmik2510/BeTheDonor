package com.beTheDonor.entity;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;


//Stores the user_information which is later encapsulated into Authentication Object
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class ApplicationUser implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone_number;
    // private String type_of_user;
    private String password;

   // @Enumerated(EnumType.STRING)
    private String applicationUserRole;
    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    private Boolean locked = false;
    private Boolean enabled = false;

    public ApplicationUser(String firstname, String lastname, String email, String phone_number/*, String type_of_user*/, String password,
                           String applicationUserRole
    ) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone_number = phone_number;
        // this.type_of_user = type_of_user;
        this.password = password;
        this.applicationUserRole = applicationUserRole;
    }

    public String getApplicationUserRole() {
        return applicationUserRole;
    }

    public void setApplicationUserRole(String applicationUserRole) {
        this.applicationUserRole = applicationUserRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(applicationUserRole);
        return Collections.singleton(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
