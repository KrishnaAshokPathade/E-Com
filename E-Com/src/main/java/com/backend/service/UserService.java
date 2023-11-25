package com.backend.service;

import com.backend.payload.PagableResponce;
import com.backend.payload.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, String userId);

    void deleteUser(String userId);

    PagableResponce<UserDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);

    UserDto getUserById(String userId) throws Exception;

    List<UserDto> searchUser(String Keyword);

    UserDto getUserByEmail(String email);


}
