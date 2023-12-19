package com.backend.payload;

import com.backend.model.Order;
import com.backend.model.Product;
import lombok.*;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class OrderItemDto {

    private int orderItemId;

    private int quantity;

    private int totalPrice;

    private ProductDto productDto;
}

