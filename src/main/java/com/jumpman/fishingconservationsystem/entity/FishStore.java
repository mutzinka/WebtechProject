package com.jumpman.fishingconservationsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "fish_store_tables")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FishStore {

    @Column(name = "store_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer storeId;

    @OneToOne
    @JoinColumn(name = "fish_fish_id")
    private Fish fish;

    @Column(name = "fish_quantity")
    @NotNull(message = "quantity must be entered")
    private Double fishQuantity;

    @Column(name = "remained_fish")
    private Double remainedFish;

    @Column(name = "sold_fish")
    private Double soldFish;

    private Double amount;
    @JsonIgnore
    @ManyToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Cooperative cooperative;

    private LocalDate dateFishUploaded;

    private LocalDate lastDayFishSold;

    private Integer orderedNumber;
}

