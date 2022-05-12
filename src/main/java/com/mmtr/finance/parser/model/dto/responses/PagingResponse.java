package com.mmtr.finance.parser.model.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PagingResponse<T> {
    private List<T> items;

    private Integer currentPage;

    private Long totalItems;

    private Integer totalPages;
}
