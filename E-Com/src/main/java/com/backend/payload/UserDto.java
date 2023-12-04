package com.backend.payload;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor

public class UserDto {

    private String userId;
    @Size(min=3,max = 30,message ="Invalid Name !!")
    private String name;
    @Email(message = "Invalid Email Id !!")
    @NotBlank(message = "Email should not blank")
    private String email;
    @NotBlank(message = "Invalid Password")
    private String password;
    @NotBlank(message = "Write something about yourself")
    private String about;
    @Size(min = 4,max = 50,message = "Fill the proper details")
    private String gender;
    private String imageName;
}
