package com.jumpman.fishingconservationsystem.service;

import com.jumpman.fishingconservationsystem.entity.Refound;
import org.springframework.stereotype.Service;

@Service
public interface RefoundService {
    Refound makeFound(Integer orderId);
}
