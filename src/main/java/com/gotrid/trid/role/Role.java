package com.gotrid.trid.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gotrid.trid.common.BaseEntity;
import com.gotrid.trid.user.Users;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"usersSet"})
@Entity
public class Role extends BaseEntity {

    @Column(unique = true, length = 20)
    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Set<Users> usersSet = new HashSet<>();


}
