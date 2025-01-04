package com.fullStack.expenseTracker.services.impls;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.fullStack.expenseTracker.dto.reponses.ApiResponseDto;
import com.fullStack.expenseTracker.dto.requests.GoalSettingRequestDto;
import com.fullStack.expenseTracker.enums.ApiResponseStatus;
import com.fullStack.expenseTracker.exceptions.UserNotFoundException;
import com.fullStack.expenseTracker.models.GoalSetting;
import com.fullStack.expenseTracker.repository.GoalSettingRepository;
import com.fullStack.expenseTracker.services.GoalSettingService;
import com.fullStack.expenseTracker.services.NotificationService;
import com.fullStack.expenseTracker.services.UserService;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GoalSettingServiceImpl implements GoalSettingService {

    @Autowired
    private GoalSettingRepository goalSettingRepository;

    @Autowired
    private UserService userService;

    @Autowired
    NotificationService notificationService;

    @Override
    public ResponseEntity<ApiResponseDto<?>> addGoal(GoalSettingRequestDto goalSettingRequestDto)
            throws UserNotFoundException {
        GoalSetting goalSetting = goalSettingRequestDtoToGoalSetting(goalSettingRequestDto);
        try {
            goalSettingRepository.save(goalSetting);
            sendNewGoalNotificationEmail(goalSetting);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ApiResponseDto<>(
                            ApiResponseStatus.SUCCESS,
                            HttpStatus.CREATED,
                            "Goal has been successfully recorded!"));
        } catch (Exception e) {
            log.error("Error happen when adding new goal: " + e.getMessage());
            throw new UserNotFoundException("Failed to record your new goal, Try again later!");
        }
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> updateGoal(Long id, GoalSettingRequestDto goalSettingRequestDto) throws UserNotFoundException {
        GoalSetting goalSetting = goalSettingRepository.findById(id).orElseThrow(
            () -> new UserNotFoundException("Goal not found with id : " + id));
        goalSetting.setName(goalSettingRequestDto.getGoalName());
        goalSetting.setPrice(goalSettingRequestDto.getPrice());
        goalSetting.setTargetDate(goalSettingRequestDto.getTargetDate());
        goalSetting.setAmountSaved(goalSettingRequestDto.getAmountSaved());
        try {
            goalSettingRepository.save(goalSetting);
            sendGoalCompletionNotification(goalSetting);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponseDto<>(
                            ApiResponseStatus.SUCCESS,
                            HttpStatus.OK,
                            "Goal has been successfully updated!"
                    )
            );
        } catch(Exception e){
            log.error("Error happen when retrieving Goal of a user: " + e.getMessage());
            throw new UserNotFoundException("Failed to update your Goal! Try again later");
        }
        
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> deleteGoal(Long id) throws UserNotFoundException {
        if (goalSettingRepository.existsById(id)) {
            try {goalSettingRepository.deleteById(id);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ApiResponseDto<>(
                                ApiResponseStatus.SUCCESS,
                                HttpStatus.OK,
                                "Goal has been successfully deleted!"
                        )
                );
            }catch(Exception e) {
                log.error("Error happen when retrieving Goals of a user: " + e.getMessage());
                throw new UserNotFoundException("Failed to delete your Goal! Try again later");
            }
        }else {
            throw new UserNotFoundException("Goal not found with id : " + id);
        }
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> getGoalsByUserId(String email) throws UserNotFoundException {

        try {
            List<GoalSetting> goals = goalSettingRepository.getGoalsByUserId(email);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponseDto<>(
                            ApiResponseStatus.SUCCESS,
                            HttpStatus.OK,
                            goals.size() == 0 ? new ArrayList<>() : goals));

        } catch (Exception e) {
            log.error("Error happen when retrieving goals of a user: " + e.getMessage());
            throw new UserNotFoundException("Failed to fetch your goals! Try again later");
        }
    }

    private GoalSetting goalSettingRequestDtoToGoalSetting(GoalSettingRequestDto goalSettingRequestDto)
            throws UserNotFoundException {
        return new GoalSetting(
                userService.findByEmail(goalSettingRequestDto.getUserEmail()),
                goalSettingRequestDto.getGoalName(),
                goalSettingRequestDto.getPrice(),
                goalSettingRequestDto.getAmountSaved(),
                goalSettingRequestDto.getTargetDate());
    }

    private void sendGoalCompletionNotification(GoalSetting goalSetting) throws UnsupportedEncodingException,MessagingException{
        if(goalSetting.getPrice() >= goalSetting.getAmountSaved()){
            try {
                notificationService.sendGoalCompletionNotificationEmail(goalSetting.getUser(), goalSetting);
            } catch (Exception e) {
                log.error("Error happen when retrieving goals of a user: " + e.getMessage());
                throw new UnsupportedEncodingException("Failed to fetch your goals! Try again later");
            }
        }
    }

    private void sendNewGoalNotificationEmail(GoalSetting goalSetting) throws UnsupportedEncodingException,MessagingException{
        try{
            notificationService.sendNewGoalNotificationEmail(goalSetting.getUser(), goalSetting);
        }
        catch (Exception e){
            log.error("Error happen when retrieving goals of a user: " + e.getMessage());
                throw new UnsupportedEncodingException("Failed to fetch your goals! Try again later");
        }
    }
}