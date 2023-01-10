package com.chatter.database.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(nullable = false)
    private String fullName;

    @NotNull
    @Size(max = 14)
    @Column(nullable = false)
    private String displayName;

    @NotNull
    @Size(max = 5)
    @Column(nullable = false)
    private String displayNameCode; // #9394 = '#' + 4 numbers

    @NotNull
    @Column(nullable = false)
    private String phone;


    @NotNull
    @Column(nullable = false)
    private String country;

    @Email
    @NotNull
    @Column(unique = true, nullable = false)
    private String email;

    @NotNull
    @Column(nullable = false)
    private String password;
}
