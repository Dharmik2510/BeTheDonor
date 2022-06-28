package com.beTheDonor.validator;


import com.beTheDonor.controller.requestbody.RegistrationRequest;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class BodyValidator implements Validator
{

    @Override
    public boolean validate(Object o) {
        RegistrationRequest registrationRequest = (RegistrationRequest)o;
        Field[] fields = registrationRequest.getClass().getFields();
        for(Field f:fields)
        {
            try {
                Object value = f.get(registrationRequest);
                if(value.equals(""))
                {
                    return false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
