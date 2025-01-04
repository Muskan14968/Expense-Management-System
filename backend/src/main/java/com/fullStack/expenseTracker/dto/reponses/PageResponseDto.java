package com.fullStack.expenseTracker.dto.reponses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageResponseDto<T> {

    T data;

    int totalNoOfPages;

    Long totalNoOfRecords;

}
