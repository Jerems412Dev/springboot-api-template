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
    @NonNull
    private Long idLoan;
    @NonNull
    private Date loanDate;
    @NonNull
    private Date returnDate;
    private boolean state;
    @NonNull
    private Long idUser;
    @NonNull
    private Long idBook;
}
