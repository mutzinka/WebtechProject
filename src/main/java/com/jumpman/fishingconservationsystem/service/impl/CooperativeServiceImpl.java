package com.jumpman.fishingconservationsystem.service.impl;

import com.jumpman.fishingconservationsystem.entity.Cooperative;
import com.jumpman.fishingconservationsystem.entity.Fish;
import com.jumpman.fishingconservationsystem.repository.CooperativeRepo;
import com.jumpman.fishingconservationsystem.service.CooperativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class CooperativeServiceImpl implements CooperativeService {
    private final CooperativeRepo cooperativeRepo;
    public CooperativeServiceImpl(CooperativeRepo cooperativeRepo){
        this.cooperativeRepo=cooperativeRepo;
    }
    @Override
    public void uploadFish(List<Fish> fishes) {

    }

    @Override
    public List<Fish> getFishesUploaded() {
        return null;
    }

    @Override
    public List<Fish> printFishReport() {
        return null;
    }

    @Override
    public Cooperative createCooperative(Cooperative cooperative) {
       return cooperativeRepo.save(cooperative);
    }

    @Override
    public Integer updateCooperative(String username, Cooperative cooperative) {
        return cooperativeRepo.updateCooperativeByUsername(cooperative.getUsername(), cooperative.getEmail(),
                cooperative.getPassword(),cooperative.getStartedDate(),
                cooperative.getPhoneNumber(),cooperative.getCity(),
                cooperative.getStreet(),cooperative.getJoinedDate(),cooperative.getThumbnail(),cooperative.getUsername());
    }

    @Override
    public List<Fish> viewSoldFish() {
        return null;
    }

    @Override
    public void deleteCooperative(Integer coopId) {
        cooperativeRepo.deleteById(coopId);

    }

    @Override
    public Cooperative findByUsername(String username) {
        return cooperativeRepo.findByUsername(username);
    }

    @Override
    public List<Cooperative> getCooperatives() {
        return cooperativeRepo.findAll();
    }

    @Override
    public Cooperative getCooperativeById(Integer id) {
        return cooperativeRepo.getById(id);
    }

    @Override
    public void updateCooperativeByAdmin(Cooperative savedCoop) {
        cooperativeRepo.save(savedCoop);
    }


}
