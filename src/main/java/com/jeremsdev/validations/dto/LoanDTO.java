package com.jeremsdev.validations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDTO {
    private Long idLoan;
    private Date loanDate;
    private Date returnDate;
    private boolean state;
    private Long idUser;
    private Long idBook;
}
