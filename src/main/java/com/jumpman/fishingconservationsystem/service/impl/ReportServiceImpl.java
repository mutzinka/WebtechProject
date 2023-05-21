package com.jumpman.fishingconservationsystem.service.impl;

import com.jumpman.fishingconservationsystem.entity.Fish;
import com.jumpman.fishingconservationsystem.entity.FishStore;
import com.jumpman.fishingconservationsystem.repository.FishStoreRepo;
import com.jumpman.fishingconservationsystem.service.ReportService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@Component
public class ReportServiceImpl implements ReportService {
    private FishStoreRepo fishStoreRepo;
    public ReportServiceImpl(FishStoreRepo fishStoreRepo){
        this.fishStoreRepo=fishStoreRepo;
    }
    @Override
    public byte[] exportReport(List<FishStore> list) throws FileNotFoundException, JRException {


        //load file and compile it
        File file= ResourceUtils.getFile("classpath:fishstore.jrxml");
        JasperReport jasperReport= JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource=new JRBeanCollectionDataSource(list);
        Map<String,Object> parameters=new HashedMap();
        parameters.put("title","Fish Farming");
        parameters.put("createdBy","Fish Farming");
        JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport,parameters,dataSource);
        //exporting
          byte[] data=JasperExportManager.exportReportToPdf(jasperPrint);

        return data;
    }
}
