package com.jumpman.fishingconservationsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "user_table",
        uniqueConstraints = {

            @UniqueConstraint(
                    name = "email_unique",
                    columnNames = "email"
            ),

                @UniqueConstraint(
                        name ="username_unique",
                        columnNames = "username"
                )

        }

)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @NotBlank(message = "username can not be blank")
    @NotEmpty(message = "username must be entered")
    private String username;

    @Column(name = "first_name")
    @NotBlank(message = "first name can not be blank")
    @NotEmpty(message = "first name must be entered")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "last name can not be blank")
    @NotEmpty(message = "last name must be entered")
    private String lastName;

    @Column(name = "password")
    @NotBlank(message = "password name can not be blank")
    @NotEmpty(message = "password must be entered")
    @Size(min = 8,max = 200,message = "password must be 8 characters or more")
    private String password;

    @Column(name = "email")
    @Email(message = "enter a valid email")
    @NotBlank(message = "email can not be blank")
    @NotEmpty(message = "email must be entered")
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    @Column(name = "phone_number")
    @NotBlank(message = "phone number can not be blank")
    @NotEmpty(message = "phone number must be entered")
    private String phoneNumber;

    @Column(name = "city")

    private String city;

    @Column(name = "street")

    private String street;

    private String thumbnail;

    @OneToOne
    @JoinColumn(name = "role_role_id")
    private Role role;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user",fetch = FetchType.EAGER)
    private List<Order> orders;


}
