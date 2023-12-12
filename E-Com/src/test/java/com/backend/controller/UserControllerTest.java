package com.backend.controller;

import com.backend.model.User;
import com.backend.payload.PagableResponce;
import com.backend.payload.UserDto;
import com.backend.service.FileService;
import com.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private UserService userService;
    @MockBean
    private FileService fileService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ModelMapper modelMapper;

    private User user;
    private final String imageUploadPath = "images/user/";


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
    public void createUserTest() throws Exception {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        Mockito.when(userService.createUser(any())).thenReturn(userDto);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/user/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists());

    }


    private String convertObjectToJsonString(Object user) {
        try {
            return new ObjectMapper().writeValueAsString(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void updateUserTest() throws Exception {
        User user1 = User.builder()
                .email("Pawan@gmail.com")
                .name("Pawan")
                .about("Mechanical")
                .gender("Male")
                .password("12333")
                .imageName("kri.png")
                .build();

        String userId = "Krishna";
        UserDto userDto = modelMapper.map(user, UserDto.class);
        Mockito.when(userService.updateUser((any()), Mockito.anyString())).thenReturn(userDto);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/user/updateUser/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    public void getUserById() throws Exception {

        String userId = "Krishna";

        UserDto userDto = modelMapper.map(user, UserDto.class);
        Mockito.when(userService.getUserById(userId)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/getUserById/" + userId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(".email").value("Krishna@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath(".name").value("Krishna"))
                .andExpect(MockMvcResultMatchers.jsonPath(".about").value("Mechanical"))
                .andExpect(MockMvcResultMatchers.jsonPath(".gender").value("Male"))
                .andExpect(MockMvcResultMatchers.jsonPath(".password").value("12333"))
                .andExpect(MockMvcResultMatchers.jsonPath(".imageName").value("kri.png"))
                .andDo(print());

    }

    @Test
    public void deleteUser() throws Exception {

        User user = User.builder()
                .email("Pawan@gmail.com")
                .name("Pawan")
                .about("Mechanical")
                .gender("Male")
                .password("12333")
                .imageName("kri.png")
                .build();

        String userId = "123";

        Mockito.doNothing().when(userService).deleteUser(userId);


        mockMvc.perform(MockMvcRequestBuilders.delete("/user/deleteUser/" + userId)
                        .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                .andExpect(content().string("Delete User Successfully"));
    }

    @Test
    public void getAllUserTest() throws Exception {
        User user = User.builder()
                .email("rahul@gmail.com")
                .name("Rahul")
                .about("Mechanical")
                .gender("Male")
                .password("12333")
                .imageName("rahul.png")
                .build();
        User user1 = User.builder()
                .email("pawan@gmail.com")
                .name("Pawan")
                .about("Mechanical")
                .gender("Male")
                .password("12333")
                .imageName("pawan.png")
                .build();


        List<User> users = Arrays.asList(user, user1);
        List<UserDto> userDtos = users.stream().map(user2 -> this.modelMapper.map(users, UserDto.class)).collect(Collectors.toList());
        PagableResponce pagableResponce = new PagableResponce();
        Mockito.when(userService.getAll(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(pagableResponce);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/getAllUser/")
                        .param("pageNumber", "1")
                        .param("pageSize", "10")
                        .param("sortDir", "asc")
                        .param("sortBy", "name")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void getUserByEmailTest() throws Exception {

        String email = "Krishna@gmail.com";

        UserDto userDto = modelMapper.map(user, UserDto.class);
        Mockito.when(userService.getUserByEmail(email)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/getUserByEmail/" + email))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(".name").value("Krishna"))
                .andExpect(MockMvcResultMatchers.jsonPath(".about").value("Mechanical"))
                .andExpect(MockMvcResultMatchers.jsonPath(".gender").value("Male"))
                .andExpect(MockMvcResultMatchers.jsonPath(".password").value("12333"))
                .andExpect(MockMvcResultMatchers.jsonPath(".imageName").value("kri.png"))
                .andDo(print());

    }

    @Test
    public void searchUserTest() throws Exception {
        UserDto useDto2 = new UserDto();
        useDto2.setEmail("Krishna@gmail.com");
        useDto2.setName("Krishna");
        useDto2.setAbout("Mechanical");
        useDto2.setGender("Male");
        useDto2.setPassword("12333");
        useDto2.setImageName("kri.png");

        List<UserDto> userDtos = Arrays.asList(useDto2);
        //  List<UserDto> userDto = users.stream().map(user -> this.modelMapper.map(users, UserDto.class)).collect(Collectors.toList());
        String Keyword = "Kri";
        Mockito.when(userService.searchUser(Keyword)).thenReturn(userDtos);
        mockMvc.perform(MockMvcRequestBuilders.get("/user/searchUser/" + Keyword))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(".email").value("Krishna@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath(".name").value("Krishna"))
                .andExpect(MockMvcResultMatchers.jsonPath(".about").value("Mechanical"))
                .andExpect(MockMvcResultMatchers.jsonPath(".gender").value("Male"))
                .andExpect(MockMvcResultMatchers.jsonPath(".password").value("12333"))
                .andExpect(MockMvcResultMatchers.jsonPath(".imageName").value("kri.png"))
                .andDo(print());
    }

    @Test
    public void uploadImageTest() throws Exception {
        String userId = "123";
        MockMultipartFile image = new MockMultipartFile("userImage", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "image data".getBytes());
        String imageName = "test_image.jpg";

        Mockito.when(fileService.uploadFile(image, imageUploadPath)).thenReturn(imageName);

        UserDto userDto = new UserDto();

        Mockito.when(userService.getUserById(userId)).thenReturn(userDto);
        Mockito.when(userService.updateUser(any((UserDto.class)), eq(userId))).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/user/uploadImage/{userId}", userId)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.imageName").value(imageName))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.message").value("Upload Image Successfully"))
                .andReturn();
    }

    @Test
    public void getUserImageTest() throws Exception {
        String userId = "123";
        String imageName = "test_image.jpg";
        UserDto userDto = new UserDto();
        userDto.setImageName(imageName);

        Mockito.when(userService.getUserById(userId)).thenReturn(userDto);

        byte[] imageData = "Test image data".getBytes();
        InputStream inputStream = new ByteArrayInputStream(imageData);
        Mockito.when(fileService.getResource(imageUploadPath, imageName)).thenReturn(inputStream);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/image/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG_VALUE))
                .andExpect(content().bytes(imageData))
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(MediaType.IMAGE_JPEG_VALUE, response.getContentType());
        assertArrayEquals(imageData, response.getContentAsByteArray());
    }
}



