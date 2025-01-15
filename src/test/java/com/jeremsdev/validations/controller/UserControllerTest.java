package com.jeremsdev.validations.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jeremsdev.validations.dto.LoanDTO;
import com.jeremsdev.validations.dto.UserDTO;
import com.jeremsdev.validations.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false) // Disabled Filter
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    private UserDTO user;
    private UserDTO user2;
    private LoanDTO loan1;
    private LoanDTO loan2;

    @BeforeEach
    void init() {
        user = new UserDTO();
        user.setName("user1");
        user.setPhoneNumber("1234567890");
        user.setEmail("johndoe@example.com");

        user2 = new UserDTO();
        user2.setName("Obiang Dev");
        user2.setPhoneNumber("+221 770000000");
        user2.setEmail("user2@gmail.com");

        loan1 = new LoanDTO();
        loan1.setLoanDate(new Date()); // actual Date
        loan1.setReturnDate(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)); // Return in 7 days
        loan1.setState(true); // Example : active loan
        loan1.setIdUser(1L);
        loan1.setIdBook(1L);

        loan2 = new LoanDTO();
        loan2.setLoanDate(new Date(System.currentTimeMillis() - 14 * 24 * 60 * 60 * 1000)); // loan add 14 days ago
        loan2.setReturnDate(new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000)); // return 7 days ago
        loan2.setState(false); // Example : loan ended
        loan2.setIdUser(1L);
        loan2.setIdBook(1L);
    }

    @Test
    void shouldUpdateUser() throws Exception {

        when(userService.update(anyLong(), any(UserDTO.class))).thenReturn(user);

        this.mockMvc.perform(put("/user/update/{idUser}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.phoneNumber", is(user.getPhoneNumber())));
    }

    @Test
    void shouldFetchOneUserById() throws Exception {

        when(userService.getById(anyLong())).thenReturn(user);

        this.mockMvc.perform(get("/user/getbyid/{idUser}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.phoneNumber", is(user.getPhoneNumber())));
    }

    @Test
    void shouldFetchAllUsers() throws Exception {

        List<UserDTO> list = new ArrayList<>();
        list.add(user);

        when(userService.findAll()).thenReturn(list);

        this.mockMvc.perform(get("/user/findall"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(list.size())));
    }

    @Test
    void shouldDeleteUser() throws Exception {

        doNothing().when(userService).delete(anyLong());

        this.mockMvc.perform(delete("/user/delete/{idUser}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldFetchLoansByIdUser() throws Exception {

        List<LoanDTO> list = new ArrayList<>();
        list.add(loan1);
        list.add(loan2);

        when(userService.getLoans(anyLong())).thenReturn(list);

        this.mockMvc.perform(get("/user/getloans/{idUser}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(list.size())));
    }

}
