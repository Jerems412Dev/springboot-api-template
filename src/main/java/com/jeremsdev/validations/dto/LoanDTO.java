package com.jeremsdev.validations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDTO {
    private Long idLoan;
    private Date loanDate;
    private Date returnDate;
    private boolean state;
    private Long idUser;
    private Long idBook;
}
