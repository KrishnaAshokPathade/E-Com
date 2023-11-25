package com.backend.model;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User {

    @Id
     //@GeneratedValue(strategy = GenerationType.AUTO)
    private String userId;
    @Column(name = "User_Name")
    private String name;
    @Column(name = "User_Email")
    private String email;
    @Column(name = "user_password", length = 10)
    private String password;
    private String about;
    private String gender;
    @Column(name = "user_imageName")
    private String imageName;
}
