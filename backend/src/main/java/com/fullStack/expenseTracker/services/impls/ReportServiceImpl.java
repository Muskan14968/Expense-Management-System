package com.fullStack.expenseTracker.services.impls;

import com.fullStack.expenseTracker.dto.reponses.ApiResponseDto;
import com.fullStack.expenseTracker.dto.reponses.MonthlyTransactionDto;
import com.fullStack.expenseTracker.enums.ApiResponseStatus;
import com.fullStack.expenseTracker.models.GoalSetting;
import com.fullStack.expenseTracker.models.User;
import com.fullStack.expenseTracker.dto.reponses.TransactionsMonthlySummaryDto;
import com.fullStack.expenseTracker.repository.GoalSettingRepository;
import com.fullStack.expenseTracker.repository.TransactionRepository;
import com.fullStack.expenseTracker.services.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Slf4j
public class ReportServiceImpl implements ReportService {

        @Autowired
        TransactionRepository transactionRepository;

        @Autowired
        private GoalSettingRepository goalSettingRepository;

        @Override
        public ResponseEntity<ApiResponseDto<?>> getTotalByTransactionTypeAndUser(Long userId, int transactionTypeId,
                        int month, int year) {
                return ResponseEntity.status(HttpStatus.OK).body(
                                new ApiResponseDto<>(ApiResponseStatus.SUCCESS,
                                                HttpStatus.OK,
                                                transactionRepository.findTotalByUserAndTransactionType(userId,
                                                                transactionTypeId, month, year)));
        }

        @Override
        public ResponseEntity<ApiResponseDto<?>> getTotalNoOfTransactionsByUser(Long userId, int month, int year) {
                return ResponseEntity.status(HttpStatus.OK).body(
                                new ApiResponseDto<>(ApiResponseStatus.SUCCESS,
                                                HttpStatus.OK,
                                                transactionRepository.findTotalNoOfTransactionsByUser(userId, month,
                                                                year)));
        }

        @Override
        public ResponseEntity<ApiResponseDto<?>> getTotalExpenseByCategoryAndUser(String email, int categoryId,
                        int month, int year) {
                return ResponseEntity.status(HttpStatus.OK).body(
                                new ApiResponseDto<>(ApiResponseStatus.SUCCESS,
                                                HttpStatus.OK,
                                                transactionRepository.findTotalByUserAndCategory(email, categoryId,
                                                                month, year)));
        }

        @Override
        public ResponseEntity<ApiResponseDto<?>> getMonthlySummaryByUser(String email) {
                List<Object[]> result = transactionRepository.findMonthlySummaryByUser(email);

                List<TransactionsMonthlySummaryDto> transactionsMonthlySummary = result.stream()
                                .map(data -> new TransactionsMonthlySummaryDto(
                                                (int) data[0],
                                                (double) data[1],
                                                (double) data[2]))
                                .toList();

                return ResponseEntity.status(HttpStatus.OK).body(
                                new ApiResponseDto<>(ApiResponseStatus.SUCCESS,
                                                HttpStatus.OK,
                                                transactionsMonthlySummary));
        }

        @Override
        public ResponseEntity<ApiResponseDto<?>> findMonthlyTransactionsByUser(String email, int month) {
                List<Object[]> result = transactionRepository.findMonthlyTransactionsByUser(email, month);
                List<MonthlyTransactionDto> monthlyTransactions = result.stream()
                                .map(data -> new MonthlyTransactionDto(
                                                (Date) data[0],
                                                (String) data[1],
                                                (String) data[2],
                                                (double) data[3]))
                                .toList();
                return ResponseEntity.status(HttpStatus.OK).body(
                                new ApiResponseDto<>(ApiResponseStatus.SUCCESS,
                                                HttpStatus.OK,
                                                monthlyTransactions));

        }

        @Override
        public String generateMonthlyReport(User user) {
                int lastMonth = LocalDate.now().minusMonths(1).getMonthValue();
                String email = user.getEmail();
                List<Object[]> transactions = transactionRepository.findMonthlyTransactionsByUser(email, lastMonth);
                List<GoalSetting> goals = goalSettingRepository.getGoalsByUserId(email);

                return buildEmailContent(user, transactions, goals);
        }

