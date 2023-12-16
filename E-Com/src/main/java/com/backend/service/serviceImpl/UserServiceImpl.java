package com.backend.service.serviceImpl;

import com.backend.exception.ResourceNotFoundException;
import com.backend.model.User;
import com.backend.payload.PagableResponce;
import com.backend.payload.UserDto;
import com.backend.repository.UserRepo;
import com.backend.service.UserService;
import com.backend.utitlity.Helper;
import org.modelmapper.ModelMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Value("${user.image}")
    private String imagePath;


    /**
     * Create the User by providing User specific details
     * Generate the random UserId.
     *
     * @param userDto
     * @return http status for save data
     * @apiNote This Api is used to create new user in databased
     */

    @Override
    public UserDto createUser(UserDto userDto) {

        String userId = UUID.randomUUID().toString();
        logger.info("Generated the random userId :{}", userId);
        userDto.setUserId(userId);
        User dtoToUser = this.dtoToUser(userDto);
        User saveUser = this.userRepo.save(dtoToUser);
        return this.userToDto(saveUser);
    }

    /**
     * Update the User by providing the user parameter and UserId
     *
     * @param userDto
     * @param userId  for upadate the user.
     * @return userDto
     * @apiNote This Api is used to update user data with id in  database
     */
    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("UserId Not Found !!"));
        logger.info("Update the User with userDto and userId :{}", userId, userDto);
        user.setAbout(userDto.getAbout());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());

        User updateUser = userRepo.save(user);
        //  return this.modelMapper.map(updateUser, UserDto.class);

        UserDto updateDto = userToDto(updateUser);
        logger.info("Update User:{}", updateDto);
        return updateDto;
    }

    /**
     * Delete the User by providing the userId
     * *@param userId  provide the unique userId for delete user.
     *
     * @apiNote This Api is used to delete user data with id in  database
     */

    @Override
    public void deleteUser(String userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("UserId Not Found"));
        String fullpath = imagePath + user.getImageName();

        try {
            Path path = Paths.get(fullpath);
            logger.info("User path" + path);
            Files.delete(path);
        } catch (NoSuchFileException ex) {
            logger.info("User image not found");
            ex.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.userRepo.delete(user);
        logger.info("User Delete Successfully");
    }


    /**
     * Retrive the PagableResponce by providing the spicific parameter
     * Retrive All the data of User.
     *
     * @return http status for getting data
     * @paramuserDto used to get to data
     * @apiNote To get all user data from database
     */
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

    /**
     * Retrive the User by provide userId.
     *
     * @param id id
     * @return http status for get single data from database
     * * @param userDto UserDto Object
     * @apiNote To get single user data from database using id
     */
    @Override
    public UserDto getUserById(String userId) throws Exception {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("UserId not Found "));
        logger.info("Fetch the User by userId:{}", user.getUserId());
        return userToDto(user);

    }

    /**
     * Searches for users based on a keyword in their names.
     *
     * @param Keyword The keyword to search for in user names.
     * @return A list of data transfer objects representing the matched users.
     */

    @Override
    public List<UserDto> searchUser(String Keyword) {
        List<User> users = userRepo.findByNameContaining(Keyword);
        logger.info("Fetching  User with Keyword :{}", users);
        List<UserDto> dtoList = users.stream().map(user -> userToDto(user)).collect(Collectors.toList());
        return dtoList;
    }

    /**
     * @param email
     * @return userDto UserDto
     * @apiNote This Api is used to get user data with email from database
     */
    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Email Not Found"));
        logger.info("Fetch the User with email :{}", user.getEmail());
        return userToDto(user);
    }


    /**
     * Converts a UserDto object to a User entity.
     *
     * @param userDto
     * @return User.
     */

    public User dtoToUser(UserDto userDto) {
        // User user = this.modelMapper.map(userDto, User.class);
//
//        User user = new User();
//        user.setUserId(userDto.getUserId());
//        user.setName(userDto.getName());
//        user.setEmail(userDto.getEmail());
//        user.setAbout(userDto.getAbout());
//        user.setGender(userDto.getGender());
//        user.setImageName(userDto.getImageName());
//        user.setPassword(userDto.getPassword());
//        return user;
//        by using modelmapper
        return modelMapper.map(userDto, User.class);
//
//        second method used to conver the userDto to user;
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

    /**
     * Converts a User entity to a UserDto object.
     *
     * @param user
     * @return UserDto
     */

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
