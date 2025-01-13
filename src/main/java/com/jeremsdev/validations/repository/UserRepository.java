package com.jeremsdev.validations.repository;

import com.jeremsdev.validations.model.Loan;
import com.jeremsdev.validations.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByIdUser(Long id);
}
