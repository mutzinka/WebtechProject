package com.jumpman.fishingconservationsystem.controller;


import com.jumpman.fishingconservationsystem.service.FishStoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FishStoreController {
    private final FishStoreService fishStoreService;
    public FishStoreController(FishStoreService fishStoreService){
        this.fishStoreService=fishStoreService;
    }


    //admin removing and item in the store
    @GetMapping("/fishing/fstore/del/{storeId}")
    public String adminRemoveItemInStore(@PathVariable Integer storeId, Model model){
        fishStoreService.removeItemInStore(storeId);
        return "redirect:/fishing/dash";
    }

    //cooperative removing and item in the store
    @GetMapping("/fishing/fstore/coop/del/{storeId}")
    public String cooperativeRemoveItemInStore(@PathVariable Integer storeId, @RequestParam(required = false,name = "coopUsername")String coopUsername, Model model){
        fishStoreService.removeItemInStore(storeId);
        model.addAttribute("cooperative",fishStoreService.getFishUploadByCooperative(coopUsername));
        return "redirect:/fishing/coop?coopUsername="+coopUsername;

    }
}
