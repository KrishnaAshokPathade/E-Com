package com.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "category")
public class Category {
    @Id
    @Column(name = "c_Id")
    private String categoryId;
    @Column(name = "c_Title", length = 50, nullable = false)
    private String title;
    @Column(name = "c_Desc", length = 40, nullable = false)
    private String description;
    private String coverImage;
    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,fetch = FetchType.LAZY,targetEntity=Product.class)
    private List<Product> products=new ArrayList<>();



}
