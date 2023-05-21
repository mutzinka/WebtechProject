package com.jumpman.fishingconservationsystem.utils;

import com.jumpman.fishingconservationsystem.entity.Cooperative;
import com.jumpman.fishingconservationsystem.entity.Fish;
import com.jumpman.fishingconservationsystem.entity.FishStore;
import com.jumpman.fishingconservationsystem.service.CooperativeService;
import com.jumpman.fishingconservationsystem.service.FishService;
import com.jumpman.fishingconservationsystem.service.FishStoreService;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UploadExcel {

    public List<FishStore> fishStoreList=new ArrayList<>();
    private final CooperativeService cooperativeService;
    private  final FishService fishService;
    public UploadExcel(FishService fishService,CooperativeService cooperativeService){
        this.fishService=fishService;
        this.cooperativeService=cooperativeService;
    }

    public List<FishStore> excel(MultipartFile file,String coopUsername) throws  Exception{
        Path tempDir= Files.createTempDirectory("");
        File tempFile=tempDir.resolve(file.getOriginalFilename()).toFile();
        //transfer multipart file to tempFile
        file.transferTo(tempFile);
        FileInputStream excelFIS = null;
        BufferedInputStream excelBIS = null;
        XSSFWorkbook excelImportToJTable = null;

        if (true) {
            try {
                excelFIS = new FileInputStream(tempFile);
                excelBIS = new BufferedInputStream(excelFIS);
                excelImportToJTable = new XSSFWorkbook(excelBIS);
                XSSFSheet excelSheet = excelImportToJTable.getSheetAt(0);
                for (int row = 1; row < excelSheet.getLastRowNum()+1; row++) {
                    XSSFRow excelRow = excelSheet.getRow(row);
                    //here I will add the rows to be read from the excel file
                    XSSFCell fishName = excelRow.getCell(0);
                    XSSFCell fishQuantity = excelRow.getCell(1);
                    Fish fish=fishService.getFishByName(fishName.getStringCellValue());
                    if(fish==null){
                        fishStoreList.clear();
                        return fishStoreList;
                    }
                    Cooperative cooperative=cooperativeService.findByUsername(coopUsername);
                    FishStore fishStore=FishStore.builder()
                            .fish(fish)
                            .cooperative(cooperative)
                            .dateFishUploaded(LocalDate.now())
                            .fishQuantity(fishQuantity.getNumericCellValue())
                            .remainedFish(fishQuantity.getNumericCellValue())
                            .soldFish(0.0)
                            .build();

                    fishStoreList.add(fishStore);


                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (excelFIS != null) {
                        excelFIS.close();
                    }
                    if (excelBIS != null) {
                        excelBIS.close();
                    }
                    if (excelImportToJTable != null) {
                        excelImportToJTable.close();
                    }
                } catch (IOException iOException) {
                        iOException.getMessage();
                }
            }
        }
        return fishStoreList;
    }


}
