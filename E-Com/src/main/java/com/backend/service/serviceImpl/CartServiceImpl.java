package com.backend.service.serviceImpl;

import com.backend.constants.AppConstant;
import com.backend.exception.BadApiRequest;
import com.backend.exception.ResourceNotFoundException;
import com.backend.model.Cart;
import com.backend.model.CartItem;
import com.backend.model.Product;
import com.backend.model.User;
import com.backend.payload.AddItemToCartRequest;
import com.backend.payload.CartDto;

import com.backend.payload.UserDto;
import com.backend.repository.CartItemRepo;
import com.backend.repository.CartRepo;
import com.backend.repository.ProductRepo;
import com.backend.repository.UserRepo;
import com.backend.service.CartService;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    private Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    @Autowired
    private CartItemRepo cartItemRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CartRepo cartRepository;



    /**
     * @param userId
     * @param request
     * @return cartDto
     * @apiNote This Api is used to add item in Cart in databased
     */
    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {


        int quantity = request.getQuantity();

        String productId = request.getProductId();

        if (quantity <= 0) {
            throw new BadApiRequest(AppConstant.INVALIDE_QUANTITY);
        }
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        Cart cart = null;
        try {
            cart = cartRepository.findByUser(user).get();

        } catch (NoSuchElementException ex) {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
            cart.setUser(user);


            logger.info("Save the Cart Details:{}", cart);
        }

        AtomicReference<Boolean> update = new AtomicReference(false);
        List<CartItem> items = cart.getItems();
        items.stream().map(item -> {
            if (item.getProduct().getProductId().equals(productId)) {
                item.setQuantity(quantity + item.getQuantity()); //change the quantity
                item.setTotalPrice(quantity * product.getPrice() + item.getTotalPrice());
                update.set(true);
            }
            return items;

        }).collect(Collectors.toList());
      //  cart.setItems(items);

        if (!update.get()) {
            CartItem cartItem = CartItem.builder().cart(cart).
                    totalPrice(quantity + product.getPrice()).
                    quantity(quantity).product(product)
                    .build();
            cart.getItems().add(cartItem);
        }

        Cart updateCart = cartRepository.save(cart);
        logger.info("Update the cart Details:{}", updateCart);
        return this.modelMapper.map(updateCart, CartDto.class);
    }

    /**
     * Remove the item  by providing the cartItemId
     * *@param cartItemId
     * @param  userId
     * @apiNote This Api is used to remove item from cart
     */
    @Override
    public void removeItemFromCart(String userId, int cartItemId) {
        CartItem cartItem1 = cartItemRepo.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("Cart Not Found"));
        logger.info("Remove the Item from Cart with cartItem :{}", cartItem1.getCartItemId());
        cartItemRepo.delete(cartItem1);
        logger.info("Remove Item from cart");
    }

    /**
     * Clear the cart  by providing the userId
     * *@param userId
     * @apiNote This Api is used to clear the cart  from the database.
     */
    @Override
    public void clearCart(String userId) {

        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart Not Found"));
        cart.getItems().clear();
        logger.info("Clear the cart with userId,{}", user.getUserId());
        cartRepository.save(cart);
    }

    /**
     * Retrieve the Cart by provide userId.
     *
     * @param userId
     * @return CartDto  Retrive the single data from database.
     * @apiNote To get single cart data from database using userId
     */
    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        logger.info("Fetch the User with userId :{}", user.getUserId());
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart Not Found"));
        logger.info("Fetch the Cart with user :{}", user);
        return this.modelMapper.map(cart, CartDto.class);
    }
}
