package com.backend.controller;

import com.backend.model.User;
import com.backend.payload.ImageResponce;
import com.backend.payload.PagableResponce;
import com.backend.payload.UserDto;
import com.backend.service.FileService;
import com.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;
    @Value("${user.image}")
    private String imageUploadPath;

    @PostMapping("/createUser")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto user = this.userService.createUser(userDto);
        return new ResponseEntity<UserDto>(user, HttpStatus.CREATED);
    }

    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable String userId) {
        UserDto updateUser = this.userService.updateUser(userDto, userId);
        return new ResponseEntity<UserDto>(updateUser, HttpStatus.OK);
    }

    @GetMapping("/getUserById/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId) throws Exception {
        UserDto userDto = this.userService.getUserById(userId);


        return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        this.userService.deleteUser((userId));
        return ResponseEntity.ok("Delete User Successfully");
    }

    @GetMapping("/getAllUser")
    public ResponseEntity<PagableResponce<UserDto>> getAllUser(
            @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "50", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {

        return new ResponseEntity<PagableResponce<UserDto>>(this.userService.getAll(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/getUserByEmail/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        UserDto user = this.userService.getUserByEmail(email);
        return new ResponseEntity<UserDto>(user, HttpStatus.OK);
    }

    @GetMapping("/searchUser/{Keyword}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable String Keyword) {
        List<UserDto> list = userService.searchUser(Keyword);
        return new ResponseEntity<List<UserDto>>(list, HttpStatus.OK);
    }

    @PostMapping("/uploadImage/{userId}")
    public ResponseEntity<ImageResponce> uploadImage(@RequestParam("userImage") MultipartFile image, @PathVariable String userId) throws Exception {

        String imageName = this.fileService.uploadFile(image, imageUploadPath);
        UserDto user = this.userService.getUserById(userId);
        //  user.setUserId(imageName);
        user.setImageName(imageName);
        userService.updateUser(user, userId);

        ImageResponce imageResponce = ImageResponce.builder().imageName(imageName).success(true).status("Success").message("Upload Image Successfully").build();
        return new ResponseEntity<ImageResponce>(imageResponce, HttpStatus.CREATED);


    }

    // serve user Image
    @GetMapping("/image/{userId}")
    public ResponseEntity<?> serveUserImage(@PathVariable String userId, HttpServletResponse responce) throws Exception {
        UserDto user = userService.getUserById(userId);
        InputStream resource = fileService.getResource(imageUploadPath, user.getImageName());
        responce.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, responce.getOutputStream());
        return ResponseEntity.ok("Get User Image Successfully");
    }
}