        private String buildEmailContent(User user, List<Object[]> transactions, List<GoalSetting> goals) {
                StringBuilder content = new StringBuilder();

                content.append("<html><head><style>")
                                .append("body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f8f9fa; color: #333; }")
                                .append(".email-container { max-width: 600px; margin: 0 auto; padding: 20px; background: white; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); }")
                                .append(".header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }")
                                .append(".content { padding: 20px; }")
                                .append("</style></head><body><div class='email-container'>")
                                .append("<div class='header'><h1>Monthly Report</h1></div>")
                                .append("<div class='content'><p>Dear " + user.getUsername() + ",</p>")
                                .append("<p>Here is your monthly report for "
                                                + LocalDate.now().minusMonths(1).getMonth().name() + ":</p>");

                content.append("<h3>Transactions</h3>");
                if (transactions.isEmpty()) {
                        content.append("<p>No transactions recorded.</p>");
                } else {
                        content.append("<table style='width: 100%; border-collapse: collapse;'>")
                                        .append("<tr style='background-color: #f4f4f4;'><th>Date</th><th>Description</th><th>Category</th><th>Amount</th></tr>");
                        for (Object[] transaction : transactions) {
                                LocalDate date = ((java.sql.Date) transaction[0]).toLocalDate();
                                String description = (String) transaction[1];
                                String categoryName = (String) transaction[2];
                                Double amount = (Double) transaction[3];

                                content.append("<tr>")
                                                .append("<td style='border: 1px solid #ddd; padding: 8px;'>"
                                                                + date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                                                                + "</td>")
                                                .append("<td style='border: 1px solid #ddd; padding: 8px;'>"
                                                                + description + "</td>")
                                                .append("<td style='border: 1px solid #ddd; padding: 8px;'>"
                                                                + categoryName + "</td>")
                                                .append("<td style='border: 1px solid #ddd; padding: 8px;'>" + amount
                                                                + "</td>")
                                                .append("</tr>");
                        }
                        content.append("</table>");
                }

                content.append("<h3>Goals</h3>");
                if (goals.isEmpty()) {
                        content.append("<p style='font-size: 16px; color: #666;'>No goals completed this month.</p>");
                } else {
                        content.append("<table style='width: 100%; border-collapse: collapse; margin: 20px 0;'>")
                                        .append("<thead>")
                                        .append("<tr style='background-color: #f4f4f9; text-align: left;'>")
                                        .append("<th style='padding: 10px; border: 1px solid #ddd;'>Goal Name</th>")
                                        .append("<th style='padding: 10px; border: 1px solid #ddd;'>Target Amount</th>")
                                        .append("<th style='padding: 10px; border: 1px solid #ddd;'>Completion Date</th>")
                                        .append("<th style='padding: 10px; border: 1px solid #ddd;'>Target Date</th>")
                                        .append("<th style='padding: 10px; border: 1px solid #ddd;'>Progress</th>")
                                        .append("</tr>")
                                        .append("</thead>")
                                        .append("<tbody>");
                        for (GoalSetting goal : goals) {
                                double progress = (goal.getAmountSaved() / goal.getPrice()) * 100;
                                content.append("<tr style='border-bottom: 1px solid #ddd;'>")
                                                .append("<td style='padding: 10px; border: 1px solid #ddd;'>"
                                                                + goal.getName() + "</td>")
                                                .append("<td style='padding: 10px; border: 1px solid #ddd;'>₹"
                                                                + goal.getPrice() + "</td>")
                                                .append("<td style='padding: 10px; border: 1px solid #ddd;'>"
                                                                + goal.getAmountSaved() + "</td>")
                                                .append("<td style='padding: 10px; border: 1px solid #ddd;'>"
                                                                + goal.getTargetDate() + "</td>")
                                                .append("<td style='padding: 10px; border: 1px solid #ddd;'>"
                                                                + String.format("%.2f", progress) + "%</td>")
                                                .append("</tr>");
                        }
                        content.append("</tbody></table>");
                }

                content.append("<p style='font-size: 16px; margin-top: 20px;'>Thank you for using Expense Tracker!</p>")
                                .append("</div></body></html>");
                return content.toString();

        }
}
