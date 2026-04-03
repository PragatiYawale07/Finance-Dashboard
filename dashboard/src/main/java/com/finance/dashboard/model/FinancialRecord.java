package com.finance.dashboard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Entity        // Financial record entity for income/expense data
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Double amount;
    @NotBlank
    private String type; // INCOME / EXPENSE
    private String category;
    private String description;
    @NotNull
    private LocalDate date;
}