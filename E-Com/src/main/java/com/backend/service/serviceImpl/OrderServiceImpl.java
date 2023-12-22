package com.backend.service.serviceImpl;

import com.backend.exception.BadApiRequest;
import com.backend.exception.ResourceNotFoundException;
import com.backend.model.*;
import com.backend.payload.CreateOrderRequest;
import com.backend.payload.OrderDto;
import com.backend.payload.PagableResponce;
import com.backend.repository.CartRepo;
import com.backend.repository.OrderRepo;
import com.backend.repository.UserRepo;
import com.backend.service.OrderService;
import com.backend.utitlity.Helper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private CartRepo cartRepo;



    /**
     * @param orderDto
     * @return orderDto
     * @apiNote This Api is used create new order in databased
     */
    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {

        String userId = orderDto.getUserId();
        String cartId = orderDto.getCartId();

        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found with given userId !!"));
        Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart Not Found with given cartId !!"));


        List<CartItem> cartItems = cart.getItems();
        if (cartItems.size() <= 0) {
            throw new BadApiRequest("Invalide Number of Items in Cart");
        }

        Order order = Order.builder().
                billingName(orderDto.getBillingName()).
                billingAddress(orderDto.getBillingAddress()).
                orderDate(new Date()).orderStatus(orderDto.getOrderStatus()).
                billingPhone(orderDto.getBillingPhone()).
                paymentStatus(orderDto.getPaymentStatus()).
                deliverDate(null).
                user(user).
                orderId(UUID.randomUUID().toString()).build();
        logger.info("create the order :{}", order);

        AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            //cartItem -> orderItem

            OrderItem orderItem = OrderItem.builder().quantity(cartItem.getQuantity()).product(cartItem.getProduct()).totalPrice(cartItem.getTotalPrice()).order(order).build();
            orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());

            return orderItem;
        }).collect(Collectors.toList());
        order.setOrderAmount(order.getOrderAmount());
        order.setOrderItemList(orderItems);

        cart.getItems().clear();

        cartRepo.save(cart);
        Order saveOrder = orderRepo.save(order);
        logger.info("Order created", saveOrder);
        return modelMapper.map(saveOrder, OrderDto.class);
    }

    /**
     * @param orderId
     * @apiNote This Api is used to remove the Order from database
     */

    @Override
    public void removeOrder(String orderId) {

        Order order = orderRepo.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order Not Found !!"));
        logger.info("Fetching single Order:{}", order.getOrderId());
        orderRepo.delete(order);
        logger.info("Order Deleted");

    }


    /**
     * @param userId
     * @return OrderDto
     * @apiNote This Api is used to get the Order from databased
     */
    @Override
    public OrderDto getOrdersOfUser(String userId) {

        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        logger.info("Fetch single user with userId:{}", userId);
        Order order = orderRepo.findByUser(user);
        logger.info("Fetch the order with user:{}", order);
        return this.modelMapper.map(order, OrderDto.class);
    }


    /**
     * Retrive the PagableResponce by providing the specific parameter
     * Retrive All the data of Category.
     *
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return  Return http status for getting data
     * @apiNote To get all user data from database
     */
    @Override
    public PagableResponce<OrderDto> getAllByPageble(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Order> page = orderRepo.findAll(pageable);
        logger.info("Fetching all Page ,{}",page.getTotalPages());
        return Helper.getPagebleResponce(page, OrderDto.class);
    }
}
