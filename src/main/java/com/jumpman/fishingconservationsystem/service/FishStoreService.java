package com.jumpman.fishingconservationsystem.service;

import com.jumpman.fishingconservationsystem.entity.Cooperative;
import com.jumpman.fishingconservationsystem.entity.Fish;
import com.jumpman.fishingconservationsystem.entity.FishStore;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public interface FishStoreService {
    List<FishStore> viewFishesInStore();
    FishStore addFishInStore(FishStore fishstore);
    FishStore updateStore(FishStore store);
    void removeItemInStore(Integer storeId);
    List<FishStore> getFishUploadByCooperative(String username);

    List<FishStore> getFishesByName(String fishName);

    void uploadFishes(List<FishStore> fishStoreList);

    List<FishStore> generateReport(LocalDate reportFrom, LocalDate reportTo,Integer coopId);
    FishStore findCooperativeByFish(Integer fishId);
}
