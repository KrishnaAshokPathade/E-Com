package com.backend.payload;

import com.backend.model.Cart;
import com.backend.model.Product;

import javax.persistence.*;

public class CartItemDto {
    private int cartItemId;
    private Product product;
    private int quantity;
    private int totalPrice;

}
