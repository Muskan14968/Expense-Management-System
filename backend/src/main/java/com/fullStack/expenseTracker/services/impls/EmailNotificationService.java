package com.fullStack.expenseTracker.services.impls;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fullStack.expenseTracker.services.NotificationService;
import com.fullStack.expenseTracker.services.ReportService;
import com.fullStack.expenseTracker.models.GoalSetting;
import com.fullStack.expenseTracker.models.Transaction;
import com.fullStack.expenseTracker.models.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationService implements NotificationService {

        @Autowired
        private JavaMailSender javaMailSender;

        @Autowired
        private ReportService reportService;

        @Value("${spring.mail.username}")
        private String fromMail;

        @Override
        public void sendUserRegistrationVerificationEmail(User user)
                        throws MessagingException, UnsupportedEncodingException, MailAuthenticationException {
                String toAddress = user.getEmail();
                String fromAddress = fromMail;
                String senderName = "Expense Tracker";
                String subject = "Please verify your registration";
                String content = "<html>" +
                                "<head>" +
                                "<style>" +
                                "    body { font-family: 'Arial', sans-serif; margin: 0; padding: 0; background-color: #f8f9fa; color: #333; }"
                                +
                                "    .email-wrapper { padding: 20px; }" +
                                "    .email-container { max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; padding: 20px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); }"
                                +
                                "    .header { text-align: center; font-size: 24px; color: #4CAF50; margin-bottom: 20px; }"
                                +
                                "    .content p { font-size: 16px; line-height: 1.6; margin: 10px 0; }" +
                                "    .verification-code { text-align: center; margin: 20px 0; font-size: 20px; font-weight: bold; color: #4CAF50; border: 1px solid #4CAF50; padding: 10px; border-radius: 5px; display: inline-block; }"
                                +
                                "    .footer { margin-top: 20px; font-size: 14px; color: #666; text-align: center; }" +
                                "</style>" +
                                "</head>" +
                                "<body>" +
                                "    <div class='email-wrapper'>" +
                                "        <div class='email-container'>" +
                                "            <div class='header'>Welcome to Expense Tracker</div>" +
                                "            <div class='content'>" +
                                "                <p>Dear <strong>" + user.getUsername() + "</strong>,</p>" +
                                "                <p>Thank you for joining us! We are thrilled to have you on board.</p>"
                                +
                                "                <p>To complete the sign-up process, please enter the verification code below on your device:</p>"
                                +
                                "                <div class='verification-code'>" + user.getVerificationCode()
                                + "</div>" +
                                "                <p><strong>Please note:</strong> The verification code will expire in 15 minutes.</p>"
                                +
                                "            </div>" +
                                "            <div class='footer'>" +
                                "                <p>Thank you,</p>" +
                                "                <p><strong>Expense Tracker Team</strong></p>" +
                                "            </div>" +
                                "        </div>" +
                                "    </div>" +
                                "</body>" +
                                "</html>";

                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message);

                helper.setFrom(fromAddress, senderName);
                helper.setTo(toAddress);
                helper.setSubject(subject);

                helper.setText(content, true);
                System.out.println(helper.toString());
                javaMailSender.send(message);

        }

        public void sendForgotPasswordVerificationEmail(User user)
                        throws MessagingException, UnsupportedEncodingException {
                String toAddress = user.getEmail();
                String fromAddress = fromMail;
                String senderName = "Expense Tracker";
                String subject = "Forgot password - Please verify your Account";
                String content = "<html>" +
                                "<head>" +
                                "<style>" +
                                "    body { font-family: 'Arial', sans-serif; margin: 0; padding: 0; background-color: #f8f9fa; color: #333; }"
                                +
                                "    .email-wrapper { padding: 20px; }" +
                                "    .email-container { max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; padding: 20px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); }"
                                +
                                "    .header { text-align: center; font-size: 24px; color: #4CAF50; margin-bottom: 20px; }"
                                +
                                "    .content p { font-size: 16px; line-height: 1.6; margin: 10px 0; }" +
                                "    .verification-code { text-align: center; margin: 20px 0; font-size: 20px; font-weight: bold; color: #4CAF50; border: 1px solid #4CAF50; padding: 10px; border-radius: 5px; display: inline-block; }"
                                +
                                "    .footer { margin-top: 20px; font-size: 14px; color: #666; text-align: center; }" +
                                "</style>" +
                                "</head>" +
                                "<body>" +
                                "    <div class='email-wrapper'>" +
                                "        <div class='email-container'>" +
                                "            <div class='header'>Password Change Request</div>" +
                                "            <div class='content'>" +
                                "                <p>Dear <strong>" + user.getUsername() + "</strong>,</p>" +
                                "                <p>To change your password, please enter the verification code below on your device:</p>"
                                +
                                "                <div class='verification-code'>" + user.getVerificationCode()
                                + "</div>" +
                                "                <p><strong>Please note:</strong> The verification code will expire in 15 minutes.</p>"
                                +
                                "            </div>" +
                                "            <div class='footer'>" +
                                "                <p>Thank you,</p>" +
                                "                <p><strong>Expense Tracker Team</strong></p>" +
                                "            </div>" +
                                "        </div>" +
                                "    </div>" +
                                "</body>" +
                                "</html>";

                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message);

                helper.setFrom(fromAddress, senderName);
                helper.setTo(toAddress);
                helper.setSubject(subject);

                helper.setText(content, true);

                javaMailSender.send(message);
        }

        @Override
        public void sendNewTransansactionNotificationEmail(User user, Transaction transaction)
                        throws MessagingException, UnsupportedEncodingException {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String formattedDate = transaction.getDate().format(dtf);
                String toAddress = user.getEmail();
                String fromAddress = fromMail;
                String senderName = "Expense Tracker";
                String subject = "New Transaction Added";
                String content = "<html>" +
                                "<head>" +
                                "<style>" +
                                "    body { font-family: 'Arial', sans-serif; margin: 0; padding: 0; background-color: #f8f9fa; color: #333; }"
                                +
                                "    .email-wrapper { width: 100%; background-color: #f8f9fa; padding: 20px; }" +
                                "    .email-container { max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); }"
                                +
                                "    .header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }"
                                +
                                "    .header h1 { margin: 0; font-size: 24px; }" +
                                "    .content { padding: 20px; }" +
                                "    .content p { margin: 10px 0; font-size: 16px; line-height: 1.6; }" +
                                "    .transaction-details { background-color: #f4f4f9; padding: 15px; border-radius: 8px; margin: 20px 0; }"
                                +
                                "    .transaction-details p { margin: 5px 0; font-size: 15px; }" +
                                "    .transaction-details p strong { color: #4CAF50; }" +
                                "    .cta-button { display: inline-block; background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; margin-top: 20px; font-size: 16px; }"
                                +
                                "    .cta-button:hover { background-color: #45a049; }" +
                                "    .footer { text-align: center; padding: 15px; font-size: 14px; color: #666; border-top: 1px solid #eaeaea; }"
                                +
                                "</style>" +
                                "</head>" +
                                "<body>" +
                                "    <div class='email-wrapper'>" +
                                "        <div class='email-container'>" +
                                "            <div class='header'>" +
                                "                <h1>New Transaction Added</h1>" +
                                "            </div>" +
                                "            <div class='content'>" +
                                "                <p>Dear <strong>" + user.getUsername() + "</strong>,</p>" +
                                "                <p>We're notifying you that a new transaction has been recorded in your account:</p>"
                                +
                                "                <div class='transaction-details'>" +
                                "                    <p><strong>Category:</strong> "
                                + transaction.getCategory().getCategoryName() + "</p>" +
                                "                    <p><strong>Description:</strong> " + transaction.getDescription()
                                + "</p>" +
                                "                    <p><strong>Amount:</strong> " + transaction.getAmount() + "</p>" +
                                "                    <p><strong>Transaction Date:</strong> " + formattedDate + "</p>" +
                                "            </div>" +
                                "                <p>If this transaction seems unfamiliar, please contact us immediately.</p>"
                                +
                                "                <a href='http://www.expensetracker.com/contact-us' class='cta-button'>Contact Support</a>"
                                +
                                "            </div>" +
                                "            <div class='footer'>" +
                                "                <p>Thank you for using Expense Tracker!</p>" +
                                "                <p>&copy; " + java.time.Year.now()
                                + " Expense Tracker. All rights reserved.</p>" +
                                "            </div>" +
                                "        </div>" +
                                "    </div>" +
                                "</body>" +
                                "</html>";

                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message);

                helper.setFrom(fromAddress, senderName);
                helper.setTo(toAddress);
                helper.setSubject(subject);

                helper.setText(content, true);

                javaMailSender.send(message);
        }

        @Override
        public void sendNewGoalNotificationEmail(User user, GoalSetting goalSetting)
                        throws MessagingException, UnsupportedEncodingException {
                String toAddress = user.getEmail();
                String fromAddress = fromMail;
                String senderName = "Expense Tracker";
                String subject = "New Goal Added!";

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

                String formattedTargetDate = goalSetting.getTargetDate().format(dateFormatter);
                String formattedCompletionDate = dateTimeFormatter.format(LocalDateTime.now());
                String formattedTargetAmount = String.valueOf(goalSetting.getPrice());
                String formattedAmountSaved = String.valueOf(goalSetting.getAmountSaved());

                String content = "<html>" 
                                +"<head>" 
                                +"<style>" 
                                + "    body { font-family: 'Arial', sans-serif; margin: 0; padding: 0; background-color: #f8f9fa; color: #333; }" 
                                + "    .email-wrapper { width: 100%; background-color: #f8f9fa; padding: 20px; }" +
                                "    .email-container { max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); }"
                                +
                                "    .header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }"
                                +
                                "    .header h1 { margin: 0; font-size: 24px; }" +
                                "    .content { padding: 20px; }" +
                                "    .content p { margin: 10px 0; font-size: 16px; line-height: 1.6; }" +
                                "    .goal-details { background-color: #f4f4f9; padding: 15px; border-radius: 8px; margin: 20px 0; }"
                                +
                                "    .goal-details p { margin: 5px 0; font-size: 15px; }" +
                                "    .goal-details p strong { color: #4CAF50; }" +
                                "    .cta-button { display: inline-block; background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; margin-top: 20px; font-size: 16px; }"
                                +
                                "    .footer { text-align: center; padding: 15px; font-size: 14px; color: #666; border-top: 1px solid #eaeaea; }"
                                +
                                "</style>" +
                                "</head>" +
                                "<body>" +
                                "    <div class='email-wrapper'>" +
                                "        <div class='email-container'>" +
                                "            <div class='header'>" +
                                "                <h1>New Goal Added!</h1>" +
                                "            </div>" +
                                "            <div class='content'>" +
                                "                <p>Dear <strong>" + user.getUsername() + "</strong>,</p>" +
                                "                <p>We're notifying you that a new goal has been recorded in your account:</p>"
                                +
                                "                <div class='goal-details'>" +
                                "                    <p><strong>Goal Name:</strong> " + goalSetting.getName() + "</p>" +
                                "                    <p><strong>Target Amount:</strong> " + formattedTargetAmount
                                + "</p>" +
                                "                    <p><strong>Amount Saved:</strong> " + formattedAmountSaved + "</p>"
                                +
                                "                    <p><strong>Target Date:</strong> " + formattedTargetDate + "</p>" +
                                "                    <p><strong>Completion Date:</strong> " + formattedCompletionDate + "</p>" +
                                "                </div>" +
                                "                <a href='http://localhost:3000/user/goalSetting' class='cta-button'>Set a New Goal</a>"
                                +
                                "            </div>" +
                                "            <div class='footer'>" +
                                "                <p>Keep up the great work!</p>" +
                                "                <p>&copy; " + java.time.Year.now()
                                + " Expense Tracker. All rights reserved.</p>" +
                                "            </div>" +
                                "        </div>" +
                                "    </div>" +
                                "</body>" +
                                "</html>";

                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message);

                helper.setFrom(fromAddress, senderName);
                helper.setTo(toAddress);
                helper.setSubject(subject);

                helper.setText(content, true);

                javaMailSender.send(message);
        }

        @Override
        public void sendGoalCompletionNotificationEmail(User user, GoalSetting goalSetting)
                        throws MessagingException, UnsupportedEncodingException {

                if (goalSetting.getAmountSaved() < goalSetting.getPrice()) {
                        return;
                }

                String toAddress = user.getEmail();
                String fromAddress = fromMail;
                String senderName = "Expense Tracker";
                String subject = "Congratulations! You have achieved a goal.";
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();

                String content = "<html>"
                                + "<head>"
                                + "<style>"
                                + "body { font-family: 'Arial', sans-serif; margin: 0; padding: 0; background-color: #f8f9fa; color: #333; }"
                                + ".email-wrapper { width: 100%; background-color: #f8f9fa; padding: 20px; }"
                                + ".email-container { max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); }"
                                + ".header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }"
                                + ".header h1 { margin: 0; font-size: 24px; }"
                                + ".content { padding: 20px; }"
                                + ".content p { margin: 10px 0; font-size: 16px; line-height: 1.6; }"
                                + ".goal-details { background-color: #f4f4f9; padding: 15px; border-radius: 8px; margin: 20px 0; }"
                                + ".goal-details p { margin: 5px 0; font-size: 15px; }"
                                + ".goal-details p strong { color: #4CAF50; }"
                                + ".cta-button { display: inline-block; background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; margin-top: 20px; font-size: 16px; }"
                                + ".footer { text-align: center; padding: 15px; font-size: 14px; color: #666; border-top: 1px solid #eaeaea; }"
                                + "</style>"
                                + "</head>"
                                + "<body>"
                                + "<div class='email-wrapper'>"
                                + "<div class='email-container'>"
                                + "<div class='header'>"
                                + "<h1>Goal Achievement Notification</h1>"
                                + "</div>"
                                + "<div class='content'>"
                                + "<p>Dear <strong>" + user.getUsername() + "</strong>,</p>"
                                + "<p>We are thrilled to share that you have successfully achieved your goal!</p>"
                                + "<div class='goal-details'>"
                                + "<p><strong>Goal Name:</strong> " + goalSetting.getName() + "</p>"
                                + "<p><strong>Target Amount:</strong> " + goalSetting.getPrice() + "</p>"
                                + "<p><strong>Amount Saved:</strong> " + goalSetting.getAmountSaved() + "</p>"
                                + "<p><strong>Target Date:</strong> " + goalSetting.getTargetDate() + "</p>"
                                + "<p><strong>Completion Date:</strong> " + dtf.format(now) + "</p>"
                                + "</div>"
                                + "<p>This is a significant milestone, and we're so proud of your progress.</p>"
                                + "<a href='http://localhost:3000/user/goalSetting' class='cta-button'>Set a New Goal</a>"
                                + "</div>"
                                + "<div class='footer'>"
                                + "<p>Keep up the great work!</p>"
                                + "<p>&copy; " + java.time.Year.now() + " Expense Tracker. All rights reserved.</p>"
                                + "</div>"
                                + "</div>"
                                + "</div>"
                                + "</body>"
                                + "</html>";

                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message);

                helper.setFrom(fromAddress, senderName);
                helper.setTo(toAddress);
                helper.setSubject(subject);
                helper.setText(content, true);

                javaMailSender.send(message);
        }

        @Override
        public void sendMonthlyReportEmail(User user) throws MessagingException, UnsupportedEncodingException {
                String fromAddress = fromMail;
                String senderName = "Expense Tracker";
                String subject = "Your Monthly Expense Tracker Report";
                String content = reportService.generateMonthlyReport(user);

                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message);

                helper.setFrom(fromAddress, senderName);
                helper.setTo(user.getEmail());
                helper.setSubject(subject);
                helper.setText(content, true);

                javaMailSender.send(message);
        }
}
