package com.jeremsdev.validations.validator;

import com.jeremsdev.validations.dto.UserDTO;
import com.jeremsdev.validations.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserValidator {
    private final UserValidatorHelper userValidatorHelper;

    public UserValidator(UserValidatorHelper userValidatorHelper) {
        this.userValidatorHelper = userValidatorHelper;
    }

    public void validateUserDTO(UserDTO userDTO) {
        if (userDTO == null) {
            throw new ValidationException("User data cannot be null");
        }

        validateRequiredField(userDTO.getName(), "Name");
        validateRequiredField(userDTO.getPhoneNumber(), "Phone Number");

        this.validateEmailFormat(userDTO.getEmail());
        userValidatorHelper.validateEmailUniqueness(userDTO.getEmail());
    }

    // Generic method to check if a field is null, empty or blank
    public void validateRequiredField(String field, String fieldName) {
        if (field == null || field.isEmpty()) {
            throw new ValidationException(fieldName + " cannot be null or empty");
        }
    }

    // Function to validate email format using a regular expression
    public void validateEmailFormat(String email) {
        this.validateRequiredField(email, "E-mail");

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new ValidationException("Invalid email format");
        }
    }
}
