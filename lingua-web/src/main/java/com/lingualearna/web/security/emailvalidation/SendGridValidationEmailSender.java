package com.lingualearna.web.security.emailvalidation;

import static com.lingualearna.web.signup.SignupController.EMAIL_VALIDATION_KEY_PARAM;
import static com.lingualearna.web.signup.SignupController.VERIFY_URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.lingualearna.web.security.users.AppUser;
import com.lingualearna.web.shared.exceptions.UnexpectedProblemException;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;

@Component
public class SendGridValidationEmailSender implements ValidationEmailSender {

    private static final Logger LOG = LoggerFactory.getLogger(SendGridValidationEmailSender.class);

    private static final String SUBJECT = "Please verify your email address";
    private static final String LINK_TEXT = "Click here to verify your email address";
    private static final String LINK_SUB = "email_verification_link";
    private static final String LINK_HREF_PLACEHOLDER = "$href";
    private static final String LINK_TEXT_PLACEHOLDER = "$text";
    private static final String HTML_LINK_TEMPLATE = "<strong><a href='$href'>$text</a></strong>";
    private static final String SENDGRID_SUCCESS_CODE = "4";

    @Value("${email.sendgrid.username}")
    private String sendgridUsername;

    @Value("${email.sendgrid.password}")
    private String sendgridPassword;

    @Value("${email.fromAddress}")
    private String fromAddress;

    @Value("${email.validation.sendgridTemplateId}")
    private String sendGridTemplateId;

    @Value("${environment.appRoot}")
    private String applicationRoot;

    private boolean errorSendingMessages(SendGrid.Response response) {

        String responseCode = Integer.toString(response.getCode());

        return !responseCode.startsWith(SENDGRID_SUCCESS_CODE);
    }

    @Async
    @Override
    public void send(AppUser user) {

        SendGrid.Email email = new SendGrid.Email();

        setupEmail(user, email);
        sendEmail(email);
    }

    private void sendEmail(SendGrid.Email email) {

        SendGrid sendgrid = new SendGrid(sendgridUsername, sendgridPassword);

        try {
            SendGrid.Response response = sendgrid.send(email);

            if (errorSendingMessages(response)) {

                String errorMessage = "Exception sending verification email."
                        + "  Response was:\n"
                        + "    Message: " + response.getMessage() + "\n"
                        + "    Code:    " + response.getCode() + "\n"
                        + "    Status:  " + response.getStatus();

                LOG.error(errorMessage);
                throw new UnexpectedProblemException(errorMessage);
            }
        }
        catch (SendGridException sendgridException) {
            // Sendgrid library never actually throws this exception
        }
    }

    private void setupEmail(AppUser user, SendGrid.Email email) {

        String emailValidationUrl =
                (applicationRoot + VERIFY_URL)
                        .replace(EMAIL_VALIDATION_KEY_PARAM, user.getEmailValidationKey());

        String emailValidationLink =
                HTML_LINK_TEMPLATE
                        .replace(LINK_HREF_PLACEHOLDER, emailValidationUrl)
                        .replace(LINK_TEXT_PLACEHOLDER, LINK_TEXT);

        email
                .addTo(user.getEmailAddress())
                .setFrom(fromAddress)
                .setSubject(SUBJECT)
                .setHtml(LINK_SUB)
                .addFilter("templates", "enabled", "1")
                .addFilter("templates", "template_id", sendGridTemplateId)
                .addSubstitution(LINK_SUB, new String[] { emailValidationLink });
    }
}
