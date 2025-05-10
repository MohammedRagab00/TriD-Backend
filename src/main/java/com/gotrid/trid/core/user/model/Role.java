package com.gotrid.trid.core.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gotrid.trid.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Users> usersSet = new HashSet<>();

}
