package com.jumpman.fishingconservationsystem.service.impl;

import com.jumpman.fishingconservationsystem.entity.Role;
import com.jumpman.fishingconservationsystem.repository.RoleRepo;
import com.jumpman.fishingconservationsystem.service.RoleService;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class RoleServiceImpl implements RoleService {
    private final RoleRepo roleRepo;
    public RoleServiceImpl(RoleRepo roleRepo){
        this.roleRepo=roleRepo;

    }
    @Override
    public Role createRole(Role role) {
        return roleRepo.save(role);
    }



    @Override
    public List<Role> getRoles() {
        return roleRepo.findAll();
    }

    @Override
    public Role updateRole(Integer roleId, String roleName) {
        return null;
    }

    @Override
    public void removeRole(Integer roleId) {
        roleRepo.deleteById(roleId);
    }

    @Override
    public Role getRoleByName(String roleName) {
        return roleRepo.findByRoleName(roleName);
    }

    @Override
    public Role getRoleById(Integer roleId) {
        return roleRepo.getById(roleId);
    }
}
