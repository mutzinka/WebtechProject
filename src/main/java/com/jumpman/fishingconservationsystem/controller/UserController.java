package com.jumpman.fishingconservationsystem.controller;


import com.jumpman.fishingconservationsystem.entity.Cooperative;
import com.jumpman.fishingconservationsystem.entity.FishStore;
import com.jumpman.fishingconservationsystem.entity.Role;
import com.jumpman.fishingconservationsystem.entity.User;
import com.jumpman.fishingconservationsystem.service.*;
import com.jumpman.fishingconservationsystem.utils.FileUploadUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.io.IOException;


@Controller
public class UserController {
    private final UserService userService;
    private final RoleService roleService;
    private final FishService fishService;
    private final FishStoreService fishStoreService;
    @Autowired
    private CooperativeService cooperativeService;

    public UserController(UserService userService, RoleService roleService, FishService fishService, FishStoreService fishStoreService) {
        this.userService = userService;
        this.roleService = roleService;
        this.fishService = fishService;
        this.fishStoreService = fishStoreService;

    }


    /*
     * retrieve first web form
     * */
    @GetMapping("/fishing/index")
    public String getIndexFishForm(@ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "index";
        }
        return "index";
    }

    // save user information
    @PostMapping("/fishing/index/save")
    public String getIndexFishFormValue(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        model.addAttribute("fishes", fishService.getFishes());
        Boolean hasErrors = resultHasErrosFromForm(result, model, user);
        if (hasErrors) {
            model.addAttribute("user", user);
            return "index";
        }
        User existingUser = userService.findByUsername(user.getUsername());
        if (existingUser != null) {
            model.addAttribute("usernames", "username taken ");
            return "index";
        }


        Role role = roleService.getRoleByName("user");
        user.setRole(role);
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));
        user.setPassword(hashedPassword);
        userService.createUserAccount(user);
        user.setPassword("");

        model.addAttribute("user", user);
        return "user_page";
    }

    //    getting the user form
    @GetMapping("/fishing/user")
    public String getUserForm(@ModelAttribute("user") User user, BindingResult result, Model model) {
        model.addAttribute("fishes", fishService.getFishes());
        User userFromDb = userService.findByUsername(user.getUsername());
        userFromDb.setPassword("");
        model.addAttribute("user", userFromDb);
        return "user_page";
    }

    //update user profile
    @PostMapping("/fishing/user/update")
    public String updateUserProfile(@Valid @ModelAttribute("user") User user, BindingResult result, @RequestParam("image") MultipartFile multipartFile, Model model) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        Boolean hasErrors = resultHasErrosFromForm(result, model, user);
        if (hasErrors) {
            model.addAttribute("user", user);
            model.addAttribute("image", "image upload error");
            return "user_page";
        }
        User existingUser = userService.findByUsername(user.getUsername());
        if (existingUser == null) {
            model.addAttribute("usernames", "user not exist ");
            return "user_page";
        }
        //setting a path where uploaded image should be located
        String uploadDir = "src/main/resources/static/img/user/" + existingUser.getUserId();
        if (!multipartFile.isEmpty()) {
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            //update the image path in db
            String actualImagePathInDb = "/img/user/" + existingUser.getUserId() + "/" + fileName;
            user.setThumbnail(actualImagePathInDb);
        }
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));
        user.setPassword(hashedPassword);
        userService.updateUserInfo(user.getUsername(), user);

        model.addAttribute("fishes", fishService.getFishes());
        user.setPassword("");
        model.addAttribute("user", user);
        return "user_page";

    }

    //get the user store form
    @GetMapping("/fishing/users")
    public String getUserStoreForm(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "userStore";
    }

    //get  login form
    @GetMapping("/fishing/user/login")
    public String getLoginForm(Model model) {
        model.addAttribute("fishes", fishService.getFishes());
        return "user_login";
    }

    //user login with credentials
    @PostMapping("/fishing/user/login")
    public String userLogin(@Valid @ModelAttribute("username") String username, @ModelAttribute("password") String password, BindingResult result, Model model) throws IOException {
        if (username.trim().isEmpty() && password.trim().isEmpty()) {
            model.addAttribute("usernameError", "please enter username ");
            model.addAttribute("passwordError", "provide your password");
            return "user_login";
        }
        if (username.trim().isEmpty()) {
            model.addAttribute("usernameError", "please enter username ");
            return "user_login";
        }

        if (password.trim().isEmpty()) {
            model.addAttribute("passwordError", "provide your password");
            return "user_login";
        }
        try {

            if (!result.hasErrors()) {
                User existingUser = userService.findByUsername(username);
                Cooperative existingCooperative = cooperativeService.findByUsername(username);
                if (existingUser != null) {
                    if (existingUser.getUsername().equals(username) && BCrypt.checkpw(password, existingUser.getPassword()) && existingUser.getRole().getRoleName().equals("admin")) {
                        return "redirect:/fishing/dash?username=" + username;
                    } else if (existingUser.getUsername().equals(username) && BCrypt.checkpw(password, existingUser.getPassword())) {
                        model.addAttribute("fishes", fishService.getFishes());
                        User userFromDb = userService.findByUsername(username);
                        userFromDb.setPassword("");
                        model.addAttribute("user", userFromDb);

                        return "user_page";
                    }
                } else if (existingCooperative != null) {
                    if (existingCooperative.getUsername().equals(username) && BCrypt.checkpw(password, existingCooperative.getPassword())) {
                        Cooperative cooperativeFromDb = cooperativeService.findByUsername(username);
                        cooperativeFromDb.setPassword("");
                        model.addAttribute("cooperative", cooperativeFromDb);
                        model.addAttribute("fishes", fishService.getFishes());
                        model.addAttribute("fishStores", fishStoreService.getFishUploadByCooperative(username));
                        FishStore fishStore = new FishStore();
                        model.addAttribute("fishStore", fishStore);
                        return "cooperative";
                    }
                }
            }
        } catch (IllegalArgumentException ex) {

            model.addAttribute("loginError", "invalid username/password");
        }
        return "user_login";
    }


    // removeUser , removes a particular use by id
    @GetMapping("/fishing/user/del/{userId}")
    public String removeUser(@PathVariable Integer userId) {
        userService.deleteUserAccount(userId);
        return "redirect:/fishing/users";
    }

    // resultHasErrosFromForm , check errors from the form
    private Boolean resultHasErrosFromForm(BindingResult result, Model model, User user) {
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return true;
        }
        return false;
    }
}


//    public void filterChain(HttpSecurity http) throws Exception {
//
//
//
//        http.authorizeRequests()
//                .antMatchers("/css/*.css", "/js/*.js").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/fishing/index")
//                .permitAll()
//                .and()
//                .logout()
//                .permitAll();
//
//
//
////                .logout(logout -> logout
////                        .logoutUrl("/fishing/logout")
////                        .logoutSuccessUrl("/fishing/index")
//////                        .logoutSuccessHandler(logoutSuccessHandler)
////                        .invalidateHttpSession(true)
//////                        .addLogoutHandler(logoutHandler)
//////                        .deleteCookies(cookieNamesToClear)
////                );
//    }
//
////    protected void configure(HttpSecurity http) throws Exception {
////        http
////                .logout(logout -> logout
////                        .logoutUrl("/my/logout")
////                        .logoutSuccessUrl("/home")
////                        .logoutSuccessHandler(logoutSuccessHandler)
////                        .invalidateHttpSession(true)
////                        .addLogoutHandler(logoutHandler)
////
////                );
////    }
////    https://www.codejava.net/frameworks/spring-boot/spring-security-logout-success-handler-example
//}
