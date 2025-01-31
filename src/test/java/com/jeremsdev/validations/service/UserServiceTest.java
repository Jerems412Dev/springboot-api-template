package com.jeremsdev.validations.service;

import com.jeremsdev.validations.dto.LoanDTO;
import com.jeremsdev.validations.dto.UserDTO;
import com.jeremsdev.validations.mapper.UserMapper;
import com.jeremsdev.validations.model.Loan;
import com.jeremsdev.validations.model.User;
import com.jeremsdev.validations.repository.LoanRepository;
import com.jeremsdev.validations.repository.UserRepository;
import com.jeremsdev.validations.service.impl.UserServiceImpl;
import com.jeremsdev.validations.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false) // Disabled Filter
class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserValidator userValidator;
    private User user;
    private User user2;
    private Loan loan;

    @BeforeEach
    void setUp() throws ParseException {
        loan = Loan.builder()
                .loanDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-01")) // actual Date
                .returnDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-08")) // Return in 7 days
                .state(true)
                .build();

        Set<Loan> loans = new HashSet<>();
        loans.add(loan);

        user = User.builder()
                .idUser(1L)
                .name("user1")
                .email("user1@example.com")
                .phoneNumber("780000000")
                .loans(loans)
                .build();

        user2 = User.builder()
                .idUser(2L)
                .name("user2")
                .email("user2@example.com")
                .phoneNumber("770000000")
                .loans(loans)
                .build();
    }

    @Test
    @Order(1)
    void updateUserSuccessfully() {
        doNothing().when(userValidator).validateUserDTO(any(UserDTO.class));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        user.setName("user updated");
        user.setEmail("userupdated@example.com");

        UserDTO updatedUser = userService.update(user.getIdUser(),userMapper.toDTO(user));

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

    /*@Test
    @Order(4)
    void deleteOneUserSuccessfully() {
        Long userId = 2L;
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user2));
        doNothing().when(userRepository).deleteById(anyLong());

        userService.delete(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }*/

    @Test
    @Order(4)
    void retrieveLoansByIdBookUnsuccessfully() {
        when(loanRepository.findByUserIdUser(anyLong())).thenReturn(List.of(loan));

        List<LoanDTO> loanDTOList = userService.getLoans(user.getIdUser());

        assertNotNull(loanDTOList);
        assertEquals(1, loanDTOList.size());
    }

}
