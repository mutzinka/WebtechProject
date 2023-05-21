package com.jumpman.fishingconservationsystem.service.impl;

import com.jumpman.fishingconservationsystem.entity.User;
import com.jumpman.fishingconservationsystem.repository.UserRepo;
import com.jumpman.fishingconservationsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void createUserAccount(User user) {
        userRepo.save(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    @Override
    public Integer updateUserInfo(String username, User user) {
        return userRepo.updateUserByUsername(user.getFirstName(),user.getLastName(),user.getPassword()
        ,user.getEmail(),user.getDob(),user.getPhoneNumber(),user.getCity(),user.getStreet(),user.getThumbnail(),user.getUsername());
    }

    @Override
    public void deleteUserAccount(Integer userId) {
        userRepo.deleteById(userId);


    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public User findUserById(Integer id) {
        return userRepo.getById(id);
    }

    @Override
    public void updateUserAdmin(User user) {
        userRepo.save(user);
    }
}
