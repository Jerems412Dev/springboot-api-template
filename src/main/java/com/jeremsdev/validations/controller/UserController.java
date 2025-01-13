package com.jeremsdev.validations.controller;

import com.jeremsdev.validations.dto.LoanDTO;
import com.jeremsdev.validations.dto.UserDTO;
import com.jeremsdev.validations.exception.ApiRequestException;
import com.jeremsdev.validations.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private  final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> add(@RequestBody UserDTO userDTO) {
        logger.info("Received request to add a new User: {}", userDTO);
        try {
            UserDTO addedUser = userService.add(userDTO);
            logger.info("User added successfully: {}", addedUser);
            return new ResponseEntity<>(addedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error adding User: {}", e.getMessage());
            throw new ApiRequestException("Error adding User: " + e.getMessage());
        }
    }

    @PutMapping("/{idUser}")
    public ResponseEntity<UserDTO> update(@PathVariable Long idUser, @RequestBody UserDTO userDTO) {
        logger.info("Received request to update User with ID: {}", idUser);
        try {
            UserDTO updatedUser = userService.update(idUser, userDTO);
            logger.info("User with ID {} updated successfully", idUser);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error updating User with ID {}: {}", idUser, e.getMessage());
            throw new ApiRequestException("Error updating User with ID " + idUser + ": " + e.getMessage());
        }
    }

    @GetMapping("/getbyid/{idUser}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long idUser) {
        logger.info("Received request to retrieve User with ID: {}", idUser);
        try {
            UserDTO userDTO = userService.getById(idUser);
            if (userDTO == null) {
                throw new ApiRequestException("User with ID " + idUser + " not found");
            }
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (ApiRequestException e) {
            logger.error("Error retrieving User with ID {}: {}", idUser, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error retrieving User with ID {}: {}", idUser, e.getMessage());
            throw new ApiRequestException("Error retrieving User with ID " + idUser + ": " + e.getMessage());
        }
    }

    @GetMapping("/findall")
    public ResponseEntity<List<UserDTO>> findAll() {
        logger.info("Received request to retrieve all Users");
        try {
            List<UserDTO> users = userService.findAll();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving User list: {}", e.getMessage());
            throw new ApiRequestException("Error retrieving User list: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{idUser}")
    public ResponseEntity<Void> delete(@PathVariable Long idUser) {
        logger.info("Received request to delete User with ID: {}", idUser);
        try {
            userService.delete(idUser);
            logger.info("User with ID {} deleted successfully", idUser);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Error deleting User with ID {}: {}", idUser, e.getMessage());
            throw new ApiRequestException("Error deleting User with ID " + idUser + ": " + e.getMessage());
        }
    }

    @GetMapping("/getloan/{idUser}")
    public ResponseEntity<List<LoanDTO>> getLoans(@PathVariable Long idUser) {
        logger.info("Received request to retrieve Loan with idUser: {}", idUser);
        try {
            List<LoanDTO> loans = userService.getLoans(idUser);
            if (loans == null) {
                throw new ApiRequestException("Loans with IdUser " + idUser + " not found");
            }
            return new ResponseEntity<>(loans, HttpStatus.OK);
        } catch (ApiRequestException e) {
            logger.error("Error retrieving Loans with IdUser {}: {}", idUser, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error retrieving Loans with IdUser {}: {}", idUser, e.getMessage());
            throw new ApiRequestException("Error retrieving Loans with IdUser " + idUser + ": " + e.getMessage());
        }
    }
}
