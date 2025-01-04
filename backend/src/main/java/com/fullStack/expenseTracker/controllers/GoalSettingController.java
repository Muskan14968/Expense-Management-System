package com.fullStack.expenseTracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fullStack.expenseTracker.dto.reponses.ApiResponseDto;
import com.fullStack.expenseTracker.dto.requests.GoalSettingRequestDto;
import com.fullStack.expenseTracker.exceptions.UserNotFoundException;
import com.fullStack.expenseTracker.services.GoalSettingService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/expensetracker/goals")
public class GoalSettingController {

    @Autowired
    private GoalSettingService goalSettingService;

    @PostMapping("/addGoal")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponseDto<?>> addGoal(@RequestBody @Valid GoalSettingRequestDto goalSettingRequestDto) throws UserNotFoundException {
        return goalSettingService.addGoal(goalSettingRequestDto);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponseDto<?>> updateGoal(@Param("id") Long id, @RequestBody @Valid GoalSettingRequestDto goalSettingRequestDto) throws UserNotFoundException {
        return goalSettingService.updateGoal(id, goalSettingRequestDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponseDto<?>> deleteGoal(@Param("id") Long id) throws UserNotFoundException {
        return goalSettingService.deleteGoal(id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponseDto<?>> getAllGoals(@Param("email") String email) throws UserNotFoundException {
        return goalSettingService.getGoalsByUserId(email);
    }
}