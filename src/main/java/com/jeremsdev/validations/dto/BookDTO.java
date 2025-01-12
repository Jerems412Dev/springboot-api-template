package com.jeremsdev.validations.dto;

import com.jeremsdev.validations.model.Categories;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookDTO {
    @NonNull
    private Long idBook;
    @NonNull
    private String title;
    @NonNull
    private String author;
    private int availableCopies;
    private int countPages;
    @NonNull
    private Categories category;
    private Set<LoanDTO> loans;
}
