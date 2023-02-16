package com.chatter.database.dto.user;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserRegisterInDto {

    @NotNull
    @Size(max = 50)
    @Column(nullable = false)
    private String fullName;

    @NotNull
    @Size(max = 14)
    @Column(nullable = false)
    private String displayName;

    @NotNull
    @Column(nullable = false)
    private String phone;


    @NotNull
    @Column(nullable = false)
    private String country;

    @NotNull
    @Column(nullable = false)
    private Date birthdayDate;

    @Email
    @NotNull
    @Column(unique = true, nullable = false)
    private String email;

    @NotNull
    @Column(nullable = false)
    private String password;
}
