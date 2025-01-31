package com.jeremsdev.validations.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.jeremsdev.validations.dto.LoanDTO;
import com.jeremsdev.validations.dto.UserDTO;
import com.jeremsdev.validations.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
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
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    private UserDTO user1;
    private UserDTO user2;
    private LoanDTO loan1;
    private LoanDTO loan2;

    @BeforeEach
    void setUp() throws ParseException {
        user1 = new UserDTO();
        user1.setName("user1");
        user1.setEmail("user1@example.com");
        user1.setPhoneNumber("780000000");

        user2 = new UserDTO();
        user2.setIdUser(2L);
        user2.setName("user updated");
        user2.setEmail("user2@example.com");
        user2.setPhoneNumber("770000000");

        loan1 = new LoanDTO();
        loan1.setLoanDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-01")); // actual Date
        loan1.setReturnDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-08")); // Return in 7 days
        loan1.setState(true); // Example : active loan
        loan1.setIdUser(1L);
        loan1.setIdBook(1L);

        loan2 = new LoanDTO();
        loan2.setLoanDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-01")); // actual Date
        loan2.setReturnDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-08")); // return 7 days ago
        loan2.setState(false); // Example : loan ended
        loan2.setIdUser(1L);
        loan2.setIdBook(1L);
    }

    @Test
    @Order(1)
    void shouldUpdateUserTwo() throws Exception {

        when(userService.update(anyLong(), any(UserDTO.class))).thenReturn(user2);

        this.mockMvc.perform(put("/user/update/{idUser}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user2.getName())))
                .andExpect(jsonPath("$.email", is(user2.getEmail())))
                .andExpect(jsonPath("$.phoneNumber", is(user2.getPhoneNumber())));
    }

    @Test
    @Order(2)
    void shouldFetchUserByIdUserOne() throws Exception {

        when(userService.getById(anyLong())).thenReturn(user1);

        this.mockMvc.perform(get("/user/getbyid/{idUser}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user1.getName())))
                .andExpect(jsonPath("$.email", is(user1.getEmail())))
                .andExpect(jsonPath("$.phoneNumber", is(user1.getPhoneNumber())));
    }

    @Test
    @Order(3)
    void shouldFetchAllUsers() throws Exception {

        List<UserDTO> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);

        when(userService.findAll()).thenReturn(list);

        this.mockMvc.perform(get("/user/findall"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(list.size())));
    }

    /*@Test
    @Order(4)
    void shouldDeleteUser() throws Exception {

        doNothing().when(userService).delete(anyLong());

        this.mockMvc.perform(delete("/user/delete/{idUser}", 2L))
                .andExpect(status().isNoContent());
    }*/

    @Test
    @Order(4)
    void shouldFetchLoansByIdUser() throws Exception {

        List<LoanDTO> list = new ArrayList<>();
        list.add(loan1);

        when(userService.getLoans(anyLong())).thenReturn(list);

        this.mockMvc.perform(get("/user/getloans/{idUser}",1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(list.size())));
    }

}
