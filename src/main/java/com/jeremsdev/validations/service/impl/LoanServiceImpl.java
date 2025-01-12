package com.jeremsdev.validations.service.impl;

import com.jeremsdev.validations.dto.LoanDTO;
import com.jeremsdev.validations.dto.UserDTO;
import com.jeremsdev.validations.service.LoanService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {
    @Override
    public LoanDTO add(LoanDTO loanDTO) {
        return null;
    }

    @Override
    public LoanDTO update(Long idLoan, LoanDTO loanDTO) {
        return null;
    }

    @Override
    public LoanDTO getById(Long idLoan) {
        return null;
    }

    @Override
    public List<LoanDTO> findAll(Long idLoan) {
        return null;
    }

    @Override
    public void delete(Long idLoan) {

    }

    @Override
    public List<LoanDTO> getLoans(Long idLoan) {
        return null;
    }

    @Override
    public List<UserDTO> getUsers(Long idUser) {
        return null;
    }
}
