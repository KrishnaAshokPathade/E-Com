package com.backend.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class PagableResponce<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private int totalPage;
    private long totalElement;
    private boolean lastPage;

}
