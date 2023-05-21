package com.jumpman.fishingconservationsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "refound_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Refound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refound_id")
    private Integer refoundId;

    @Column(name = "refound_date")
    private LocalDate refoundDate;

}
