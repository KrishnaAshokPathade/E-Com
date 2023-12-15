package com.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_Item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartItemId;
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(name = "quantity")
    private int quantity;

    @Column(name = "totalPrice")
    private int totalPrice;
    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;


}
