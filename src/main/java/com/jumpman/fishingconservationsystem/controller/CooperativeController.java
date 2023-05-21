package com.jumpman.fishingconservationsystem.controller;

import com.jumpman.fishingconservationsystem.entity.Cooperative;
import com.jumpman.fishingconservationsystem.entity.Fish;
import com.jumpman.fishingconservationsystem.entity.FishStore;
import com.jumpman.fishingconservationsystem.service.CooperativeService;
import com.jumpman.fishingconservationsystem.service.FishService;
import com.jumpman.fishingconservationsystem.service.FishStoreService;
import com.jumpman.fishingconservationsystem.service.ReportService;
import com.jumpman.fishingconservationsystem.utils.FileUploadUtil;
import com.jumpman.fishingconservationsystem.utils.UploadExcel;
import net.sf.jasperreports.engine.JRException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Controller
public class CooperativeController {

    private final CooperativeService cooperativeService;
    private final FishService fishService;
    private final FishStoreService fishStoreService;
    private final ReportService reportService;

    public CooperativeController(CooperativeService cooperativeService,FishService fishService,FishStoreService fishStoreService,ReportService reportService){
        this.cooperativeService=cooperativeService;
        this.fishService=fishService;
        this.fishStoreService=fishStoreService;
        this.reportService=reportService;
    }
    //cooperative index page
    @GetMapping("/fishing/coop")
    public String getCooperativePage(@ModelAttribute("fishStore") FishStore fishStore,@ModelAttribute("cooperative") Cooperative cooperative,BindingResult result,Model model,@RequestParam("coopUsername") String username){
        if(cooperativeService.findByUsername(username)==null){
            return "redirect:/fishing/user/login";
        }
        model.addAttribute("fishes",fishService.getFishes());
        model.addAttribute("fishStores",fishStoreService.getFishUploadByCooperative(username));
        Cooperative cooperativeFromDb=cooperativeService.findByUsername(username);
        cooperativeFromDb.setPassword("");
        model.addAttribute("cooperative",cooperativeFromDb);

        if(result.hasErrors()){
            model.addAttribute("fishStore",fishStore);
            return "cooperative";
        }
        return "cooperative";
    }

    //upload single or many fishes by cooperative
    @PostMapping("/fishing/coop/fish/save")
    public String uploadFish(@Valid @ModelAttribute("fishStore")FishStore fishStore, BindingResult result,
                             @RequestParam("singleFish")String singleFish, @RequestParam("coopUsername")String coopUsername,
                             Model model){
        if(singleFish.trim().isEmpty()){
            model.addAttribute("fishes",fishService.getFishes());
            model.addAttribute("fishStores",fishStoreService.getFishUploadByCooperative(coopUsername));
            model.addAttribute("singleFish","select fish type");
            model.addAttribute("fishStore",fishStore);
            return "cooperative";
        }

        if(result.hasErrors()){
            model.addAttribute("fishes",fishService.getFishes());
            model.addAttribute("fishStores",fishStoreService.getFishUploadByCooperative(coopUsername));
            model.addAttribute("fishStore",fishStore);
            return "cooperative";
        }
        fishStore.setDateFishUploaded(LocalDate.now());
        Fish fish=fishService.getSingleFishById(Integer.parseInt(singleFish));
        fishStore.setFish(fish);
        Cooperative cooperative=cooperativeService.findByUsername(coopUsername);
        fishStore.setCooperative(cooperative);
        fishStore.setSoldFish(0.0);
        fishStore.setRemainedFish(fishStore.getFishQuantity());
        fishStoreService.addFishInStore(fishStore);
        model.addAttribute("fishes",fishService.getFishes());
        model.addAttribute("fishStores",fishStoreService.getFishUploadByCooperative(coopUsername));
        model.addAttribute("fishStore",fishStore);
        cooperative.setPassword("");
        model.addAttribute("cooperative",cooperative);
        return "cooperative";
    }
    //upload many fishes at once using exel file
    @PostMapping("/fishing/coop/fish/excel/save")
    public String uploadFishInExcelFile(@ModelAttribute("fishStore")FishStore fishStore, BindingResult result,@RequestParam("excelFile") MultipartFile multipartFile,
                                        @RequestParam("coopUsername")String coopUsername,Model model) throws Exception {
        String fileName=multipartFile.getOriginalFilename();
        UploadExcel uploadExcel=new UploadExcel(fishService,cooperativeService);
        Cooperative cooperative=cooperativeService.findByUsername(coopUsername);
        List<FishStore> fishStoreList=uploadExcel.excel(multipartFile,coopUsername);
        if(fishStoreList.isEmpty()){
            model.addAttribute("error","invalid fish type in system");
            model.addAttribute("fishes",fishService.getFishes());
            model.addAttribute("fishStores",fishStoreService.getFishUploadByCooperative(coopUsername));
            model.addAttribute("fishStore",fishStore);
            cooperative.setPassword("");
            model.addAttribute("cooperative",cooperative);
            return "cooperative";
        }
        fishStoreService.uploadFishes(fishStoreList);
        model.addAttribute("fishes",fishService.getFishes());
        model.addAttribute("fishStores",fishStoreService.getFishUploadByCooperative(coopUsername));
        model.addAttribute("fishStore",fishStore);
        cooperative.setPassword("");
        model.addAttribute("cooperative",cooperative);
        return "cooperative";
    }

