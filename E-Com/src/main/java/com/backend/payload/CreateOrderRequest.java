package com.backend.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    private String cartId;
    private String userId;

    private String orderStatus = "Pending";

    private String paymentStatus = "Not_Paid";


    private String billingAddress;

    private String billingPhone;
    private String billingName;


}
