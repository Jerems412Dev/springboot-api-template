package com.jeremsdev.validations.dto;

import com.jeremsdev.validations.model.Categories;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookDTO {
    private Long idBook;
    private String title;
    private String author;
    private int availableCopies;
    private int countPages;
    private Categories category;
}
