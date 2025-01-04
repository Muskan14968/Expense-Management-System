package com.fullStack.expenseTracker.dto.reponses;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.sql.Date;

@Data
@AllArgsConstructor
public class MonthlyTransactionDto {

    private Date date;

    private String description;

    private String categoryName;

    private double amount;

}