    //register cooperative form
    @GetMapping("/fishing/coop/form")
    public String registerCooperativeForm(@ModelAttribute("cooperative") Cooperative cooperative,BindingResult result,Model model ){
        if(result.hasErrors()){
            model.addAttribute("cooperative",cooperative);
            return "register_cooperative";
        }

        return "register_cooperative";
    }

//    save a particular cooperative
    @PostMapping("/fishing/coop/save")
    public String saveCooperative(@Valid @ModelAttribute("cooperative")Cooperative cooperative, BindingResult result, @RequestParam("image") MultipartFile multipartFile, Model model) throws IOException {
        String fileName= multipartFile.getOriginalFilename();
        if(result.hasErrors()){
            model.addAttribute("cooperative",cooperative);
            model.addAttribute("image","image upload error");
            return "register_cooperative";
        }
        Cooperative existingCooperative=cooperativeService.findByUsername(cooperative.getUsername());
        if(existingCooperative!=null){
            model.addAttribute("usernames","username taken ");
            return "register_cooperative";
        }
        cooperative.setJoinedDate(LocalDate.now());
        String hashedPassword= BCrypt.hashpw(cooperative.getPassword(),BCrypt.gensalt(10));
        cooperative.setPassword(hashedPassword);
        Cooperative savedCoop=cooperativeService.createCooperative(cooperative);
        //setting a path where uploaded image should be located
        String uploadDir="src/main/resources/static/img/coop/"+savedCoop.getCoopId();
        if(!multipartFile.isEmpty()){

            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
            //update the image path in db
            String actualImagePathInDb="/img/coop/"+savedCoop.getCoopId()+"/"+ fileName;
            savedCoop.setThumbnail(actualImagePathInDb);
        }
        cooperativeService.updateCooperative(savedCoop.getUsername(),savedCoop);
        cooperative.setPassword("");
        model.addAttribute("cooperative",cooperative);
        model.addAttribute("fishStores",fishStoreService.getFishUploadByCooperative(cooperative.getUsername()));
        model.addAttribute("fishes",fishService.getFishes());
        FishStore fishStore=new FishStore();
        model.addAttribute("fishStore",fishStore);
        return "cooperative";

    }


    //update cooperative profile
    @PostMapping("/fishing/coop/update")
    public String updateCooperativeProfile(@Valid @ModelAttribute("cooperative") Cooperative cooperative,BindingResult result,  @RequestParam("image") MultipartFile multipartFile,Model model) throws IOException {
        String fileName= multipartFile.getOriginalFilename();
        if(result.hasErrors()){
            model.addAttribute("cooperative",cooperative);
            model.addAttribute("image","image upload error");
            return "cooperative";
        }
        Cooperative existingCooperative=cooperativeService.findByUsername(cooperative.getUsername());
        if(existingCooperative==null){
            model.addAttribute("usernames","user not exist ");
            return "cooperative";
        }
        //setting a path where uploaded image should be located
        String uploadDir="src/main/resources/static/img/coop/"+existingCooperative.getCoopId();
        if(!multipartFile.isEmpty()){

            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
            //update the image path in db
            String actualImagePathInDb="/img/coop/"+existingCooperative.getCoopId()+"/"+ fileName;
            cooperative.setThumbnail(actualImagePathInDb);
        }
        String hashedPassword= BCrypt.hashpw(cooperative.getPassword(),BCrypt.gensalt(10));
        cooperative.setPassword(hashedPassword);
        cooperativeService.updateCooperative(cooperative.getUsername(),cooperative) ;
        Cooperative cooperativeFromDb=cooperativeService.findByUsername(cooperative.getUsername());
        cooperativeFromDb.setPassword("");
        model.addAttribute("cooperative",cooperativeFromDb);
        model.addAttribute("fishes",fishService.getFishes());
        model.addAttribute("fishStores",fishStoreService.getFishUploadByCooperative(cooperative.getUsername()));
        FishStore fishStore=new FishStore();
        model.addAttribute("fishStore",fishStore);
        return "cooperative";

    }

