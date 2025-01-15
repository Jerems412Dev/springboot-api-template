package com.jeremsdev.validations.validator;

import com.jeremsdev.validations.dto.LoanDTO;
import com.jeremsdev.validations.exception.ValidationException;
import com.jeremsdev.validations.repository.BookRepository;
import com.jeremsdev.validations.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class LoanValidator {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public LoanValidator(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }


    /**
     * Valid loan DTO.
     *
     * @param loanDTO loan DTO.
     */
    public void validateLoanDTO(LoanDTO loanDTO) {
        if (loanDTO == null) {
            throw new ValidationException("Loan data cannot be null");
        }

        validateRequiredField(loanDTO.getLoanDate(),"LoanDate");
        validateRequiredField(loanDTO.getReturnDate(),"ReturnDate");
        validateRequiredField(loanDTO.getIdBook(),"idBook");
        validateRequiredField(loanDTO.getIdUser(),"idUser");
        validateUniqueIdUser(loanDTO.getIdUser());
        validateUniqueIdBook(loanDTO.getIdBook());
    }

    /**
     * Checks whether a field is not null and not empty.
     *
     * @param field     the field to be checked.
     * @param fieldName Name field.
     */
    private void validateRequiredField(Object field, String fieldName) {
        if (field == null) {
            throw new ValidationException(fieldName + " cannot be null or empty");
        }

        if (field instanceof String) {
            if (field.toString().isBlank()) {
                throw new ValidationException(fieldName + " cannot be null or empty");
            }
        }
    }

    /**
     * Check if the book id exist.
     *
     * @param idBook id of book.
     */
    private void validateUniqueIdBook(Long idBook) {
        if (!bookRepository.existsUserByIdBook(idBook)) {
            throw new ValidationException("Book with this id  doesn't exists");
        }
    }

    /**
     * Check if the user id exist.
     *
     * @param idUser id of user.
     */
    private void validateUniqueIdUser(Long idUser) {
        if (!userRepository.existsByIdUser(idUser)) {
            throw new ValidationException("User with this id doesn't exists");
        }
    }
}
