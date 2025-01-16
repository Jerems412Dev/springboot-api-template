package com.jeremsdev.validations.service;

import com.jeremsdev.validations.dto.BookDTO;
import com.jeremsdev.validations.dto.LoanDTO;
import com.jeremsdev.validations.dto.UserDTO;
import com.jeremsdev.validations.mapper.LoanMapper;
import com.jeremsdev.validations.model.*;
import com.jeremsdev.validations.model.Loan;
import com.jeremsdev.validations.repository.LoanRepository;
import com.jeremsdev.validations.repository.LoanRepository;
import com.jeremsdev.validations.service.impl.BookServiceImpl;
import com.jeremsdev.validations.service.impl.LoanServiceImpl;
import com.jeremsdev.validations.validator.LoanValidator;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false) // Disabled Filter
public class LoanServiceTest {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private LoanServiceImpl loanService;
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private LoanMapper loanMapper;
    @Mock
    private LoanValidator loanValidator;
    @Mock
    private BookServiceImpl bookService;
    private Loan loan;
    private LoanDTO loanDTO;
    private User user;
    private Book book;
    private BookDTO bookDTO;

    @BeforeEach
    void setUp() throws ParseException {
        book = Book.builder()
                .idBook(1L)
                .title("book")
                .author("Author book")
                .availableCopies(10)
                .countPages(218)
                .category(Categories.ROMAN)
                .build();

        bookDTO = BookDTO.builder()
                .idBook(1L)
                .title("book")
                .author("Author book")
                .availableCopies(10)
                .countPages(218)
                .category(Categories.ROMAN)
                .build();

        user = User.builder()
                .idUser(1L)
                .name("user")
                .email("user@example.com")
                .phoneNumber("780000000")
                .build();

        loan = Loan.builder()
                .idLoan(1L)
                .loanDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-01")) // actual Date
                .returnDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-08")) // Return in 7 days
                .state(true)
                .book(book)
                .user(user)
                .build();

        loanDTO = LoanDTO.builder()
                .idLoan(1L)
                .loanDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-01")) // actual Date
                .returnDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-08")) // Return in 7 days
                .state(true)
                .idBook(1L)
                .idUser(1L)
                .build();
    }

    @Test
    @Order(1)
    void addLoanSuccessfully(){
        doNothing().when(loanValidator).validateLoanDTO(any(LoanDTO.class));
        when(bookService.updateAvailableCopies(-1, loanDTO.getIdBook())).thenReturn(bookDTO);
        when(loanMapper.toEntity(any(LoanDTO.class))).thenReturn(loan);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        LoanDTO addedLoan = loanService.add(loanDTO);

        verify(bookService, times(1)).updateAvailableCopies(-1, loanDTO.getIdBook());
        verify(loanRepository, times(1)).save(any(Loan.class));

        assertNotNull(addedLoan);
        assertThat(addedLoan.getIdLoan()).isEqualTo(loan.getIdLoan());
    }

    @Test
    @Order(2)
    void updateLoanSuccessfully() throws ParseException {
        doNothing().when(loanValidator).validateLoanDTO(any(LoanDTO.class));
        when(loanRepository.findById(anyLong())).thenReturn(Optional.of(loan));
        doNothing().when(loanMapper).updateEntityFromDTO(loanDTO,(loan));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        loanDTO.setReturnDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-10"));

        LoanDTO updatedLoan = loanService.update(loan.getIdLoan(),loanDTO);

        assertNotNull(updatedLoan);
        assertThat(updatedLoan.getIdLoan()).isEqualTo(loan.getIdLoan());
        assertThat(updatedLoan.getReturnDate()).isEqualTo("2023-01-10");
    }

    @Test
    @Order(3)
    void getAllLoansSuccessfully() {
        when(loanRepository.findAll()).thenReturn(List.of(loan));

        List<LoanDTO> loanList = loanService.findAll();

        assertNotNull(loanList);
        assertEquals(1, loanList.size());
    }

    @Test
    @Order(4)
    void getOneLoanByIdLoanSuccessfully() {
        when(loanRepository.findById(anyLong())).thenReturn(Optional.ofNullable(loan));

        LoanDTO loanReceived = loanService.getById(loan.getIdLoan());

        assertNotNull(loanReceived);
        assertThat(loanReceived.getIdLoan()).isEqualTo(loan.getIdLoan());
        assertThat(loanReceived.getReturnDate()).isEqualTo(loan.getReturnDate());
    }

    @Test
    @Order(5)
    void deleteOneLoanSuccessfully() {
        Long loanId = 1L;
        when(loanRepository.existsById(anyLong())).thenReturn(true);
        when(loanRepository.findById(anyLong())).thenReturn(Optional.of(loan));
        doNothing().when(loanRepository).deleteById(anyLong());

        loanService.delete(loanId);
        verify(loanRepository, times(1)).deleteById(loanId);
    }
}
