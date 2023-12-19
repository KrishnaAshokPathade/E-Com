package com.backend.payload;

import com.backend.model.OrderItem;
import com.backend.model.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private int orderId;

    private String orderStatus="Pending";

    private String paymentStatus="Not_Paid";

    private int orderAmount;

    private String billingAddress;

    private String billingPhone;
    private String billingName;

    private Date orderDate=new Date();

    private Date deliverDate;
    //private UserDto userDto;

    private List<OrderItemDto> orderItemList = new ArrayList<>();
}
