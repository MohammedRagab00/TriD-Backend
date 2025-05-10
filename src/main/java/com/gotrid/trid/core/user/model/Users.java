package com.gotrid.trid.core.user.model;

import com.gotrid.trid.common.model.BaseEntity;
import com.gotrid.trid.common.exception.custom.user.InvalidAgeException;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"roles"})
@Entity
@Table(name = "users")
public class Users extends BaseEntity {
    @Column(length = 50, nullable = false)
    private String firstname;
    @Column(length = 50, nullable = false)
    private String lastname;
    @Column(unique = true, length = 100, nullable = false, updatable = false)
    private String email;
    private String password;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dob;
    private String photo;
    private Gender gender;

    private boolean accountLocked;
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public String getName() {
        return firstname + " " + lastname;
    }

    @PrePersist
    @PreUpdate
    private void validateAge() {
        if (dob != null &&
                ChronoUnit.YEARS.between(dob, LocalDate.now()) < 13) {
            throw new InvalidAgeException("User must be at least 13 years old");
        }
    }

    public int calculateAge() {
        return Period.between(dob, LocalDate.now()).getYears();
    }
}