    //get the cooperative form
    @GetMapping("/fishing/coops")
    public String getCoopStoreForm(Model model){
        model.addAttribute("cooperatives",cooperativeService.getCooperatives());
        return "coopStore";
    }

    // delete a cooperatice by passing its ID
    @GetMapping("/fishing/coop/del/{coopId}")
    public String removeCooperative(@PathVariable Integer coopId){
        cooperativeService.deleteCooperative(coopId);
        return "redirect:/fishing/coops";
    }

    //generate list of all cooperative store
    @GetMapping("/fishing/admin/report")
    public ResponseEntity<byte[]> generatePdf() throws JRException, FileNotFoundException {
        HttpHeaders headers=new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION,"inline;filename=fishStore.pdf");
        List<FishStore> fishStoreList=fishStoreService.viewFishesInStore();
        fishStoreList.get(0).getCooperative().setName("All Cooperatives");
        byte[]data=reportService.exportReport(fishStoreList);
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);

    }
    //generate cooperative Report
    @PostMapping("/fishing/coop/report")
    public ResponseEntity<byte[]>  cooperativeReport(@RequestParam("reportFrom")String reportFrom,@RequestParam("reportTo")String reportTo,
                                    @RequestParam("coopUsername")String coopUsername,Model model) throws JRException, FileNotFoundException {
        LocalDate rptFrom=LocalDate.parse(reportFrom);
        LocalDate rptTo=LocalDate.parse(reportTo);
        Cooperative cooperative=cooperativeService.findByUsername(coopUsername);
        List<FishStore> list=fishStoreService.generateReport(rptFrom,rptTo,cooperative.getCoopId());
        HttpHeaders headers=new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION,"inline;filename=fishStore.pdf");
        byte[]data=reportService.exportReport(list);
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);
    }

    // coopUpdateForm get form to update cooperative
    @GetMapping("/fishing/coop/edit/{id}")
    public String coopUpdateForm(@PathVariable Integer id,Model model){
        Cooperative existingCoop=cooperativeService.getCooperativeById(id);
        existingCoop.setPassword("");
        model.addAttribute("cooperative",existingCoop);
        return "cooperativeUpdate";

    }

    // updateAdminCoopByAdmin updates the cooperative info

    @PostMapping("/fishing/coop/updates/{id}")
    public String updateAdminCoopByAdmin(@PathVariable Integer id,@Valid @ModelAttribute("cooperative")Cooperative cooperative, BindingResult result, @RequestParam("image") MultipartFile multipartFile, Model model) throws IOException {
        String fileName= multipartFile.getOriginalFilename();
        if(result.hasErrors()){
            model.addAttribute("cooperative",cooperative);
            model.addAttribute("image","image upload error");
            return "cooperativeUpdate";
        }
        Cooperative existingCoop=cooperativeService.getCooperativeById(id);
        Cooperative existingCooperative=cooperativeService.findByUsername(cooperative.getUsername());
        if(!Objects.equals(existingCooperative.getCoopId(), existingCoop.getCoopId())){
            model.addAttribute("usernames","username taken ");
            return "cooperativeUpdate";
        }
        cooperative.setJoinedDate(LocalDate.now());

        cooperative.setStreet(existingCoop.getStreet());
        cooperative.setCity(existingCoop.getCity());
        cooperative.setCoopId(id);
        LocalDate startedDate=cooperative.getStartedDate();
        if (startedDate.isAfter(LocalDate.now())){
            model.addAttribute("startedDates","started can not be in future");
            return "cooperativeUpdate";
        }
        String hashedPassword= BCrypt.hashpw(cooperative.getPassword(),BCrypt.gensalt(10));
        cooperative.setPassword(hashedPassword);
        Cooperative savedCoop=cooperativeService.createCooperative(cooperative);
        //setting a path where uploaded image should be located
        String uploadDir="src/main/resources/static/img/coop/"+savedCoop.getCoopId();
        if(!multipartFile.isEmpty()){

            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
            //update the image path in db
            String actualImagePathInDb="/img/coop/"+savedCoop.getCoopId()+"/"+ fileName;
            savedCoop.setThumbnail(actualImagePathInDb);
        }
        cooperativeService.updateCooperativeByAdmin(savedCoop);
        cooperative.setPassword("");
        model.addAttribute("cooperative",cooperative);
        model.addAttribute("fishStores",fishStoreService.getFishUploadByCooperative(cooperative.getUsername()));
        model.addAttribute("fishes",fishService.getFishes());
        FishStore fishStore=new FishStore();
        model.addAttribute("fishStore",fishStore);
        return "redirect:/fishing/coops";

    }

}
