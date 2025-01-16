package com.jeremsdev.validations.repository;

import com.jeremsdev.validations.model.Book;
import com.jeremsdev.validations.model.Categories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false) // Disabled Filter
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    private Book book;

    @BeforeEach
    void SetUp() {
        book = new Book();
        book.setTitle("book");
        book.setAuthor("Author book");
        book.setAvailableCopies(10);
        book.setCountPages(218);
        book.setCategory(Categories.ROMAN);
    }

    @Test
    @DisplayName("Test: Save Book")
    @Order(1)
    @Rollback(value = false)
    void save() {
        Book newBook = bookRepository.save(book);
        assertNotNull(newBook);
        assertThat(newBook.getIdBook()).isNotEqualTo(null);
    }

    @Test
    @DisplayName("Test: get all Books")
    @Order(2)
    @Rollback(value = false)
    void getAllBooks() {
        List<Book> list = bookRepository.findAll();

        assertNotNull(list);
        assertThat(list).isNotNull();
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Test: Get one Book by ID")
    @Order(3)
    @Rollback(value = false)
    void getBookById() {

        Book newBook = bookRepository.findById(1L).get();

        assertNotNull(newBook);
        assertEquals(book.getTitle(), newBook.getTitle());
    }

    @Test
    @DisplayName("Test: Update Book")
    @Order(4)
    @Rollback(value = false)
    void update() {
        Book bookFind = bookRepository.findById(1L).get();
        bookFind.setTitle("title updated");
        Book bookUpdated =  bookRepository.save(bookFind);

        assertThat(bookUpdated.getTitle()).isEqualTo("title updated");
    }

    @Test
    @DisplayName("Test: Delete one Book by ID")
    @Order(5)
    @Rollback(value = false)
    void deleteBookById() {

        bookRepository.deleteById(1L);

        Optional<Book> existingBook = bookRepository.findById(1L);

        assertThat(existingBook).isEmpty();
    }
}
