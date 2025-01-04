package com.fullStack.expenseTracker.dto.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class GoalSettingRequestDto {

    @NotNull(message = "User email is required!")
    private String userEmail;

    @NotNull(message = "Goal name is required!")
    @Size(max = 50, message = "Goal name can have at most 50 characters!")
    private String goalName;

    @NotNull(message = "Goal price is required!")
    private Double price;

    @NotNull(message = "Target date is required!")
    private LocalDate targetDate;

    private Double amountSaved;
} 
