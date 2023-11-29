package com.backend.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@NoArgsConstructor
@Data
public class CategoryDto {

    private String categoryId;
    @NotBlank
    @Size(min = 4, max = 50, message = "Fill the proper title")
    private String title;
    @NotBlank
    @Size(min = 4, max = 50, message = "Fill the proper desc")
    private String description;

    private String coverImage;

}

