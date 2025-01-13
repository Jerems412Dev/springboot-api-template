package com.jeremsdev.validations.service;

import com.jeremsdev.validations.dto.LoanDTO;
import com.jeremsdev.validations.dto.UserDTO;

import java.util.List;

public interface LoanService {
    LoanDTO add(LoanDTO loanDTO);
    LoanDTO update(Long idLoan, LoanDTO loanDTO);
    LoanDTO getById(Long idLoan);
    List<LoanDTO> findAll();
    void delete(Long idLoan);
}
