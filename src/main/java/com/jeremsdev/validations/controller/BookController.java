package com.jeremsdev.validations.controller;

import com.jeremsdev.validations.dto.BookDTO;
import com.jeremsdev.validations.dto.LoanDTO;
import com.jeremsdev.validations.exception.ApiRequestException;
import com.jeremsdev.validations.service.BookService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;

    @PostMapping("/add")
    public ResponseEntity<BookDTO> add(@RequestBody BookDTO bookDTO) {
        logger.info("Received request to add a new Book: {}", bookDTO);
        try {
            BookDTO addedBook = bookService.add(bookDTO);
            logger.info("Book added successfully: {}", addedBook);
            return new ResponseEntity<>(addedBook, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error adding Book: {}", e.getMessage());
            throw new ApiRequestException("Error adding Book: " + e.getMessage());
        }
    }

    @PutMapping("/update/{idBook}")
    public ResponseEntity<BookDTO> update(@PathVariable Long idBook, @RequestBody BookDTO bookDTO) {
        logger.info("Received request to update Book with ID: {}", idBook);
        try {
            BookDTO updatedBook = bookService.update(idBook, bookDTO);
            logger.info("Book with ID {} updated successfully", idBook);
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error updating Book with ID {}: {}", idBook, e.getMessage());
            throw new ApiRequestException("Error updating Book with ID " + idBook + ": " + e.getMessage());
        }
    }

    @GetMapping("/getbyid/{idBook}")
    public ResponseEntity<BookDTO> getById(@PathVariable Long idBook) {
        logger.info("Received request to retrieve Book with ID: {}", idBook);
        try {
            BookDTO bookDTO = bookService.getById(idBook);
            if (bookDTO == null) {
                throw new ApiRequestException("Book with ID " + idBook + " not found");
            }
            return new ResponseEntity<>(bookDTO, HttpStatus.OK);
        } catch (ApiRequestException e) {
            logger.error("Error retrieving Book with ID {}: {}", idBook, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error retrieving Book with ID {}: {}", idBook, e.getMessage());
            throw new ApiRequestException("Error retrieving Book with ID " + idBook + ": " + e.getMessage());
        }
    }

    @GetMapping("/findall")
    public ResponseEntity<List<BookDTO>> findAll() {
        logger.info("Received request to retrieve all Books");
        try {
            List<BookDTO> books = bookService.findAll();
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving Book list: {}", e.getMessage());
            throw new ApiRequestException("Error retrieving Book list: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{idBook}")
    public ResponseEntity<Void> delete(@PathVariable Long idBook) {
        logger.info("Received request to delete Book with ID: {}", idBook);
        try {
            bookService.delete(idBook);
            logger.info("Book with ID {} deleted successfully", idBook);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Error deleting Book with ID {}: {}", idBook, e.getMessage());
            throw new ApiRequestException("Error deleting Book with ID " + idBook + ": " + e.getMessage());
        }
    }

    @GetMapping("/getloans/{idBook}")
    public ResponseEntity<List<LoanDTO>> getLoans(@PathVariable Long idBook) {
        logger.info("Received request to retrieve Loans with idBook: {}", idBook);
        try {
            List<LoanDTO> loans = bookService.getLoans(idBook);
            if (loans == null) {
                throw new ApiRequestException("Loans with IdBook " + idBook + " not found");
            }
            return new ResponseEntity<>(loans, HttpStatus.OK);
        } catch (ApiRequestException e) {
            logger.error("Error retrieving Loans with IdBook {}: {}", idBook, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error retrieving Loans with IdBook {}: {}", idBook, e.getMessage());
            throw new ApiRequestException("Error retrieving Loans with IdBook " + idBook + ": " + e.getMessage());
        }
    }

    @PutMapping("/updateavailablecopies/{idBook}/{nb}")
    public ResponseEntity<BookDTO> updateAvailableCopies(@PathVariable Long idBook, @PathVariable int nb) {
        logger.info("Received request to update available copies for book with ID: {}", idBook);
        try {
            BookDTO bookDTO = bookService.updateAvailableCopies(nb, idBook);
            logger.info("Available copies for book with ID {} updated successfully", idBook);
            return new ResponseEntity<>(bookDTO, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error updating Book with ID {}: {}", idBook, e.getMessage());
            throw new ApiRequestException("Error updating Book with ID " + idBook + ": " + e.getMessage());
        }
    }
}
