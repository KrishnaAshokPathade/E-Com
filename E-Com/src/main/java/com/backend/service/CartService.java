package com.backend.service;

import com.backend.payload.AddItemToCartRequest;
import com.backend.payload.CartDto;

public interface CartService {


    //add the item to cart
    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    //remove item from cart
    void removeItemFromCart(String userId, int cartItem);

    //remove all item from cart
    void clearCart(String userId);


    CartDto getCartByUser(String userId);
}