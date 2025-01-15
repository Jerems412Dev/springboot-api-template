package com.jeremsdev.validations.controller;

import com.jeremsdev.validations.dto.LoanDTO;
import com.jeremsdev.validations.exception.ApiRequestException;
import com.jeremsdev.validations.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/loan")
public class LoanController {
    private static final Logger logger = LoggerFactory.getLogger(LoanController.class);
    private final LoanService loanService;

    @PostMapping("/add")
    public ResponseEntity<LoanDTO> add(@RequestBody LoanDTO loanDTO) {
        logger.info("Received request to add a new Loan: {}", loanDTO);
        try {
            LoanDTO addedLoan = loanService.add(loanDTO);
            logger.info("Loan added successfully: {}", addedLoan);
            return new ResponseEntity<>(addedLoan, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error adding Loan: {}", e.getMessage());
            throw new ApiRequestException("Error adding Loan: " + e.getMessage());
        }
    }

    @PutMapping("/update/{idLoan}")
    public ResponseEntity<LoanDTO> update(@PathVariable Long idLoan, @RequestBody LoanDTO loanDTO) {
        logger.info("Received request to update Loan with ID: {}", idLoan);
        try {
            LoanDTO updatedLoan = loanService.update(idLoan, loanDTO);
            logger.info("Loan with ID {} updated successfully", idLoan);
            return new ResponseEntity<>(updatedLoan, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error updating Loan with ID {}: {}", idLoan, e.getMessage());
            throw new ApiRequestException("Error updating Loan with ID " + idLoan + ": " + e.getMessage());
        }
    }

    @GetMapping("/getbyid/{idLoan}")
    public ResponseEntity<LoanDTO> getById(@PathVariable Long idLoan) {
        logger.info("Received request to retrieve Loan with ID: {}", idLoan);
        try {
            LoanDTO loanDTO = loanService.getById(idLoan);
            if (loanDTO == null) {
                throw new ApiRequestException("Loan with ID " + idLoan + " not found");
            }
            return new ResponseEntity<>(loanDTO, HttpStatus.OK);
        } catch (ApiRequestException e) {
            logger.error("Error retrieving Loan with ID {}: {}", idLoan, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error retrieving Loan with ID {}: {}", idLoan, e.getMessage());
            throw new ApiRequestException("Error retrieving Loan with ID " + idLoan + ": " + e.getMessage());
        }
    }

    @GetMapping("/findall")
    public ResponseEntity<List<LoanDTO>> findAll() {
        logger.info("Received request to retrieve all Loans");
        try {
            List<LoanDTO> loans = loanService.findAll();
            return new ResponseEntity<>(loans, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving Loan list: {}", e.getMessage());
            throw new ApiRequestException("Error retrieving Loan list: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{idLoan}")
    public ResponseEntity<Void> delete(@PathVariable Long idLoan) {
        logger.info("Received request to delete Loan with ID: {}", idLoan);
        try {
            loanService.delete(idLoan);
            logger.info("Loan with ID {} deleted successfully", idLoan);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Error deleting Loan with ID {}: {}", idLoan, e.getMessage());
            throw new ApiRequestException("Error deleting Loan with ID " + idLoan + ": " + e.getMessage());
        }
    }
}
