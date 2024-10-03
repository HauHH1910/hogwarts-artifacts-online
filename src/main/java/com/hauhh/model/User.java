package com.hauhh.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Builder
@Setter
@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userID;

    private String username;

    private String password;

    private boolean enabled;

    private String roles;


}
