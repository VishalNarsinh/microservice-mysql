package com.vishal.accounts.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    private String name;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String mobileNumber;
}
