package com.jumpman.fishingconservationsystem.service.impl;

import com.jumpman.fishingconservationsystem.entity.Cooperative;
import com.jumpman.fishingconservationsystem.entity.FishStore;
import com.jumpman.fishingconservationsystem.entity.Order;
import com.jumpman.fishingconservationsystem.repository.OrderRepo;
import com.jumpman.fishingconservationsystem.service.OrderService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {
    private final OrderRepo orderRepo;
    public OrderServiceImpl(OrderRepo orderRepo){
        this.orderRepo=orderRepo;
    }
    @Override
    public Order makeOrder(Order order) {
        return null;
    }

    @Override
    public List<Order> viewOrder() {
        return orderRepo.findAll();
    }

    @Override
    public void cancelOrder() {

    }

    @Override
    public void saveOrders(List<Order> orders) {
        orderRepo.saveAll(orders);
    }

    @Override
    public List<Order> getOrdersByCooperative(Cooperative cooperative) {
        return orderRepo.findByCooperative(cooperative);
    }


}
