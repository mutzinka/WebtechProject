package com.jumpman.fishingconservationsystem.controller;

import com.jumpman.fishingconservationsystem.entity.Role;
import com.jumpman.fishingconservationsystem.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }


    @GetMapping("/fishing/role/del/{roleId}")
    public String deleteRoleById(@PathVariable Integer roleId, Model model){
        roleService.removeRole(roleId);
        model.addAttribute("roles",roleService.getRoles());
        return "roles";
    }


    @GetMapping("/fishing/roles")
    public String roles( Model model){
        model.addAttribute("roles",roleService.getRoles());
        return "roles";
    }
}
