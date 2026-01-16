package com.douiou0.patientsmvc.entities;

import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Le nom ne peut pas être vide")
    @Size(min = 4, max = 40, message = "Le nom doit contenir entre 4 et 40 caractères")
    private String nom;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateNaissance;

    private boolean malade;

    // CORRECTION : Utilisez @Min au lieu de @DecimalMin pour int
    @Min(value = 100, message = "Le score ne peut pas etre moin de 100")
    private int score;
}
