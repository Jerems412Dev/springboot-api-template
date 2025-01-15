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
    private BookDTO book2Updated;
    private BookDTO bookUpdatedAvailable;

    @BeforeEach
    void init() {
        book1 = new BookDTO();
        book1.setTitle("book 1");
        book1.setAuthor("Author book 1");
        book1.setAvailableCopies(10);
        book1.setCountPages(218);
        book1.setCategory(Categories.ROMAN);

        book2 = new BookDTO();
        book2.setTitle("book 2");
        book2.setAuthor("Author book 2");
        book2.setAvailableCopies(15);
        book2.setCountPages(400);
        book2.setCategory(Categories.HORROR);

        book2Updated = new BookDTO();
        book2Updated.setIdBook(2L);
        book2Updated.setTitle("book updated");
        book2Updated.setAuthor("Author book updated");
        book2Updated.setAvailableCopies(10);
        book2Updated.setCountPages(200);
        book2Updated.setCategory(Categories.HORROR);

        bookUpdatedAvailable = new BookDTO();
        bookUpdatedAvailable.setTitle("book 1");
        bookUpdatedAvailable.setAuthor("Author book 1");
        bookUpdatedAvailable.setAvailableCopies(15);
        bookUpdatedAvailable.setCountPages(218);
        bookUpdatedAvailable.setCategory(Categories.ROMAN);
    }

    @Test
    void shouldAddNewBookOne() throws Exception {
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
    void shouldAddNewBookTwo() throws Exception {
        when(bookService.add(any(BookDTO.class))).thenReturn(book2);

        this.mockMvc.perform(post("/book/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(book2.getTitle())))
                .andExpect(jsonPath("$.author", is(book2.getAuthor())))
                .andExpect(jsonPath("$.availableCopies", is(book2.getAvailableCopies())))
                .andExpect(jsonPath("$.countPages", is(book2.getCountPages())))
                .andExpect(jsonPath("$.category", is(book2.getCategory().toString())));
    }

    @Test
    void shouldUpdateBook() throws Exception {

        when(bookService.update(anyLong(), any(BookDTO.class))).thenReturn(book2Updated);

        this.mockMvc.perform(put("/book/update/{idBook}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book2Updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(book2Updated.getTitle())))
                .andExpect(jsonPath("$.author", is(book2Updated.getAuthor())))
                .andExpect(jsonPath("$.availableCopies", is(book2Updated.getAvailableCopies())))
                .andExpect(jsonPath("$.countPages", is(book2Updated.getCountPages())))
                .andExpect(jsonPath("$.category", is(book2Updated.getCategory().toString())));

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
        list.add(book2Updated);

        when(bookService.findAll()).thenReturn(list);

        this.mockMvc.perform(get("/book/findall"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(list.size())));
    }

    @Test
    void shouldDeleteBookTwo() throws Exception {

        doNothing().when(bookService).delete(anyLong());

        this.mockMvc.perform(delete("/book/delete/{idBook}", 2L))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldFetchLoansByIdBookOne() throws Exception {

        List<LoanDTO> list = new ArrayList<>();


        when(bookService.getLoans(anyLong())).thenReturn(list);

        this.mockMvc.perform(get("/book/getloans/{idBook}",1L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.size()", is(list.size())));
    }

    @Test
    void shouldUpdateAvailableCopiesBookOne() throws Exception {

        when(bookService.updateAvailableCopies(anyInt(),anyLong())).thenReturn(bookUpdatedAvailable);

        this.mockMvc.perform(put("/book/updateavailablecopies/{idBook}/{nb}", 1L,5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookUpdatedAvailable)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(bookUpdatedAvailable.getTitle())))
                .andExpect(jsonPath("$.author", is(bookUpdatedAvailable.getAuthor())))
                .andExpect(jsonPath("$.availableCopies", is(bookUpdatedAvailable.getAvailableCopies())))
                .andExpect(jsonPath("$.countPages", is(bookUpdatedAvailable.getCountPages())))
                .andExpect(jsonPath("$.category", is(bookUpdatedAvailable.getCategory().toString())));

    }
}
