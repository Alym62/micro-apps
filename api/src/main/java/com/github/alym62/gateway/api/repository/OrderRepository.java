package com.github.alym62.gateway.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.alym62.gateway.api.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

}
