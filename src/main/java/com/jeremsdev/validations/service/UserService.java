package com.jeremsdev.validations.service;

import com.jeremsdev.validations.dto.LoanDTO;
import com.jeremsdev.validations.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO add(UserDTO userDTO);
    UserDTO update(Long idUser, UserDTO userDTO);
    UserDTO getById(Long idUser);
    List<UserDTO> findAll();
    void delete(Long idUser);
    List<LoanDTO> getLoans(Long idUser);
}
