package com.hsf302.pojo;


import com.hsf302.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String fullName;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean isStudent = false;

    private boolean isActive = true;

}
