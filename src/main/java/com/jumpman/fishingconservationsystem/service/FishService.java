package com.jumpman.fishingconservationsystem.service;

import com.jumpman.fishingconservationsystem.entity.Fish;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FishService {
    Fish registerFish(Fish fish);
    Fish updateFish (Fish fish);
    List<Fish> getFishes();
    Fish getSingleFishById(Integer id);
    void deleteFish(Integer fishId);
    Fish getFishByName(String name);
}
