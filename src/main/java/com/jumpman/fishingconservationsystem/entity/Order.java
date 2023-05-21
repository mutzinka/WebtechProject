package com.jumpman.fishingconservationsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "orders_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    @Column(name = "fish_type")
    private String fishType;

    @Column(name = "fish_number_ordered")
    private Double fishNumberOrdered;


    private Double amount;

    @Column(name = "payment_method")
    private String paymentMethod;

    private LocalDate dateOrderMade;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Cooperative cooperative;

    private Double orderedAmountOnItem;

}
