package com.jeremsdev.validations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeremsdev.validations.dto.LoanDTO;
import com.jeremsdev.validations.dto.BookDTO;
import com.jeremsdev.validations.model.Categories;
import com.jeremsdev.validations.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false) // Disabled Filter
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private BookService bookService;
    @Autowired
    private ObjectMapper objectMapper;
    private BookDTO book1;
    private BookDTO book2;
    private LoanDTO loan1;
    private LoanDTO loan2;

    @BeforeEach
    void init() {
        book1 = new BookDTO();
        book1.setTitle("The Great Gatsby");
        book1.setAuthor("F. Scott Fitzgerald");
        book1.setAvailableCopies(3);
        book1.setCountPages(218);
        book1.setCategory(Categories.ROMAN);

        book2 = new BookDTO();
        book2.setTitle("Clean Code");
        book2.setAuthor("Robert C. Martin");
        book2.setAvailableCopies(3);
        book2.setCountPages(464);
        book2.setCategory(Categories.MYSTERY);

        loan1 = new LoanDTO();
        loan1.setLoanDate(new Date()); // actual Date
        loan1.setReturnDate(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)); // Return in 7 days
        loan1.setState(true); // Example : active loan
        loan1.setIdBook(1L);
        loan1.setIdUser(1L);

        loan2 = new LoanDTO();
        loan2.setLoanDate(new Date(System.currentTimeMillis() - 14 * 24 * 60 * 60 * 1000)); // loan add 14 days ago
        loan2.setReturnDate(new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000)); // return 7 days ago
        loan2.setState(false); // Example : loan ended
        loan2.setIdBook(1L);
        loan2.setIdUser(1L);
    }

    @Test
    void shouldAddNewBook() throws Exception {
        when(bookService.add(any(BookDTO.class))).thenReturn(book1);

        this.mockMvc.perform(post("/book/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(book1.getTitle())))
                .andExpect(jsonPath("$.author", is(book1.getAuthor())))
                .andExpect(jsonPath("$.availableCopies", is(book1.getAvailableCopies())))
                .andExpect(jsonPath("$.countPages", is(book1.getCountPages())))
                .andExpect(jsonPath("$.category", is(book1.getCategory().toString())));
    }

    @Test
    void shouldUpdateBook() throws Exception {

        when(bookService.update(anyLong(), any(BookDTO.class))).thenReturn(book1);

        this.mockMvc.perform(put("/book/update/{idBook}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(book1.getTitle())))
                .andExpect(jsonPath("$.author", is(book1.getAuthor())))
                .andExpect(jsonPath("$.availableCopies", is(book1.getAvailableCopies())))
                .andExpect(jsonPath("$.countPages", is(book1.getCountPages())))
                .andExpect(jsonPath("$.category", is(book1.getCategory().toString())));

    }

    @Test
    void shouldFetchOneBookById() throws Exception {

        when(bookService.getById(anyLong())).thenReturn(book1);

        this.mockMvc.perform(get("/book/getbyid/{idBook}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(book1.getTitle())))
                .andExpect(jsonPath("$.author", is(book1.getAuthor())))
                .andExpect(jsonPath("$.availableCopies", is(book1.getAvailableCopies())))
                .andExpect(jsonPath("$.countPages", is(book1.getCountPages())))
                .andExpect(jsonPath("$.category", is(book1.getCategory().toString())));
    }

    @Test
    void shouldFetchAllBooks() throws Exception {

        List<BookDTO> list = new ArrayList<>();
        list.add(book1);
        list.add(book2);

        when(bookService.findAll()).thenReturn(list);

        this.mockMvc.perform(get("/book/findall"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(list.size())));
    }

    @Test
    void shouldDeleteBook() throws Exception {

        doNothing().when(bookService).delete(anyLong());

        this.mockMvc.perform(delete("/book/delete/{idBook}", 2L))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldFetchLoansByIdBook() throws Exception {

        List<LoanDTO> list = new ArrayList<>();
        list.add(loan1);
        list.add(loan2);

        when(bookService.getLoans(anyLong())).thenReturn(list);

        this.mockMvc.perform(get("/book/getloans/{idBook}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(list.size())));
    }

    @Test
    void shouldUpdateAvailableCopiesBook() throws Exception {

        when(bookService.updateAvailableCopies(anyInt(),anyLong())).thenReturn(book1);

        this.mockMvc.perform(put("/book/updateavailablecopies/{idBook}/{nb}", 1L,-10)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(book1.getTitle())))
                .andExpect(jsonPath("$.author", is(book1.getAuthor())))
                .andExpect(jsonPath("$.availableCopies", is(book1.getAvailableCopies())))
                .andExpect(jsonPath("$.countPages", is(book1.getCountPages())))
                .andExpect(jsonPath("$.category", is(book1.getCategory().toString())));

    }
}
