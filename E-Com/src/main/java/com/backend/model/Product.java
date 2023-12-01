package com.backend.model;

import com.backend.payload.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Product {
    @Id
    private String productId;
    @Column(name = "p_Title")
    private String title;
    @Column(name = "p_Desc")
    private String description;
    @Column(name = "p_Quantity")
    private int quantity;
    @Column(name = "p_Price")
    private int price;
    @Column(name = "p_Disc.Price")
    private int discountPrice;
    @Column(name = "p_AddedDate")
    private Date addedDate;
    @Column(name = "p_Stock")
    private boolean stock;
    @Column(name = "productImageName")
    private String productImageName;
  //  @OneToMany
  //  private CategoryDto categoryDto;

}
