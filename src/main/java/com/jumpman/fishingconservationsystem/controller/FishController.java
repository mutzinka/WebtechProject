package com.jumpman.fishingconservationsystem.controller;


import com.jumpman.fishingconservationsystem.entity.User;
import com.jumpman.fishingconservationsystem.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class FishController {

    private final UserService userService;
    public FishController(UserService userService){
        this.userService=userService;
    }






}
