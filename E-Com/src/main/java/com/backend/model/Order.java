package com.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "order_Details")
public class Order {
    @Id
    private int orderId;
    //Pending,Delivered,Dispatched
    private String orderStatus;
    //Not_Paid,Paid

    //boolean
    //enum
    private String paymentStatus;

    private int orderAmount;
    @Column(name = "Billing_Address", length = 1000)
    private String billingAddress;

    private String billingPhone;


    private String billingName;

    private Date orderDate;

    private Date deliverDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_Id")
    private User user;
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderItem> orderItemList = new ArrayList<>();
}
