package com.jeremsdev.validations.service.impl;

import com.jeremsdev.validations.dto.BookDTO;
import com.jeremsdev.validations.dto.LoanDTO;
import com.jeremsdev.validations.exception.ResourceNotFoundException;
import com.jeremsdev.validations.exception.ValidationException;
import com.jeremsdev.validations.mapper.BookMapper;
import com.jeremsdev.validations.mapper.LoanMapper;
import com.jeremsdev.validations.model.Book;
import com.jeremsdev.validations.model.Loan;
import com.jeremsdev.validations.repository.BookRepository;
import com.jeremsdev.validations.repository.LoanRepository;
import com.jeremsdev.validations.service.BookService;
import com.jeremsdev.validations.validator.BookValidator;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    private final BookValidator bookValidator;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final BookMapper bookMapper;
    private final LoanMapper loanMapper;

    public BookServiceImpl(BookValidator bookValidator, BookRepository bookRepository, LoanRepository loanRepository, BookMapper bookMapper, LoanMapper loanMapper) {
        this.bookValidator = bookValidator;
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
        this.bookMapper = bookMapper.INSTANCE;
        this.loanMapper = loanMapper.INSTANCE;
    }

    @Override
    public BookDTO add(BookDTO bookDTO) {
        logger.info("Adding a new book with title: {}", bookDTO.getTitle());

        bookValidator.validateBookDTO(bookDTO);

        Book book = bookMapper.toEntity(bookDTO);
        book = bookRepository.save(book);

        logger.info("Book added successfully with ID: {}", book.getIdBook());
        return bookMapper.toDTO(book);
    }

    @Override
    public BookDTO update(Long idBook, BookDTO bookDTO) {
        logger.info("Updating book with ID: {}", idBook);
        bookValidator.validateBookDTO(bookDTO);

        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> {
                    logBookNotFound(idBook);
                    return new ResourceNotFoundException("Book with ID: " + idBook + " not found");
                });

        bookMapper.updateEntityFromDTO(bookDTO, book);

        logger.info("Book updated successfully with ID: {}", idBook);
        return bookMapper.toDTO(bookRepository.save(book));
    }

    @Override
    public BookDTO getById(Long idBook) {
        logger.info("Finding book with ID: {}", idBook);

        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> {
                    logBookNotFound(idBook);
                    return new ResourceNotFoundException("Book with ID: " + idBook + " not found");
                });

        logger.info("Book with ID: {} found", idBook);
        return bookMapper.toDTO(book);
    }

    @Override
    public List<BookDTO> findAll() {
        logger.info("Retrieving all books");

        List<Book> books = bookRepository.findAll();

        if (books.isEmpty()) {
            logger.warn("No books found in the database");
            throw new ResourceNotFoundException("No books found");
        }

        logger.info("Successfully retrieved {} books", books.size());
        return books.stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long idBook) {
        logger.info("Deleting book with ID: {}", idBook);

        if (!bookRepository.existsById(idBook)) {
            logBookNotFound(idBook);
            throw new ResourceNotFoundException("Book with ID: " + idBook + " not found");
        }

        bookRepository.deleteById(idBook);
        logger.info("Book with ID: {} deleted successfully", idBook);
    }

    @Override
    public List<LoanDTO> getLoans(Long idBook) {
        logger.info("Retrieving loans for book with ID: {}", idBook);

        List<Loan> loans = loanRepository.findByBookIdBook(idBook);

        if (loans.isEmpty()) {
            logger.warn("No loans found for book ID: {}", idBook);
            throw new ResourceNotFoundException("No loans found for this book");
        }

        logger.info("Successfully retrieved {} loans for book ID: {}", loans.size(), idBook);
        return loans.stream()
                .map(loanMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookDTO updateAvailableCopies(int nb, Long idBook) {
        logger.info("Updating available copies for books with adjustment: {}", nb);

        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> {
                    logBookNotFound(idBook);
                    return new ResourceNotFoundException("Book with ID: " + idBook + " not found");
                });

        book.setAvailableCopies(book.getAvailableCopies() + nb);
        if(book.getAvailableCopies() < 0) {
            return null;
        }

        logger.info("Available copies updated for book with title : {}", book.getTitle());
        return bookMapper.toDTO(bookRepository.save(book));
    }

    private void logBookNotFound(Long idBook) {
        logger.error("Book with ID: {} not found", idBook);
    }
}

