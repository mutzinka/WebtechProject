package com.jumpman.fishingconservationsystem.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name="fish_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Fish {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "fish_id",nullable = false)
    private Integer id;

    @NotEmpty(message = "fish name must be entered")
    @NotBlank(message = "fish name must be not be empty")
    @Column(name="fish_name",nullable = false)
    private String name;



    @Column(name="fish_weight",nullable = true)
    @NotNull(message = "weight can't be null")
    private Double fishWeight;

    @Column(name="rocorded_at" ,nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recordedAt;


    @NotNull(message = "price can't be null")
    private Double price;
    @Column(nullable = true)
    private String thumbnail;
}
