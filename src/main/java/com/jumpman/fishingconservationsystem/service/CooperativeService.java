package com.jumpman.fishingconservationsystem.service;

import com.jumpman.fishingconservationsystem.entity.Cooperative;
import com.jumpman.fishingconservationsystem.entity.Fish;
import com.jumpman.fishingconservationsystem.entity.FishStore;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface CooperativeService {
    void uploadFish(List<Fish> fishes);
    List<Fish> getFishesUploaded();
    List<Fish> printFishReport();
    Cooperative createCooperative(Cooperative cooperative);
    Integer updateCooperative(String username,Cooperative cooperative);
    List<Fish> viewSoldFish();
    void deleteCooperative(Integer coopdId);
    Cooperative findByUsername(String username);

    List<Cooperative> getCooperatives();

    Cooperative getCooperativeById(Integer id);

    void updateCooperativeByAdmin(Cooperative savedCoop);
}
