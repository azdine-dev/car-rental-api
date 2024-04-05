package com.nbm.carrental.event;

import com.nbm.carrental.entity.User;
import com.nbm.carrental.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final UserService userService;
    private final JavaMailSender mailSender;
    private final TaskExecutor taskExecutor; // TaskExecutor for asynchronous email sending
    private User theUser;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // 1. Get the newly registered user
        theUser = event.getUser();

        // Check if user is null (just to be safe)
        if (theUser == null) {
            log.error("Null user received in RegistrationCompleteEvent. Email not sent.");
            return;
        }
        if(theUser!=null && theUser.getEnabled()){
            log.error("User Already Verified");
            return;
        }

        // 2. Create a verification token for the user
        String verificationToken = UUID.randomUUID().toString();

        // 3. Save the verification token for the user
        userService.saveUserVerificationToken(theUser, verificationToken);

        // 4. Build the verification URL to be sent to the user
        String url = event.getApplicationUrl() + "/auth/verifyEmail?token=" + verificationToken;

        // 5. Send the email asynchronously
        taskExecutor.execute(() -> {
            try {
                sendVerificationEmail(url);
                log.info("Verification email sent to: {}", theUser.getEmail());
            } catch (MessagingException | UnsupportedEncodingException e) {
                log.error("Failed to send verification email to {}: {}", theUser.getEmail(), e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification Mobilia";
        String senderName = "Mobilia Support Team";
        String mailContent = "<p> Hi, "+ theUser.getUsername()+ ", </p>"+
                "<p>Thank you for registering with us,"+"" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you <br> Users Registration Portal Service</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
        messageHelper.setFrom("omnicar2024@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}
