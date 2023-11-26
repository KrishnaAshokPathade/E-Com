package com.backend.service.serviceImpl;

import com.backend.exception.ResourceNotFoundException;
import com.backend.model.User;
import com.backend.payload.PagableResponce;
import com.backend.payload.UserDto;
import com.backend.repository.UserRepo;
import com.backend.service.UserService;
import com.backend.utitlity.Helper;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto createUser(UserDto userDto) {

        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        User dtoToUser = this.dtoToUser(userDto);
        User saveUser = this.userRepo.save(dtoToUser);

        return this.userToDto(saveUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found Exception !!"));

        user.setAbout(userDto.getAbout());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());

        User updateUser = userRepo.save(user);
        //  return this.modelMapper.map(updateUser, UserDto.class);

        UserDto updateDto = userToDto(updateUser);
        return updateDto;
    }

    @Override
    public void deleteUser(String userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found Exception"));

        this.userRepo.delete(user);
    }

    @Override
    public PagableResponce<UserDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {

        // Sort sort=Sort.by(sortBy);

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        //pageNumber default start from 0
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<User> page = this.userRepo.findAll(pageable);

        PagableResponce<UserDto> responce = Helper.getPagebleResponce(page, UserDto.class);
        return responce;

    }

    @Override
    public UserDto getUserById(String userId) throws Exception {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("UserId not Found "));
        return userToDto(user);

    }

    @Override
    public List<UserDto> searchUser(String Keyword) {
        List<User> users = userRepo.findByNameContaining(Keyword);

        List<UserDto> dtoList = users.stream().map(user -> userToDto(user)).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not Found with given Email"));

        return userToDto(user);
    }

    public User dtoToUser(UserDto userDto) {
        // User user = this.modelMapper.map(userDto, User.class);

//        User user = new User();
//        user.setUserId(userDto.getUserId());
//        user.setName(userDto.getName());
//        user.setEmail(userDto.getEmail());
//        user.setAbout(userDto.getAbout());
//        user.setGender(userDto.getGender());
//        user.setImageName(userDto.getImageName());
//        user.setPassword(userDto.getPassword());
//        return user;
//by using modelmapper
        return modelMapper.map(userDto, User.class);

// second method used to conver the userDto to user;
//        User user = user.builder().
//                email(userDto.getEmail())
//                .about(userDto.getAbout())
//                .name(userDto.getName())
//                .gender(userDto.getGender())
//                .password(userDto.getPassword())
//                .userId(userDto.getUserId())
//                .imageName(userDto.getImageName())
//                .build();
//        return user;

    }

    public UserDto userToDto(User user) {
        //UserDto userDto = this.modelMapper.map(user, UserDto.class);
//
//        UserDto userDto = new UserDto();
//        userDto.setUserId(user.getUserId());
//        userDto.setName(user.getName());
//        userDto.setAbout(user.getAbout());
//        userDto.setEmail(user.getEmail());
//        userDto.setGender(user.getGender());
//        userDto.setImageName(user.getImageName());
//        userDto.setPassword(user.getPassword());
//        return userDto;
//
        // by using the modelmapper
        return modelMapper.map(user, UserDto.class);
    }

}
