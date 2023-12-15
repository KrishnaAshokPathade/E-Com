package com.backend.payload;

import com.backend.model.CartItem;
import com.backend.model.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CartDto {
    private String cartId;
    private Date createdAt;
    private User user;
    private List<CartItem> items = new ArrayList<>();
}
