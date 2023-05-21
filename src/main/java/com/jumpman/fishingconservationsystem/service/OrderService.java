package com.jumpman.fishingconservationsystem.service;

import com.jumpman.fishingconservationsystem.entity.Cooperative;
import com.jumpman.fishingconservationsystem.entity.FishStore;
import com.jumpman.fishingconservationsystem.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    Order makeOrder(Order order);
    List<Order> viewOrder();
    void cancelOrder();
    void saveOrders(List<Order> orders);
    List<Order> getOrdersByCooperative(Cooperative cooperative);

}
