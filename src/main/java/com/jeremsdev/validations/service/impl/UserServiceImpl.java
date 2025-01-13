package com.jeremsdev.validations.service.impl;

import com.jeremsdev.validations.dto.LoanDTO;
import com.jeremsdev.validations.dto.UserDTO;
import com.jeremsdev.validations.exception.ResourceNotFoundException;
import com.jeremsdev.validations.mapper.LoanMapper;
import com.jeremsdev.validations.mapper.UserMapper;
import com.jeremsdev.validations.model.Loan;
import com.jeremsdev.validations.model.User;
import com.jeremsdev.validations.repository.LoanRepository;
import com.jeremsdev.validations.repository.UserRepository;
import com.jeremsdev.validations.service.UserService;
import com.jeremsdev.validations.validator.UserValidator;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserValidator userValidator;
    private final UserRepository userRepository;
    private final LoanRepository loanRepository;
    private final UserMapper userMapper;
    private final LoanMapper loanMapper;

    public UserServiceImpl(UserValidator userValidator, UserRepository userRepository, UserMapper userMapper, LoanRepository loanRepository, LoanMapper loanMapper) {
        this.userValidator = userValidator;
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
        this.loanMapper = loanMapper.INSTANCE;
        this.userMapper = UserMapper.INSTANCE;
    }

    @Override
    public UserDTO add(UserDTO userDTO) {
        logger.info("Adding user with name: {}", userDTO.getName());

        userValidator.validateUserDTO(userDTO);

        User user = userMapper.toEntity(userDTO);
        user = userRepository.save(user);

        logger.info("User added successfully with ID: {}", user.getIdUser());

        return userMapper.toDTO(user);
    }

    @Override
    public UserDTO update(Long idUser, UserDTO userDTO) {
        logger.info("Updating user with ID: {}", idUser);

        User user = userRepository.findById(idUser)
                .orElseThrow(()
                        -> {
                    logUserNotFound(idUser);
                    return new ResourceNotFoundException("User with ID :  " + idUser + " not found");
                });

        userMapper.updateEntityFromDTO(userDTO, user);

        logger.info("User updated successfully with ID: {}", idUser);
        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    public UserDTO getById(Long idUser) {
        logger.info("Finding user with ID: {}", idUser);

        User user = userRepository.findById(idUser)
                .orElseThrow(() -> {
                    logUserNotFound(idUser);
                    return new ResourceNotFoundException("User with ID :  " + idUser + " not found");
                });

        logger.info("User with ID: {} found", idUser);
        return userMapper.toDTO(user);
    }

    @Override
    public List<UserDTO> findAll() {
        logger.info("Retrieving all users");

        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            logger.warn("No users found in the database");
            throw new ResourceNotFoundException("No users found");
        }

        logger.info("Successfully retrieved {} users", users.size());
        return users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public void delete(Long idUser) {
        logger.info("Delete user with ID: {}", idUser);

        if (!userRepository.existsById(idUser)) {
            logUserNotFound(idUser);
            throw new ResourceNotFoundException("User with ID :  " + idUser + " not found");
        }

        userRepository.deleteById(idUser);
        logger.info("User with ID: {} deleted successfully", idUser);
    }

    @Override
    public List<LoanDTO> getLoans(Long idUser) {
        logger.info("Retrieving all users");

        List<Loan> loans = loanRepository.findByUserIdUser(idUser);

        if (loans.isEmpty()) {
            logger.warn("No loans found in the database for this ID: {}", idUser);
            throw new ResourceNotFoundException("No loans found");
        }

        logger.info("Successfully retrieved {} loans", loans.size());
        return loans.stream()
                .map(loanMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void logUserNotFound(Long idUser) {
        logger.error("User with ID: {} not found", idUser);
    }
}
