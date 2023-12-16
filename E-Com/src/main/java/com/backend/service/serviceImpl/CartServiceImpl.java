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
import com.backend.repository.CartItemRepo;
import com.backend.repository.CartRepo;
import com.backend.repository.ProductRepo;
import com.backend.repository.UserRepo;
import com.backend.service.CartService;

import org.modelmapper.ModelMapper;
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

    @Override
    public CartDto addItemToCart(String id, AddItemToCartRequest request) {

        int quantity = request.getQuantity();

        String productId = String.valueOf(request.getProductId());

        if (quantity <= 0) {
            throw new BadApiRequest(AppConstant.INVALIDE_QUANTITY);
        }
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        Cart cart = null;
        try {
            cart = cartRepository.findByUser(user).get();

        } catch (NoSuchElementException ex) {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
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
        cart.setItems(items);


        if (!update.get()) {
            CartItem cartItem = CartItem.builder().cart(cart).totalPrice(quantity + product.getPrice()).quantity(quantity).product(product).build();
            cart.getItems().add(cartItem);
        }
        cart.setUser(user);
        Cart updateCart = cartRepository.save(cart);
        return this.modelMapper.map(updateCart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(String id, int cartItem) {
        CartItem cartItem1 = cartItemRepo.findById(cartItem).orElseThrow(() -> new ResourceNotFoundException("Cart Not Found"));
        cartItemRepo.delete(cartItem1);
    }
    @Override
    public void clearCart(String id) {

        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart Not Found"));

        cart.getItems().clear();

        cartRepository.save(cart);
    }
}
