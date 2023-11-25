package com.backend.utitlity;

import com.backend.model.User;
import com.backend.payload.PagableResponce;
import com.backend.payload.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    public static <U, V> PagableResponce<V> getPagebleResponce(Page<U> page, Class<V> type) {
        List<U> entity = page.getContent();
        List<V> dtoList = entity.stream().map(object -> new ModelMapper().map(object, type)).collect(Collectors.toList());
        PagableResponce<UserDto> pageResponce = new PagableResponce<>();
        pageResponce.setContent((List<UserDto>) dtoList);
        pageResponce.setPageNumber(page.getNumber());
        pageResponce.setPageSize(page.getSize());
        pageResponce.setTotalPage(page.getTotalPages());
        pageResponce.setLastPage(page.isLast());
        pageResponce.setTotalElement(page.getTotalElements());
        return (PagableResponce<V>) pageResponce;
    }
}
