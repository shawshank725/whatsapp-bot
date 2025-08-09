package com.example.javabot.service;

import com.example.javabot.entity.Order;

import java.util.List;

public interface OrderService {

    List<Order> findAllOrders();

    Order saveOrder(Order order);

    List<Order> findAllOrdersByPhoneNumber(String phoneNumber);

}
