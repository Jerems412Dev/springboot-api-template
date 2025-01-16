package com.jeremsdev.validations.service;

import com.jeremsdev.validations.dto.UserDTO;
import com.jeremsdev.validations.mapper.UserMapper;
import com.jeremsdev.validations.model.User;
import com.jeremsdev.validations.repository.UserRepository;
import com.jeremsdev.validations.service.impl.UserServiceImpl;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false) // Disabled Filter
public class UserServiceTest {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    private User user;
    private User user2;

    @BeforeEach
    void setUp() {

        user = User.builder()
            .idUser(1L)
            .name("user1")
            .email("user1@example.com")
            .phoneNumber("780000000")
                .build();

        user2 = User.builder()
            .idUser(2L)
            .name("user2")
            .email("user2@example.com")
            .phoneNumber("770000000")
                .build();
    }

    @Test
    @Order(1)
    void updateUserSuccessfully() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        user.setName("user updated");
        user.setEmail("userupdated@example.com");

        // action
        UserDTO updatedUser = userService.update(user.getIdUser(),userMapper.toDTO(user));

        // verify
        assertNotNull(updatedUser);
        assertThat(updatedUser.getEmail()).isEqualTo("userupdated@example.com");
        assertThat(updatedUser.getName()).isEqualTo("user updated");
    }

    @Test
    @Order(2)
    void getOneUserByIdUserSuccessfully() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        UserDTO userReceived = userService.getById(user.getIdUser());

        assertNotNull(userReceived);
        assertThat(userReceived.getIdUser()).isEqualTo(user.getIdUser());
        assertThat(userReceived.getEmail()).isEqualTo(user.getEmail());
        assertThat(userReceived.getName()).isEqualTo(user.getName());
        assertThat(userReceived.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
    }

    @Test
    @Order(3)
    void getAllUsersSuccessfully() {
        when(userRepository.findAll()).thenReturn(List.of(user,user2));

        List<UserDTO> userList = userService.findAll();

        assertNotNull(userList);
        assertEquals(2, userList.size());
    }

    @Test
    @Order(4)
    void deleteOneUserSuccessfully() {
        Long userId = 2L;
        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user2));
        doNothing().when(userRepository).deleteById(userId);

        userService.delete(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

}
