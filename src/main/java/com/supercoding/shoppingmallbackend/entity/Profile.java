package com.supercoding.shoppingmallbackend.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "profiles")
public class Profile extends CommonField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Long id;

    @Size(max = 63)
    @NotNull
    @Column(name = "email", nullable = false, length = 63)
    private String email;

    @Size(max = 63)
    @NotNull
    @Column(name = "password", nullable = false, length = 63)
    private String password;

    @Lob
    @Column(name = "image_url")
    private String imageUrl;

    @Size(max = 63)
    @NotNull
    @Column(name = "name", nullable = false, length = 63)
    private String name;

    @Size(max = 15)
    @NotNull
    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @NotNull
    @Column(name = "paymoney", nullable = false)
    private Long paymoney;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProfileRole role;
}