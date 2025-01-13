package com.jeremsdev.validations.service;

import com.jeremsdev.validations.dto.BookDTO;
import com.jeremsdev.validations.dto.LoanDTO;

import java.util.List;

public interface BookService {
    BookDTO add(BookDTO bookDTO);
    BookDTO update(Long idBook, BookDTO bookDTO);
    BookDTO getById(Long idBook);
    List<BookDTO> findAll();
    void delete(Long idBook);
    List<LoanDTO> getLoans(Long idBook);
    BookDTO updateAvailableCopies(int nb, Long idBook);
}
