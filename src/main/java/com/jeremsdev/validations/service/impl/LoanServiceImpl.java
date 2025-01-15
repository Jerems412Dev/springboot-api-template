package com.jeremsdev.validations.service.impl;

import com.jeremsdev.validations.dto.LoanDTO;
import com.jeremsdev.validations.exception.ResourceNotFoundException;
import com.jeremsdev.validations.mapper.LoanMapper;
import com.jeremsdev.validations.model.Loan;
import com.jeremsdev.validations.repository.LoanRepository;
import com.jeremsdev.validations.service.BookService;
import com.jeremsdev.validations.service.LoanService;
import com.jeremsdev.validations.validator.LoanValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {
    private static final Logger logger = LoggerFactory.getLogger(LoanServiceImpl.class);
    private final LoanValidator loanValidator;
    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;
    private final BookService bookService;

    public LoanServiceImpl(LoanValidator loanValidator, LoanRepository loanRepository, LoanMapper loanMapper, BookService bookService) {
        this.loanValidator = loanValidator;
        this.loanRepository = loanRepository;
        this.loanMapper = loanMapper.INSTANCE;
        this.bookService = bookService;
    }

    @Override
    public LoanDTO add(LoanDTO loanDTO) {
        logger.info("Adding a new loan");

        loanValidator.validateLoanDTO(loanDTO);
        if(bookService.updateAvailableCopies(-1,loanDTO.getIdBook()) == null) {
            logger.error("Cannot add loan because book with ID: {} have not available copy.", loanDTO.getIdBook());
            return null;
        }

        Loan loan = loanMapper.toEntity(loanDTO);
        loan = loanRepository.save(loan);

        logger.info("Loan added successfully with ID: {}", loan.getIdLoan());

        return loanMapper.toDTO(loan);
    }

    @Override
    public LoanDTO update(Long idLoan, LoanDTO loanDTO) {
        logger.info("Updating loan with ID: {}", idLoan);

        Loan loan = loanRepository.findById(idLoan)
                .orElseThrow(() -> {
                    logLoanNotFound(idLoan);
                    return new ResourceNotFoundException("Loan with ID: " + idLoan + " not found");
                });

        if (loan.isState() != loanDTO.isState() && !loanDTO.isState()) {
            bookService.updateAvailableCopies(1,loanDTO.getIdBook());
        }

        loanMapper.updateEntityFromDTO(loanDTO, loan);

        logger.info("Loan updated successfully with ID: {}", idLoan);
        return loanMapper.toDTO(loanRepository.save(loan));
    }

    @Override
    public LoanDTO getById(Long idLoan) {
        logger.info("Finding loan with ID: {}", idLoan);

        Loan loan = loanRepository.findById(idLoan)
                .orElseThrow(() -> {
                    logLoanNotFound(idLoan);
                    return new ResourceNotFoundException("Loan with ID: " + idLoan + " not found");
                });

        logger.info("Loan with ID: {} found", idLoan);
        return loanMapper.toDTO(loan);
    }

    @Override
    public List<LoanDTO> findAll() {
        logger.info("Retrieving all loans");

        List<Loan> loans = loanRepository.findAll();

        if (loans.isEmpty()) {
            logger.warn("No loans found in the database");
            throw new ResourceNotFoundException("No loans found");
        }

        logger.info("Successfully retrieved {} loans", loans.size());
        return loans.stream()
                .map(loanMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long idLoan) {
        logger.info("Deleting loan with ID: {}", idLoan);

        if (!loanRepository.existsById(idLoan)) {
            logLoanNotFound(idLoan);
            throw new ResourceNotFoundException("Loan with ID: " + idLoan + " not found");
        }
        if(getById(idLoan).isState()) {
            bookService.updateAvailableCopies(1,getById(idLoan).getIdBook());
        }
        loanRepository.deleteById(idLoan);
        logger.info("Loan with ID: {} deleted successfully", idLoan);
    }

    private void logLoanNotFound(Long idLoan) {
        logger.error("Loan with ID: {} not found", idLoan);
    }
}

