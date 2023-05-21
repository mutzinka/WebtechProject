package com.jumpman.fishingconservationsystem.rest;


import com.jumpman.fishingconservationsystem.entity.Fish;
import com.jumpman.fishingconservationsystem.entity.FishStore;
import com.jumpman.fishingconservationsystem.service.FishService;
import com.jumpman.fishingconservationsystem.service.FishStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fishing/api")
public class FishRestController {
    @Autowired
    private  FishStoreService fishStoreService;
    @Autowired
    private FishService fishService;
    @GetMapping("/fishes")
    public ResponseEntity<List<FishStore>> getAllFishes() {
        List<FishStore> fishStoreList=fishStoreService.viewFishesInStore();
        return new ResponseEntity<>(fishStoreList, HttpStatus.OK);
    }


    @GetMapping("/fishTypes")
    public ResponseEntity<List<Fish>> getFishes() {
        List<Fish> fishList=fishService.getFishes();
        return new ResponseEntity<>(fishList, HttpStatus.OK);
    }
}
