package com.jumpman.fishingconservationsystem.repository;

import com.jumpman.fishingconservationsystem.entity.Refound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefoundRepo extends JpaRepository<Refound,Integer> {
}
