package com.beTheDonor.service;


import com.beTheDonor.controller.requestbody.RegistrationRequest;
import com.beTheDonor.entity.ApplicationUser;
import com.beTheDonor.entity.UserConfirmationToken;
import com.beTheDonor.validator.BodyValidator;
import com.beTheDonor.validator.EmailValidator;
import com.beTheDonor.email.EmailSender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RegistrationService {


    private EmailValidator emailValidator;
    private BodyValidator bodyValidator;
    private final ApplicationUserService applicationUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final AnalyticsService analyticsService;


    public boolean register(RegistrationRequest registrationRequest) {
        boolean validBody = bodyValidator.validate(registrationRequest);
        if (!validBody) {
            System.out.println("Empty field found");
        }
        boolean validEmail = emailValidator.validate(registrationRequest.getEmail());
        if (!validEmail) {
            System.out.println("not a valid email");
        }

        boolean password_matched = registrationRequest.getPassword().equals(registrationRequest.getConfirm_password());
        if (!password_matched) {
            System.out.println("Password and Confirm password do not match");
        }
        String token = applicationUserService.signUpUser(new ApplicationUser(
                registrationRequest.getFirstName(),
                registrationRequest.getLastName(),
                registrationRequest.getEmail(),
                registrationRequest.getPhone_number(),
                // registrationRequest.getType_of_user(),
                registrationRequest.getPassword(),
                registrationRequest.getApplication_user_role()
        ));
        if (token.equals("emailFound")) {
            return false;
        }
        String link = "https://bethedonor-dev.herokuapp.com/api/v1/registration/confirm?token=" + token;
        System.out.println(registrationRequest.getEmail());
        emailSender.send(registrationRequest.getEmail(), buildEmail(registrationRequest.getFirstName(), link));
        System.out.println("Email sent");
        analyticsService.addUser(registrationRequest.getFirstName(), registrationRequest.getEmail(), registrationRequest.getApplication_user_role());
        return true;
    }


    @Transactional
    public String confirmToken(String token) {

        Optional<UserConfirmationToken> userConfirmationToken = confirmationTokenService.getToken(token);

        if (userConfirmationToken.isPresent()) {
            UserConfirmationToken userConfirmationTokens = userConfirmationToken.get();

            if (userConfirmationTokens.getConfirmedAt() != null) {
                System.out.println("Email already exist");
            }

            LocalDateTime expiredAt = userConfirmationTokens.getExpiresAt();

            if (expiredAt.isBefore(LocalDateTime.now())) {
                System.out.println("Token expired");
            }

            confirmationTokenService.setConfirmedAt(token);
            applicationUserService.enableApplicationUser(userConfirmationTokens.getApplicationUser().getEmail());
            analyticsService.enableUser(userConfirmationTokens.getApplicationUser().getEmail(), userConfirmationTokens.getApplicationUser().getApplicationUserRole(), userConfirmationTokens.getApplicationUser().getId());

            return "confirmation";
        } else {
            return "Token not found";
        }
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#C91616\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#C91616\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm to be a part of Be the Donor</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes." +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }


}
