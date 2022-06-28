package com.beTheDonor.controller.requestbody;


import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class RegistrationRequest {

    public RegistrationRequest()
    {

    }

    //We need to capture these things

    @NotEmpty(message = "user first name can not be empty")
    @Size(min = 3,max = 70)
    private String firstName;
    @NotEmpty(message = "user last name can not be empty")
    @Size(min = 3,max = 70)
    private String lastName;
    @NotEmpty(message = "user email can not be empty")
    @Email(message = "Please Provide a valid Gmail")
    @Pattern(regexp = "^[a-zA-Z][-_.a-zA-Z0-9]{5,29}@g(oogle)?mail.com$",message = " Please enter valid gmail (google mail address)")
    private String email;
    @NotNull(message = "Please fill the phone number field")
    @Size(min = 9,max = 10)
    private String phone_number;
    private String application_user_role;
    @NotEmpty(message = "Password can not be empty")
    private String password;
    @NotEmpty(message = "Confirm Password can not be empty")
    private String confirm_password;

}
