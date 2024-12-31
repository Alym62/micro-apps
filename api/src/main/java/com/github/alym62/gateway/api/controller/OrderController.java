package com.github.alym62.gateway.api.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.github.alym62.gateway.api.entity.Order;
import com.github.alym62.gateway.api.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/order")
@AllArgsConstructor
public class OrderController {
    private OrderService service;

    @GetMapping("/list")
    public ResponseEntity<List<Order>> getList() {
        return ResponseEntity.ok().body(service.getList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(service.getById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Order> create(@RequestBody Order entity) {
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(entity.getId())
                .toUri();
        return ResponseEntity.created(uri).body(service.create(entity));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Order> update(@PathVariable UUID id, @RequestBody Order entity) {
        return ResponseEntity.ok().body(service.update(id, entity));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
