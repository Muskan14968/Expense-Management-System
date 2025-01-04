package com.fullStack.expenseTracker.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fullStack.expenseTracker.dto.reponses.ApiResponseDto;
import com.fullStack.expenseTracker.dto.requests.GoalSettingRequestDto;
import com.fullStack.expenseTracker.exceptions.UserNotFoundException;

@Service
public interface GoalSettingService {

    ResponseEntity<ApiResponseDto<?>> addGoal(GoalSettingRequestDto goalSettingRequestDto) 
            throws UserNotFoundException;

    ResponseEntity<ApiResponseDto<?>> updateGoal(Long id, GoalSettingRequestDto goalSettingRequestDto) 
            throws UserNotFoundException;

    ResponseEntity<ApiResponseDto<?>> deleteGoal(Long id)
            throws UserNotFoundException;

    ResponseEntity<ApiResponseDto<?>> getGoalsByUserId(String email)
            throws UserNotFoundException;
}