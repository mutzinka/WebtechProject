package com.jumpman.fishingconservationsystem.repository;

import com.jumpman.fishingconservationsystem.entity.Fish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FishRepo extends JpaRepository<Fish,Integer> {
    Fish findByName(String name);
}
