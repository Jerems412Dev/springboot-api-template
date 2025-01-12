package com.jeremsdev.validations.service.impl;

import com.jeremsdev.validations.dto.LoanDTO;
import com.jeremsdev.validations.dto.UserDTO;
import com.jeremsdev.validations.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public UserDTO add(UserDTO userDTO) {
        return null;
    }

    @Override
    public UserDTO update(Long idUser, UserDTO userDTO) {
        return null;
    }

    @Override
    public UserDTO getById(Long idUser) {
        return null;
    }

    @Override
    public List<UserDTO> findAll(Long idUser) {
        return null;
    }

    @Override
    public void delete(Long idUser) {

    }

    @Override
    public List<LoanDTO> getLoans(Long idUser) {
        return null;
    }
}
