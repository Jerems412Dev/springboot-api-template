package com.jeremsdev.validations.validator;

import com.jeremsdev.validations.dto.BookDTO;
import com.jeremsdev.validations.exception.ValidationException;
import com.jeremsdev.validations.repository.BookRepository;
import org.springframework.stereotype.Component;

@Component
public class BookValidator {
    private final BookRepository bookRepository;

    public BookValidator(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Valid book DTO.
     *
     * @param bookDTO book DTO.
     */
    public void validateBookDTO(BookDTO bookDTO) {
        if (bookDTO == null) {
            throw new ValidationException("Book data cannot be null");
        }

        if (bookDTO.getCategory() == null) {
            throw new ValidationException("Category cannot be null");
        }

        validateRequiredField(bookDTO.getAuthor(),"Author");
        validateUniqueTitle(bookDTO.getTitle());
        validateNumber(bookDTO.getAvailableCopies());
        validateNumber(bookDTO.getCountPages());
    }

    /**
     * Checks whether a field is not null and not empty.
     *
     * @param field     the field to be checked.
     * @param fieldName Name field.
     */
    private void validateRequiredField(String field, String fieldName) {
        if (field == null || field.isBlank()) {
            throw new ValidationException(fieldName + " cannot be null or empty");
        }
    }

    /**
     * Check if the book title is unique.
     *
     * @param title title of book.
     */
    private void validateUniqueTitle(String title) {
        if (bookRepository.existsBookByTitle(title)) {
            throw new ValidationException("Book with title '" + title + "' already exists");
        }
    }

    /**
     * Check if number is < zero.
     *
     * @param number number.
     */
    private void validateNumber(int number) {
        if (number < 0) {
            throw new ValidationException("Cannot be < zero !");
        }
    }
}
