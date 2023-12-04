package com.backend.service;


import com.backend.model.User;
import com.backend.payload.PagableResponce;
import com.backend.payload.UserDto;
import com.backend.repository.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;
    User user;
    @MockBean
    private UserRepo userRepo;

    @BeforeEach
    public void init() {
        user = User.builder()
                .email("Krishna@gmail.com")
                .name("Krishna")
                .about("Mechanical")
                .gender("Male")
                .password("12333")
                .imageName("kri.png")
                .build();

    }

    @Test
    public void createUserTest() {
        Mockito.when(userRepo.save(Mockito.any())).thenReturn(user);
        UserDto userDto = userService.createUser(modelMapper.map(user, UserDto.class));
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals("Krishna", userDto.getName(), "User Not Found");

    }

    @Test
    public void updateUserTest() {

        String userId = "";

        UserDto userDto = new UserDto();
        userDto.setEmail("Sagar@gmail.com");
        userDto.setName("Sagar");
        userDto.setAbout("Electrical");
        userDto.setGender("Male");
        userDto.setPassword("12333");
        userDto.setImageName("kri.png");


        Mockito.when(userRepo.findById(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(userRepo.save(Mockito.any())).thenReturn(user);

        UserDto updateUser = userService.updateUser(userDto, userId);
        System.out.println(updateUser.getName());
        Assertions.assertNotNull(updateUser);
        Assertions.assertEquals(userDto.getName(), updateUser.getName(), "User Name Not Matched !!");
    }

    @Test
    public void deleteUserTest() {
        String userId = "Krishna";
        //User user = User.builder().userId(userId).build();

        Mockito.when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        userService.deleteUser(userId);
        Mockito.verify(userRepo, Mockito.times(1)).delete(user);
    }

    @Test
    public void getAllTest() {

        User user1 = User.builder()
                .email("rahul@gmail.com")
                .name("Rahul Pathade")
                .about("Mechanical")
                .gender("Male")
                .password("12333")
                .imageName("kri.png")
                .build();
        User user2 = User.builder()
                .email("pawan@gmail.com")
                .name("Pawan Pathade")
                .about("Mechanical")
                .gender("Male")
                .password("12333")
                .imageName("kri.png")
                .build();
        List<User> usersList = Arrays.asList(user, user1, user2);

        Page<User> page = new PageImpl<>(usersList);
        Mockito.when(userRepo.findAll((Pageable) Mockito.any())).thenReturn( page);

        PagableResponce<UserDto> pagableResponce = userService.getAll(1, 1, "name", "ase");
        Assertions.assertEquals(3, pagableResponce.getContent().size());
    }

    @Test
    public void getUserByIdTest() throws Exception {
        String userId = "Krishna";

        Mockito.when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        UserDto userDto = userService.getUserById(userId);
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(user.getName(), userDto.getName(), "Name not matched !!");
    }

    @Test
    public void getUserByEmailTest() {
        String emailId = "Sagar@gmail.com";
        Mockito.when(userRepo.findByEmail(emailId)).thenReturn(Optional.of(user));
        UserDto userDto = userService.getUserByEmail(emailId);
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(user.getEmail(), userDto.getEmail(), "Email Not Matched !!");

    }

    @Test
    public void searchUserTest() {

        User user1 = User.builder()
                .email("rahul@gmail.com")
                .name("Rahul Pathade")
                .about("Mechanical")
                .gender("Male")
                .password("12333")
                .imageName("kri.png")
                .build();
        User user2 = User.builder()
                .email("pawan@gmail.com")
                .name("Pawan Pathade")
                .about("Mechanical")
                .gender("Male")
                .password("12333")
                .imageName("kri.png")
                .build();
        User user3 = User.builder()
                .email("ramesh@gmail.com")
                .name("Ramesh Pathade")
                .about("Mechanical")
                .gender("Male")
                .password("12333")
                .imageName("kri.png")
                .build();

        String searchId = "Pathade";
        Mockito.when(userRepo.findByNameContaining(searchId)).thenReturn(Arrays.asList(user, user1, user2, user3));
        List<User> users = userRepo.findByNameContaining(searchId);

        Assertions.assertEquals(4, users.size(), "Size not matched !!");
    }
}
