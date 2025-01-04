package com.fullStack.expenseTracker.handlers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.fullStack.expenseTracker.models.User;
import com.fullStack.expenseTracker.repository.UserRepository;
import com.fullStack.expenseTracker.services.NotificationService;

@EnableScheduling
@Configuration
public class SchedulerConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;


    //@Scheduled(cron = "0 */1 * * * *") // every minute
    @Scheduled(cron = "0 0 0 1 * ?") // every month
    public void sendMonthlyReports() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            try {
                notificationService.sendMonthlyReportEmail(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
