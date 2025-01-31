package com.jeremsdev.validations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeremsdev.validations.dto.LoanDTO;
import com.jeremsdev.validations.service.LoanService;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false) // Disabled Filter
class LoanControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private LoanService loanService;
    @Autowired
    private ObjectMapper objectMapper;
    private LoanDTO loan1;
    private LoanDTO loan2;
    private LoanDTO loanUpdated;

    @BeforeEach
    void setUp() throws ParseException {
        loan1 = new LoanDTO();
        loan1.setLoanDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-01")); // actual Date
        loan1.setReturnDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-08")); // Return in 7 days
        loan1.setState(true); // Example : active loan
        loan1.setIdUser(1L);
        loan1.setIdBook(1L);

        loan2 = new LoanDTO();
        loan2.setLoanDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-01")); // actual Date
        loan2.setReturnDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-08")); // return 7 days ago
        loan2.setState(true);
        loan2.setIdUser(1L);
        loan2.setIdBook(1L);

        loanUpdated = new LoanDTO();
        loanUpdated.setIdLoan(2L);
        loanUpdated.setLoanDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-01")); // actual Date
        loanUpdated.setReturnDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-08")); // return 7 days ago
        loanUpdated.setState(false); // Example : loan ended
        loanUpdated.setIdUser(1L);
        loanUpdated.setIdBook(1L);
    }

    @Test
    @Order(1)
    void shouldAddNewLoanOne() throws Exception {
        when(loanService.add(any(LoanDTO.class))).thenReturn(loan1);

        this.mockMvc.perform(post("/loan/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.loanDate", is("2023-01-01")))
                .andExpect(jsonPath("$.returnDate", is("2023-01-08")))
                .andExpect(jsonPath("$.state", is(loan1.isState())))
                .andExpect(jsonPath("$.idUser", is(loan1.getIdUser().intValue())))
                .andExpect(jsonPath("$.idBook", is(loan1.getIdBook().intValue())));

    }

    @Test
    @Order(2)
    void shouldAddNewLoanTwo() throws Exception {
        when(loanService.add(any(LoanDTO.class))).thenReturn(loan2);

        this.mockMvc.perform(post("/loan/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.loanDate", is("2023-01-01")))
                .andExpect(jsonPath("$.returnDate", is("2023-01-08")))
                .andExpect(jsonPath("$.state", is(loan2.isState())))
                .andExpect(jsonPath("$.idUser", is(loan2.getIdUser().intValue())))
                .andExpect(jsonPath("$.idBook", is(loan2.getIdBook().intValue())));

    }

    @Test
    @Order(3)
    void shouldUpdateLoan() throws Exception {

        when(loanService.update(anyLong(), any(LoanDTO.class))).thenReturn(loanUpdated);

        this.mockMvc.perform(put("/loan/update/{idLoan}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanUpdated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanDate", is("2023-01-01")))
                .andExpect(jsonPath("$.returnDate", is("2023-01-08")))
                .andExpect(jsonPath("$.state", is(loanUpdated.isState())))
                .andExpect(jsonPath("$.idUser", is(loanUpdated.getIdUser().intValue())))
                .andExpect(jsonPath("$.idBook", is(loanUpdated.getIdBook().intValue())));

    }

    @Test
    @Order(4)
    void shouldFetchOneLoanById() throws Exception {

        when(loanService.getById(anyLong())).thenReturn(loan1);

        this.mockMvc.perform(get("/loan/getbyid/{idLoan}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanDate", is("2023-01-01")))
                .andExpect(jsonPath("$.returnDate", is("2023-01-08")))
                .andExpect(jsonPath("$.state", is(loan1.isState())))
                .andExpect(jsonPath("$.idUser", is(loan1.getIdUser().intValue())))
                .andExpect(jsonPath("$.idBook", is(loan1.getIdBook().intValue())));
    }

    @Test
    @Order(5)
    void shouldFetchAllLoans() throws Exception {

        List<LoanDTO> list = new ArrayList<>();
        list.add(loan1);
        list.add(loanUpdated);

        when(loanService.findAll()).thenReturn(list);

        this.mockMvc.perform(get("/loan/findall"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(list.size())));
    }

    /*@Test
    @Order(6)
    void shouldDeleteLoan() throws Exception {

        doNothing().when(loanService).delete(anyLong());

        this.mockMvc.perform(delete("/loan/delete/{idLoan}", 2L))
                .andExpect(status().isNoContent());
    }*/
}
