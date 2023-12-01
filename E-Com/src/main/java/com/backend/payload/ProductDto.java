package com.backend.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
public class ProductDto {
    private String productId;
    @Size(min = 4, max = 30, message = "Fill the proper title of product")
    @NotBlank
    private String title;
    @NotBlank
    @Size(min = 3, max = 90, message = "Fill the desc of product !!")

    private String description;


    private int quantity;

    private int price;
    private int discountPrice;


    @JsonFormat(pattern = "dd/MM/yyyy")

    private Date addedDate;

    private boolean stock;
    @NotBlank
    private String productImageName;
    //   private CategoryDto categoryDto;
}
