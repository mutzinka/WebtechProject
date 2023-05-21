package com.jumpman.fishingconservationsystem.controller;

import com.jumpman.fishingconservationsystem.entity.*;
import com.jumpman.fishingconservationsystem.service.FishService;
import com.jumpman.fishingconservationsystem.service.FishStoreService;
import com.jumpman.fishingconservationsystem.service.RoleService;
import com.jumpman.fishingconservationsystem.service.UserService;
import com.jumpman.fishingconservationsystem.utils.FileUploadUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
public class AdminController {
    private final FishService fishService;
    private final FishStoreService fishStoreService;
    private final RoleService roleService;
    private final UserService userService;
    public AdminController(FishService fishService,FishStoreService fishStoreService,RoleService roleService,UserService userService){

        this.fishService=fishService;
        this.fishStoreService=fishStoreService;
        this.roleService=roleService;
        this.userService=userService;
    }
    //getting the admin form
    @GetMapping("/fishing/dash")
    public String getAdminForm(Model model){
        model.addAttribute("fishStores",fishStoreService.viewFishesInStore());
        return "adminIndex";
    }


    //access the login page
    @GetMapping("/fishing/login")
    public String getLoginForm(Model model){

        return "login";
    }

    //user login with credentials
    @PostMapping("/fishing/login")
    public String userLogin(@Valid @ModelAttribute("username") String username,@ModelAttribute("password") String password,BindingResult result,Model model){
        if(username.trim().isEmpty() && password.trim().isEmpty()){
            model.addAttribute("usernameError","please enter username ");
            model.addAttribute("passwordError","provide your password");
            return "login";
        }
        if(username.trim().isEmpty()){
            model.addAttribute("usernameError","please enter username ");
            return "login";
        }

        if(password.trim().isEmpty()){
            model.addAttribute("passwordError","provide your password");
            return "login";
        }
        try{


        if(!result.hasErrors()){
            User existingUser=userService.findByUsername(username);
            if(existingUser!=null){
                if(existingUser.getUsername().equals(username) && BCrypt.checkpw(password,existingUser.getPassword()) && existingUser.getRole().getRoleName().equals("admin")){
                    return "redirect:/fishing/dash?username="+username;
                }
            }
        }
        }catch (IllegalArgumentException ex){
        model.addAttribute("loginError","invalid username/password");

        }
        return"login";
    }

    //register an admin or admin creating user account
    @GetMapping("/fishing/admin/user/register")
    public String getRegisterForm(@ModelAttribute("user") User user,BindingResult result, Model model){
        model.addAttribute("user",user);
        model.addAttribute("roles",roleService.getRoles());
        if(result.hasErrors()){
            return "register";
        }
        return "register";
    }

