package com.jumpman.fishingconservationsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "cooperative_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cooperative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coop_id")
    private Integer coopId;

    @Column(name = "cooperative_name")
    @NotEmpty(message = "cooperative name must be entered")
    @NotBlank(message = "cooperative name can't be blank")
    private String name;

    @NotBlank(message = "cooperative username can't be blank")
    @NotEmpty(message = "cooperative username must be entered")
    private String username;

    @NotBlank(message = "cooperative email can't be blank")
    @NotEmpty(message = "cooperative email must be provided")
    @Email(message = "enter a valid email")
    private String email;

    @NotBlank(message = "cooperative password can't be blank")
    @NotEmpty(message = "cooperative password must be provided")
    @Size(min = 8,message = "password must contains 8 characters and above")
    private String password;

    @Column(name = "started_date")
//    @NotEmpty(message = "cooperative started date must be provided")
//    @NotBlank(message = "cooperative started can't be blank")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startedDate;

    @Column(name = "phone_number")
    @NotEmpty(message = "cooperative phone must be provided")
    @NotBlank(message = "cooperative phone can't be blank")
    private String phoneNumber;

    private String city;

    private String street;

    @Column(name = "joined_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinedDate;

    private String thumbnail;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "cooperative",fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<FishStore> fishStoreList;

//    @OneToMany(cascade = CascadeType.ALL,mappedBy = "cooperative",fetch = FetchType.EAGER)
//    private List<Order> orders;
}
