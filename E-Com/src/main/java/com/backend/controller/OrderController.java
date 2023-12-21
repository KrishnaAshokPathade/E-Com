package com.backend.controller;

import com.backend.payload.ApiResponceMessage;
import com.backend.payload.CreateOrderRequest;
import com.backend.payload.OrderDto;
import com.backend.payload.PagableResponce;

import com.backend.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.backend.constants.AppConstant.*;
import static com.backend.constants.AppConstant.SORT_BY_TITLE;

@RestController
@RequestMapping("/order/")
public class OrderController {


    private Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private OrderService orderService;

    @PostMapping("/createOrder")
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequest orderRequest) {
        logger.info("Create new Order with orderRequest :{}", orderRequest);
        OrderDto orderDto = this.orderService.createOrder(orderRequest);
        logger.info("New Order create Successfully :{}", orderDto);
        return new ResponseEntity<OrderDto>(orderDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/removeOrder/{orderId}")
    public ResponseEntity<ApiResponceMessage> removeOrder(@PathVariable String orderId) {
        logger.info("Remove the order with orderId :{}", orderId);
        this.orderService.removeOrder(orderId);
        logger.info("Order remove successfully");
        ApiResponceMessage apiResponceMessage = ApiResponceMessage.builder().success(true).message("Order Delete Successfully").build();
        return new ResponseEntity<>(apiResponceMessage, HttpStatus.OK);
    }

    @GetMapping("/getOrderByUser/{userId}")
    public ResponseEntity<OrderDto> getOrderByUser(@PathVariable String userId) {
        logger.info("Fetch single Order with userId:{}", userId);
        OrderDto orderDto = this.orderService.getOrdersOfUser(userId);
        logger.info("Order fetch successfully:{}", orderDto);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @GetMapping("/getAllByPageble")
    public ResponseEntity<PagableResponce<OrderDto>> getAllByPageble(@RequestParam(value = "pageNumber", defaultValue = PAGE_NUMBER, required = false) int pageNumber,
                                                                     @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE, required = false) int pageSize,
                                                                     @RequestParam(value = "sortDir", defaultValue = SORT_DIR, required = false) String sortDir,
                                                                     @RequestParam(value = "sortBy", defaultValue = SORT_BY_BILLINGNAME, required = false) String sortBy) {
        logger.info("Fetching Product by pagable parameter");
        logger.info("Page Number :{}", pageNumber);
        logger.info("Page Size:{}", pageSize);
        logger.info("Sort Direction :{}", sortDir);
        logger.info("Sort By :{}", sortBy);
        PagableResponce<OrderDto> all = this.orderService.getAllByPageble(pageNumber, pageSize, sortBy, sortDir);
        logger.info("Successfully Fetching the All Product :{}", all);

        return new ResponseEntity<PagableResponce<OrderDto>>(all, HttpStatus.OK);
    }
}