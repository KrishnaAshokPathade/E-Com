package com.backend.controller;

import com.backend.model.Cart;
import com.backend.model.User;
import com.backend.payload.AddItemToCartRequest;
import com.backend.payload.CartDto;
import com.backend.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.http.ResponseEntity.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest {
    @MockBean
    private CartService cartService;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    MockMvc mockMvc;


    Cart cart;

    User user;

    @BeforeEach
    public void init() {
        user = User.builder()
                .email("Krishna@gmail.com")
                .name("Krishna")
                .about("Mechanical")
                .gender("Male")
                .password("12333")
                .imageName("kri.png")
                .build();

        cart = Cart.builder()
                .cartId("12344")
                .user(user)
                .createdAt(null)
                .items(null)
                .build();


    }

    @Test
    public void addItemToCartTest() throws Exception {

        AddItemToCartRequest cartRequest = AddItemToCartRequest.builder()
                .productId("12344")
                .quantity(123)
                .build();

        String userId = "1234";

        CartDto cartDto = this.modelMapper.map(cart, CartDto.class);
        Mockito.when(cartService.addItemToCart(Mockito.anyString(), Mockito.any())).thenReturn(cartDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/cart/addItemToCart/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(cartRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private String convertObjectToJsonString(Object cartRequest) {
        try {
            return new ObjectMapper().writeValueAsString(cartRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void removeItemFromCartTest() throws Exception {

        String userId = "123";
        int cartItemId = 12;

        Mockito.doNothing().when(cartService).removeItemFromCart(userId, cartItemId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/cart/{userId}/removeItemFromCart/{cartItemId}", userId, cartItemId))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Remove the Item from Cart Successfully"));

    }

    @Test
    public void clearCartTest() throws Exception {
        String userId = "123";

        Mockito.doNothing().when(cartService).clearCart(userId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/cart/clearCart/{userId}", userId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Cart Clear Successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
    }

    @Test
    public void getCartByUserTest() throws Exception {

        String userId = "123";

        CartDto cartDto = this.modelMapper.map(cart, CartDto.class);
        Mockito.when(cartService.getCartByUser(Mockito.anyString())).thenReturn(cartDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/cart/getCartByUser/{userId}", userId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

    }
}
