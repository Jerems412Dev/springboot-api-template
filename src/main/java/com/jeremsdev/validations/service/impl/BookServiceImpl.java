package com.jeremsdev.validations.service.impl;

import com.jeremsdev.validations.dto.BookDTO;
import com.jeremsdev.validations.dto.LoanDTO;
import com.jeremsdev.validations.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Override
    public BookDTO add(BookDTO bookDTO) {
        return null;
    }

    @Override
    public BookDTO update(Long idBook, BookDTO bookDTO) {
        return null;
    }

    @Override
    public BookDTO getById(Long idBook) {
        return null;
    }

    @Override
    public List<BookDTO> findAll(Long idBook) {
        return null;
    }

    @Override
    public void delete(Long idBook) {

    }

    @Override
    public List<LoanDTO> getLoans(Long idBook) {
        return null;
    }

    @Override
    public void updateAvailableCopies(int nb) {

    }
}
