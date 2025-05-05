package com.gotrid.trid.marshmello.models.Shops;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "details_shop")
@NoArgsConstructor
@AllArgsConstructor
public class DetailsShop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "location")
    private String location;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "facebook")
    private String facebook;

    public DetailsShop(String type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public String toString() {
        return "DetailsShop{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", facebook='" + facebook + '\'' +
                '}';
    }
// Constructors, getters, setters, equals, hashCode, toString
    // Add all getters and setters
    // Add equals and hashCode
    // Add toString
}
