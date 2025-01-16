package com.jeremsdev.validations.repository;

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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false) // Disabled Filter
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void SetUp() throws ParseException {
        user = new User();
        user.setName("user1");
        user.setEmail("user1@example.com");
        user.setPhoneNumber("780000000");
        user.setPassword("strongpassword123");
    }

    @Test
    @DisplayName("Test: Save User")
    @Order(1)
    @Rollback(value = false)
    void save() {
        User newUser = userRepository.save(user);
        assertNotNull(newUser);
        assertThat(newUser.getIdUser()).isNotEqualTo(null);
    }

    @Test
    @DisplayName("Test: get all Users")
    @Order(2)
    @Rollback(value = false)
    void getAllUsers() {
        List<User> list = userRepository.findAll();

        assertNotNull(list);
        assertThat(list).isNotNull();
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Test: Get one User by ID")
    @Order(3)
    @Rollback(value = false)
    void getUserById() {

        User newUser = userRepository.findById(1L).get();

        assertNotNull(newUser);
        assertEquals(user.getName(), newUser.getName());
    }

    @Test
    @DisplayName("Test: Update User")
    @Order(4)
    @Rollback(value = false)
    void update() {
        User userFind = userRepository.findById(1L).get();
        userFind.setEmail("jeremy2obiang@gmail.com");
        User userUpdated =  userRepository.save(userFind);

        assertThat(userUpdated.getEmail()).isEqualTo("jeremy2obiang@gmail.com");
    }

    @Test
    @DisplayName("Test: Delete one User by ID")
    @Order(5)
    @Rollback(value = false)
    void deleteUserById() {

        userRepository.deleteById(1L);

        Optional<User> existingUser = userRepository.findById(1L);

        assertThat(existingUser).isEmpty();
    }
}
