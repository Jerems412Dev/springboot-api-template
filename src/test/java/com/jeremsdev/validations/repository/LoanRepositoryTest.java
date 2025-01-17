package com.jeremsdev.validations.repository;

import com.jeremsdev.validations.dto.LoanDTO;
import com.jeremsdev.validations.model.Book;
import com.jeremsdev.validations.model.Categories;
import com.jeremsdev.validations.model.Loan;
import com.jeremsdev.validations.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false) // Disabled Filter
public class LoanRepositoryTest {
    @Autowired
    private LoanRepository loanRepository;
    private Loan loan;
    private Book book;
    private User user;

    @BeforeEach
    void SetUp() throws ParseException {
        user = new User();
        user.setIdUser(1L);
        user.setName("user1");
        user.setEmail("user1@example.com");
        user.setPhoneNumber("780000000");
        user.setPassword("strongpassword123");

        book = new Book();
        book.setIdBook(1L);
        book.setTitle("book");
        book.setAuthor("Author book");
        book.setAvailableCopies(10);
        book.setCountPages(218);

        loan = new Loan();
        loan.setLoanDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-01")); // actual Date
        loan.setReturnDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-08")); // Return in 7 days
        loan.setState(true); // Example : active loan
        loan.setBook(book);
        loan.setUser(user);
    }

    @Test
    @DisplayName("Test: Save Loan")
    @Order(1)
    @Rollback(value = false)
    void save() {
        Loan newLoan = loanRepository.save(loan);
        assertNotNull(newLoan);
        assertThat(newLoan.getIdLoan()).isNotEqualTo(null);
    }

    @Test
    @DisplayName("Test: get all Loans")
    @Order(2)
    @Rollback(value = false)
    void getAllLoans() {
        List<Loan> list = loanRepository.findAll();

        assertNotNull(list);
        assertThat(list).isNotNull();
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Test: Get one Loan by ID")
    @Order(3)
    @Rollback(value = false)
    void getLoanById() {

        Loan newLoan = loanRepository.findById(1L).get();

        assertNotNull(newLoan);
        assertEquals(1L, newLoan.getIdLoan());
    }

    @Test
    @DisplayName("Test: Update Loan")
    @Order(4)
    @Rollback(value = false)
    void update() throws ParseException {
        Loan loanFind = loanRepository.findById(1L).get();
        loanFind.setReturnDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-18"));
        Loan loanUpdated =  loanRepository.save(loanFind);

        assertThat(loanUpdated.getReturnDate().toString()).isEqualTo(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-18").toString());
    }

    @Test
    @DisplayName("Test: Delete one Loan by ID")
    @Order(5)
    @Rollback(value = false)
    void deleteLoanById() {

        loanRepository.deleteById(1L);

        Optional<Loan> existingBook = loanRepository.findById(1L);

        assertThat(existingBook).isEmpty();
    }


}
