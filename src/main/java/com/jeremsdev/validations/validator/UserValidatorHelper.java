package com.jeremsdev.validations.validator;

import com.jeremsdev.validations.exception.ValidationException;
import com.jeremsdev.validations.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserValidatorHelper {
    private final UserRepository userRepository;

    public UserValidatorHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Validates if an email already exists in the database.
     *
     * @param email email to check.
     * @throws ValidationException if email exists.
     */
    public void validateEmailUniqueness(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ValidationException(email + " already exists !");
        }
    }
}
