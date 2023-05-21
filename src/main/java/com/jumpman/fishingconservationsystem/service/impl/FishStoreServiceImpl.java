package com.jumpman.fishingconservationsystem.service.impl;

import com.jumpman.fishingconservationsystem.entity.Cooperative;
import com.jumpman.fishingconservationsystem.entity.Fish;
import com.jumpman.fishingconservationsystem.entity.FishStore;
import com.jumpman.fishingconservationsystem.repository.FishRepo;
import com.jumpman.fishingconservationsystem.repository.FishStoreRepo;
import com.jumpman.fishingconservationsystem.service.FishStoreService;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class FishStoreServiceImpl implements FishStoreService {
    private final FishRepo fishRepo;
    private final FishStoreRepo fishStoreRepo;
    public EntityManager entityManager;
    public FishStoreServiceImpl(FishRepo fishRepo,FishStoreRepo fishStoreRepo){
        this.fishStoreRepo=fishStoreRepo;
        this.fishRepo=fishRepo;
    }
    @Override
    public List<FishStore> viewFishesInStore() {

        return fishStoreRepo.findAll();
    }

    @Override
    public FishStore addFishInStore(FishStore fishstore) {
        return fishStoreRepo.save(fishstore);
    }



    @Override
    public FishStore updateStore(FishStore store) {
        return fishStoreRepo.save(store);
    }

    @Override
    public void removeItemInStore(Integer storeId) {
        fishStoreRepo.deleteByStoreId(storeId);
    }

    @Override
    public List<FishStore> getFishUploadByCooperative(String username) {
        return fishStoreRepo.getFishUploadByCooperative(username);
    }

    @Override
    public List<FishStore> getFishesByName(String fishName) {
        return fishStoreRepo.getFishesByName(fishName);
    }

    @Override
    public void uploadFishes(List<FishStore> fishStoreList) {
            fishStoreRepo.saveAll(fishStoreList);
    }
    @Override
    public List<FishStore> generateReport(LocalDate reportFrom, LocalDate reportTo,Integer coopId) {
        return fishStoreRepo.generateReportFromRepo(reportFrom,reportTo,coopId);
    }

    @Override
    public FishStore findCooperativeByFish(Integer fishId) {
        return fishStoreRepo.getCooperativeByFish(fishId);
    }


}
