package com.fullStack.expenseTracker.services;

import com.fullStack.expenseTracker.models.GoalSetting;
import com.fullStack.expenseTracker.models.Transaction;
import com.fullStack.expenseTracker.models.User;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public interface NotificationService {

    void sendUserRegistrationVerificationEmail(User user) throws MessagingException, UnsupportedEncodingException;

    void sendForgotPasswordVerificationEmail(User user) throws MessagingException, UnsupportedEncodingException;

    void sendNewTransansactionNotificationEmail(User user, Transaction transaction) throws MessagingException, UnsupportedEncodingException;

    void sendNewGoalNotificationEmail(User user, GoalSetting goalSetting) throws MessagingException, UnsupportedEncodingException;
    
    void sendGoalCompletionNotificationEmail(User user, GoalSetting goalSetting) throws MessagingException, UnsupportedEncodingException;

    void sendMonthlyReportEmail(User user) throws MessagingException, UnsupportedEncodingException;

}
