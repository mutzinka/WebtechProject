package com.jumpman.fishingconservationsystem.repository;

import com.jumpman.fishingconservationsystem.entity.Cooperative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CooperativeRepo extends JpaRepository<Cooperative,Integer> {
    Cooperative findByUsername(String username);

    //updating cooperative

    @Modifying //to make it modify to the db table we add this annotation
    @Transactional //to make the comit to db
    @Query(
            value="update cooperative_table set cooperative_name=?1,email=?2,password=?3,started_date=?4,phone_number=?5,city=?6,street=?7,joined_date=?8,thumbnail=?9 where username=?10",
            nativeQuery=true
    )
    int updateCooperativeByUsername(String name, String email,
                             String password, LocalDate startedDate, String phoneNumber,
                             String city, String street,LocalDate joinedDate, String thumbnail, String username);

}
