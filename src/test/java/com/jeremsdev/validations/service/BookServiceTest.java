package com.jeremsdev.validations.service;

import com.jeremsdev.validations.dto.BookDTO;
import com.jeremsdev.validations.model.Book;
import com.jeremsdev.validations.model.Categories;
import com.jeremsdev.validations.repository.BookRepository;
import com.jeremsdev.validations.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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
    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        book1 = Book.builder()
                .idBook(1L)
            .title("book 1")
            .author("Author book 1")
            .availableCopies(10)
            .countPages(218)
            .category(Categories.ROMAN)
                .build();

        book2 = Book.builder()
            .title("book 2")
            .author("Author book 2")
            .availableCopies(15)
            .countPages(400)
            .category(Categories.HORROR)
                .build();
    }
}
