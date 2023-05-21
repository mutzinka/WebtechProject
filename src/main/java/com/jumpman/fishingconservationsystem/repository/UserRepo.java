package com.jumpman.fishingconservationsystem.repository;

import com.jumpman.fishingconservationsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {
    //updating user

    @Modifying //to make it modify to the db table we add this annotation
    @Transactional //to make the comit to db
    @Query(
            value="update user_table set first_name=?1,last_name=?2,password=?3,email=?4,dob=?5,phone_number=?6,city=?7,street=?8,thumbnail=?9 where username=?10",
            nativeQuery=true
    )
    int updateUserByUsername(String firstName, String lastName, String password,
                                   String email,LocalDate dob,String phoneNumber,
                                   String city,String street,String thumbnail,String username);


    /*
    * @param username
    * returns User entity
    * */
    User findByUsername(String username);
}
