package com.beTheDonor.validator;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailValidator implements Predicate<String>,Validator {

    @Override
    public boolean validate(Object o) {
        String email = (String)o;
        return test(email);

    }

    @Override
    public boolean test(String email) {
        Pattern pattern = Pattern.compile("^[a-zA-Z][-_.a-zA-Z0-9]{5,29}@g(oogle)?mail.com$");

        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
