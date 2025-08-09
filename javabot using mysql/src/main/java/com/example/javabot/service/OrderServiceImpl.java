package com.example.javabot.service;

import com.example.javabot.entity.Order;
import com.example.javabot.repository.AppointmentRepository;
import com.example.javabot.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> findAllOrders() {
        return List.of();
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findAllOrdersByPhoneNumber(String phoneNumber) {
        return orderRepository.findAllByPhoneNumberOrderByPlaceAtDesc(phoneNumber);
    }


}