    //save user or admin
    @PostMapping("/fishing/admin/user/register/save")
    public String registerUserOrAdmin(@Valid @ModelAttribute("user")User user,BindingResult result, @RequestParam("roleId")String roleId,  Model model){
        if(result.hasErrors()){
            model.addAttribute("user",user);
            model.addAttribute("roles",roleService.getRoles());
            return "register";
        }
        User existingUser=userService.findByUsername(user.getUsername());
        if(existingUser!=null){
            model.addAttribute("usr","username/email taken");
            model.addAttribute("roles",roleService.getRoles());
            return "register";
        }

        Role role=roleService.getRoleById(Integer.parseInt(roleId));
        user.setRole(role);
        String hashedPassword= BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(10));
        user.setPassword(hashedPassword);
        userService.createUserAccount(user);
        model.addAttribute("user",user);
        return "adminIndex";
    }


    //page for password forget
    @GetMapping("/fishing/password")
    public String getPasswordForm(Model model){

        return "password";
    }


    //page for table forms
    @GetMapping("/fishing/tables")
    public String getFishingTables(Model model){
        model.addAttribute("fishStores",fishStoreService.viewFishesInStore());
        return "tables";
    }
    // getFishingChartsForm retrieves the chars form
    @GetMapping("/fishing/charts")
    public String getFishingChartsForm(Model model){

        return "charts";
    }


    //get the fishes Types form
    @GetMapping("/fishing/fish")
    public String getFishTypes(Model model){
        model.addAttribute("fishes",fishService.getFishes());
        return "fishes";
    }

    //get Fish Form by Admin
    @GetMapping("/fishing/admin/fish/form")
    public String registerNewFishForm(@ModelAttribute("fish") Fish fish,BindingResult result,Model model){
        if(result.hasErrors()){
            model.addAttribute("fish",fish);
            return "registerFish";
        }
        return "registerFish";
    }


    //register new Fish
    @PostMapping("/fishing/admin/fish/save")
    public String registerNewFish(@Valid @ModelAttribute("fish")Fish fish, BindingResult result ,
                                  @RequestParam("image")MultipartFile multipartFile, Model model) throws IOException {
        String fileName= multipartFile.getOriginalFilename();
        fish.setThumbnail(fileName);

        if(result.hasErrors()){
            model.addAttribute("fish",fish);
            model.addAttribute("image","image upload error");
            return "registerFish";
        }
        Fish existingFish=fishService.getFishByName(fish.getName());
        if(existingFish!=null){
            model.addAttribute("name","fish already registered ");
            return "registerFish";
        }
        fish.setRecordedAt(LocalDate.now());
       Fish savedFish= fishService.registerFish(fish);
       //setting a path where uploaded image should be located
        String uploadDir="src/main/resources/static/img/"+savedFish.getId();
        FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        //update the image path in db
        String actualImagePathInDb="/img/"+savedFish.getId()+"/"+ savedFish.getThumbnail();
        savedFish.setThumbnail(actualImagePathInDb);
        fishService.updateFish(savedFish);
        return "redirect:/fishing/fish";
    }




    //get new role fom
    @GetMapping("/fishing/admin/newrole")
    public String getNewRoleForm(@ModelAttribute("role") Role role,Model model){
        return "role_record";
    }
    //populate roles in store
    @GetMapping("/fishing/admin/rolestore")
    public String populateRoles(Model model){
        model.addAttribute("roles",roleService.getRoles());
        return "roles";
    }
    //record new role
    @PostMapping("/fishing/admin/newrole/save")
    public String registerNewRole(@Valid @ModelAttribute("role") Role role,BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("role",role);
            return"role_record";
        }
        Role existingRole=roleService.getRoleByName(role.getRoleName());
        if(existingRole!=null){
            model.addAttribute("roleName","role already recorded!");
            return "role_record";
        }

        roleService.createRole(role);
        return"redirect:/fishing/admin/rolestore";
    }
    // deleteFishType removes a fish by Id
    @GetMapping("/fishing/fish/del/{id}")
    public String deleteFishType(@PathVariable Integer id){
        fishService.deleteFish(id);
        return"redirect:/fishing/dash";
    }
    // updateFishForm redirect to updating form
    @GetMapping("/fishing/admin/fish/edit/{id}")
    public String updateFishForm(@PathVariable Integer id,Model model){
        model.addAttribute("fish",fishService.getSingleFishById(id));
    return "updateFish";
    }
    // updateFish updates an existing fish
    @PostMapping("/fishing/admin/fish/update/{id}")
    public String updateFish(@PathVariable Integer id,@Valid @ModelAttribute("fish")Fish fish, BindingResult result ,
                                  @RequestParam("image")MultipartFile multipartFile, Model model) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
//        fish.setThumbnail(fileName);

        if(result.hasErrors()){
            model.addAttribute("fish",fish);
            model.addAttribute("image","image upload error");
            return "registerFish";
        }
        Fish existingFish=fishService.getSingleFishById(id);
        fish.setRecordedAt(existingFish.getRecordedAt());

        if(existingFish==null){
            model.addAttribute("name","fish doesn't exist");
            return "updateFish";
        }
        if (!multipartFile.isEmpty()){

            //setting a path where uploaded image should be located
            String uploadDir="src/main/resources/static/img/"+existingFish.getId();
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
            //update the image path in db
            String actualImagePathInDb="/img/"+existingFish.getId()+"/"+ fileName;
            fish.setThumbnail(actualImagePathInDb);
        }else {
            fish.setThumbnail(existingFish.getThumbnail());
        }
        fish.setId(id);
        fishService.updateFish(fish);
        return "redirect:/fishing/fish";
    }

    // updateUserForm get form for updating user info
    @GetMapping("/fishing/admin/user/edit/{id}")
    public String updateUserForm(@PathVariable Integer id ,Model model){
        User existingUser=userService.findUserById(id);
        existingUser.setPassword("");
        model.addAttribute("user",existingUser);
        model.addAttribute("role",roleService.getRoles());
        return "userAdminUpdate";
    }

    // updateUserOrAdmin updates the user info
    @PostMapping("/fishing/admin/userAdmin/update/{id}")
    public String updateUserOrAdmin(@PathVariable Integer id,@Valid @ModelAttribute("user")User user,BindingResult result, @RequestParam("roleId")String roleId,  Model model){
        if(result.hasErrors()){
            model.addAttribute("user",user);
            model.addAttribute("role",roleService.getRoles());
            return "userAdminUpdate";
        }
        User existingUser=userService.findByUsername(user.getUsername());
        if(existingUser!=null){
            model.addAttribute("usr","username/email taken");
            model.addAttribute("role",roleService.getRoles());
            return "userAdminUpdate";
        }
        User existToUpdate=userService.findUserById(id);
        user.setUserId(id);
        user.setThumbnail(existToUpdate.getThumbnail());
        user.setCity(existToUpdate.getCity());
        user.setStreet(existToUpdate.getStreet());
        user.setDob(existToUpdate.getDob());
        Role role=roleService.getRoleById(Integer.parseInt(roleId));
        user.setRole(role);
        String hashedPassword= BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(10));
        user.setPassword(hashedPassword);
        userService.updateUserAdmin(user);
        model.addAttribute("user",user);
        return "redirect:/fishing/users";
    }


}
