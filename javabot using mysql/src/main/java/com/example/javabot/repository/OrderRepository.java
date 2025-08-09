package com.example.javabot.repository;

import com.example.javabot.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findAllByPhoneNumberOrderByPlaceAtDesc(String phoneNumber);
}
