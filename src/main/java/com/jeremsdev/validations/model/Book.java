package com.jeremsdev.validations.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBook;
    @Column(nullable = false, length = 80)
    private String title;
    @Column(nullable = false, length = 80)
    private String author;
    private int availableCopies;
    @Column(nullable = false)
    private int countPages;
    @Column(nullable = false)
    private Categories category;

    //relations
    @OneToMany
    @JoinColumn(name = "loan_id")
    private Set<Loan> loans;
}
