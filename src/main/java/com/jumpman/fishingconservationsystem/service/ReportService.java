package com.jumpman.fishingconservationsystem.service;

import com.jumpman.fishingconservationsystem.entity.FishStore;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;

@Service
public interface ReportService {
    byte[] exportReport(List<FishStore> list) throws FileNotFoundException, JRException;
}
