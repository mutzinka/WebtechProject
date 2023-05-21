package com.jumpman.fishingconservationsystem.service;

import com.jumpman.fishingconservationsystem.entity.Role;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface RoleService {
    Role createRole(Role role);
    List<Role> getRoles();
    Role updateRole(Integer roleId,String roleName);
    void removeRole(Integer roleId);
    Role getRoleByName(String roleName);
    Role getRoleById(Integer roleId);

}
