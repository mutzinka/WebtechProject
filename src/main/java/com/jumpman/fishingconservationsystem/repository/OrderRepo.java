package com.jumpman.fishingconservationsystem.repository;

import com.jumpman.fishingconservationsystem.entity.Cooperative;
import com.jumpman.fishingconservationsystem.entity.FishStore;
import com.jumpman.fishingconservationsystem.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order,Integer> {
    List<Order>findByCooperative(Cooperative cooperative);
}
