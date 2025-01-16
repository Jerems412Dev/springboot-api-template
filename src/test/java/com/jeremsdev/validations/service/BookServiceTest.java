package com.jeremsdev.validations.service;

import com.jeremsdev.validations.dto.BookDTO;
import com.jeremsdev.validations.dto.LoanDTO;
import com.jeremsdev.validations.mapper.BookMapper;
import com.jeremsdev.validations.model.Book;
import com.jeremsdev.validations.model.Categories;
import com.jeremsdev.validations.model.Loan;
import com.jeremsdev.validations.repository.BookRepository;
import com.jeremsdev.validations.repository.LoanRepository;
import com.jeremsdev.validations.service.impl.BookServiceImpl;
import com.jeremsdev.validations.validator.BookValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false) // Disabled Filter
public class BookServiceTest {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookValidator bookValidator;
    private Book book;
    private Loan loan;
    private BookDTO bookDTO;

    @BeforeEach
    void setUp() throws ParseException {
        loan = Loan.builder()
                .loanDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-01")) // actual Date
                .returnDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-08")) // Return in 7 days
                .state(true)
                .build();

        Set<Loan> loans = new HashSet<>();
        loans.add(loan);

        book = Book.builder()
                .idBook(1L)
                .title("book 1")
                .author("Author book 1")
                .availableCopies(10)
                .countPages(218)
                .category(Categories.ROMAN)
                .loans(loans)
                .build();

        bookDTO = BookDTO.builder()
                .idBook(1L)
                .title("book 1")
                .author("Author book 1")
                .availableCopies(10)
                .countPages(218)
                .category(Categories.ROMAN)
                .build();
    }

    @Test
    @Order(1)
    void addBookSuccessfully(){
        doNothing().when(bookValidator).validateBookDTO(any(BookDTO.class));
        when(bookMapper.toEntity(any(BookDTO.class))).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDTO addedBook = bookService.add(bookDTO);

        verify(bookValidator, times(1)).validateBookDTO(bookDTO);
        verify(bookRepository, times(1)).save(book);

        assertNotNull(addedBook);
        assertThat(addedBook.getIdBook()).isEqualTo(book.getIdBook());
        assertThat(addedBook.getTitle()).isEqualTo(book.getTitle());
        assertThat(addedBook.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(addedBook.getCategory()).isEqualTo(book.getCategory());
    }

    @Test
    @Order(2)
    void updateBookSuccessfully() {
        doNothing().when(bookValidator).validateBookDTO(any(BookDTO.class));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        book.setTitle("book updated");
        book.setAuthor("Author updated");

        BookDTO updatedBook = bookService.update(book.getIdBook(),bookMapper.toDTO(book));

        assertNotNull(updatedBook);
        assertThat(updatedBook.getTitle()).isEqualTo("book updated");
        assertThat(updatedBook.getAuthor()).isEqualTo("Author updated");
    }

    @Test
    @Order(3)
    void getOneBookByIdBookSuccessfully() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.ofNullable(book));

        BookDTO bookReceived = bookService.getById(book.getIdBook());

        assertNotNull(bookReceived);
        assertThat(bookReceived.getIdBook()).isEqualTo(book.getIdBook());
        assertThat(bookReceived.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookReceived.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(bookReceived.getCategory()).isEqualTo(book.getCategory());
    }

    @Test
    @Order(4)
    void getAllBooksSuccessfully() {
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<BookDTO> bookList = bookService.findAll();

        assertNotNull(bookList);
        assertEquals(1, bookList.size());
    }

    @Test
    @Order(5)
    void deleteOneBookSuccessfully() {
        Long bookId = 1L;
        when(bookRepository.existsById(anyLong())).thenReturn(true);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).deleteById(anyLong());

        bookService.delete(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    @Order(6)
    void updateAvailableCopiesSuccessfully() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.ofNullable(book));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        });

        BookDTO bookUpdated = bookService.updateAvailableCopies(5,1L);

        assertNotNull(bookUpdated);
        assertThat(bookUpdated.getAvailableCopies()).isEqualTo(15);
    }

    @Test
    @Order(7)
    void retrieveLoansByIdBookUnsuccessfully() {
        when(loanRepository.findByBookIdBook(anyLong())).thenReturn(List.of(loan));

        List<LoanDTO> loanDTOList = bookService.getLoans(book.getIdBook());

        assertNotNull(loanDTOList);
        assertEquals(1, loanDTOList.size());
    }

}
