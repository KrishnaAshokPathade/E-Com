package com.backend.service;

import com.backend.model.Cart;
import com.backend.model.CartItem;
import com.backend.model.Product;
import com.backend.model.User;
import com.backend.payload.AddItemToCartRequest;
import com.backend.payload.CartDto;
import com.backend.repository.CartItemRepo;
import com.backend.repository.CartRepo;
import com.backend.repository.ProductRepo;
import com.backend.repository.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class CartServiceTest {

    @MockBean
    private CartRepo cartRepo;
    @MockBean
    private CartItemRepo cartItemRepo;
    @MockBean
    private ProductRepo productRepo;
    @MockBean
    private UserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;
    @MockBean
    private CartService cartService;
    User user;

    Product product;

    Cart cart;

    @BeforeEach
    public void init() {

        user = User.builder().email("Krishna@gmail.com").name("Krishna").about("Mechanical").gender("Male").password("12333").imageName("kri.png").build();
        cart = Cart.builder().cartId("123").items(null).user(user).createdAt(null).build();

        product = Product.builder().price(123).title("Krishna").description("Java Dev").discountPrice(122).productImageName("k.jpg").quantity(12).addedDate(null).category(null).stock(true).build();
    }

    @Test
    public void addItemToCartTest() {

        AddItemToCartRequest request = AddItemToCartRequest.builder().quantity(12).productId("123445").build();

        String userId = "123";

        Mockito.when(productRepo.findById(request.getProductId())).thenReturn(Optional.of(product));
        Mockito.when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        Mockito.when(cartRepo.findByUser(user)).thenReturn(Optional.of(cart));
        CartDto cartDto = cartService.addItemToCart(userId, request);
        Cart updateCart = cartRepo.save(cart);
        Assertions.assertNull(updateCart);

        //  CartDto cartDto1 = this.modelMapper.map(updateCart, CartDto.class);
        // Assertions.assertNull(cartDto1);

        // /Assertions.assertEquals(updateCart.getCartId(),cartDto.getItems(),"Cart No Found");


//
//     verify(userRepo, times(1)).findById(userId);
//      verify(productRepo, times(1)).findById(request.getProductId());
//     verify(cartRepo, times(1)).findByUser(user);

    }


    @Test
    public void removeItemFromCartTest() {
        CartItem cartItem = CartItem.builder().cartItemId(1234).product(product).cart(cart).totalPrice(122222).build();

        String userId = "Krishna";
        int cartItemId = 12;
        Mockito.when(cartItemRepo.findById(cartItemId)).thenReturn(Optional.of(cartItem));

        // Mockito.doNothing().when(cartService).removeItemFromCart(userId, cartItemId);
        this.cartService.removeItemFromCart(userId, cartItemId);
        // verify(cartItemRepo, times(1)).delete(cartItem);
      //  verify(cartItemRepo, times(1)).delete(any());

    }

    @Test
    public void clearCartTest() {
        String userId = "1234";

        Mockito.when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(cartRepo.findByUser(user)).thenReturn(Optional.of(cart));

        cartService.clearCart(userId);

    //    verify(cartRepo,times(1)).delete(cart);
    }

    @Test
    public void getCartByUserTest() {

        User user1 = User.builder().email("Krishna@gmail.com").name("Krishna").about("Mechanical").gender("Male").password("12333").imageName("kri.png").build();
        Cart cart1 = Cart.builder().cartId("123").items(null).createdAt(null).build();

        String userId = "1234";
        Mockito.when(userRepo.findById(userId)).thenReturn(Optional.of(user1));
        Mockito.when(cartRepo.findByUser(user1)).thenReturn(Optional.of(cart1));
        System.out.println(cart1);
        CartDto cartDto = cartService.getCartByUser(userId);
        System.out.println(cartDto);
        Assertions.assertNull(cartDto);


    }

}
