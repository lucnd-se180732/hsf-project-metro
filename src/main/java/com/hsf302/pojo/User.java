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

    @Column(columnDefinition = ("NVARCHAR(100)"))
    private String fullName;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String identificationNumber;

    @Column(name = "is_student")
    private boolean isStudent = false;

    @Column(name = "is_active")
    private boolean isActive = true;


}
