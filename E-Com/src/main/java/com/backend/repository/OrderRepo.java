package com.backend.repository;

import com.backend.model.Order;
import com.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, String> {
    List<Order> findByUser(User user);
}
