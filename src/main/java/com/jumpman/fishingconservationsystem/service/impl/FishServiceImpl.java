package com.jumpman.fishingconservationsystem.service.impl;

import com.jumpman.fishingconservationsystem.entity.Fish;
import com.jumpman.fishingconservationsystem.repository.FishRepo;
import com.jumpman.fishingconservationsystem.service.FishService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FishServiceImpl implements FishService {
    private final FishRepo fishRepo;
    public FishServiceImpl(FishRepo fishRepo){
        this.fishRepo=fishRepo;
    }
    @Override
    public Fish registerFish(Fish fish) {
        return fishRepo.save(fish);
    }

    @Override
    public Fish updateFish(Fish fish) {
        return fishRepo.save(fish);
    }

    @Override
    public List<Fish> getFishes() {
        return fishRepo.findAll();
    }

    @Override
    public Fish getSingleFishById(Integer id) {
        return fishRepo.getById(id);
    }

    @Override
    public void deleteFish(Integer fishId) {
    }

    @Override
    public Fish getFishByName(String name) {
        return fishRepo.findByName(name);
    }

}
