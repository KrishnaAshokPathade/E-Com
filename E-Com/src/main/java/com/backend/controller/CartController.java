package com.backend.controller;

import com.backend.payload.AddItemToCartRequest;
import com.backend.payload.ApiResponceMessage;
import com.backend.payload.CartDto;
import com.backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping(value = "/addItemToCart/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId, @RequestBody AddItemToCartRequest request) {
        CartDto cartDto = this.cartService.addItemToCart(userId, request);
        return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/removeItemFromCart/{cartItemId}")
    public ResponseEntity<ApiResponceMessage> removeItemFromCart(@PathVariable String userId, @PathVariable int cartItemId) {
        this.cartService.removeItemFromCart(userId, cartItemId);
        ApiResponceMessage apiResponceMessage = ApiResponceMessage.builder().success(true).message("Remove the Item from Cart Successfully").build();
        return new ResponseEntity<ApiResponceMessage>(apiResponceMessage, HttpStatus.OK);
    }

    @DeleteMapping("/clearCart/{userId}")
    public ResponseEntity<  ApiResponceMessage> clearCart(@PathVariable String userId) {
        this.cartService.clearCart(userId);
        ApiResponceMessage apiResponceMessage = ApiResponceMessage.builder().message("Cart Clear Successfully").success(true).build();
        return new ResponseEntity<  ApiResponceMessage>(apiResponceMessage,HttpStatus.OK);
    }

    @GetMapping("/getCartByUser/{userId}")
    public ResponseEntity<CartDto> getCartByUser(@PathVariable String userId) {
        CartDto cartDto = this.cartService.getCartByUser(userId);
        return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
    }

}
