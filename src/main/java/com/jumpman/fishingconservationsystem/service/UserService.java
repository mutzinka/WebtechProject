package com.jumpman.fishingconservationsystem.service;

import com.jumpman.fishingconservationsystem.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    void createUserAccount(User user);
    List<User> getUsers();
    Integer updateUserInfo(String username,User user);
    void deleteUserAccount(Integer userId);
    User findByUsername(String username);

    User findUserById(Integer id);

    void updateUserAdmin(User user);
}
