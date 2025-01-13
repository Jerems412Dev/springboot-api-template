package com.jeremsdev.validations.repository;

import com.jeremsdev.validations.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUserIdUser(Long idUser);
    List<Loan> findByBookIdBook(Long idBook);
}
