package com.example.desafiocrosscommerce.controller;

import com.example.desafiocrosscommerce.service.NumbersListManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.PriorityQueue;

@RestController
@RequestMapping("/api/numbers")
public class Controller {

    @Autowired
    NumbersListManager numbersListManager;

    @GetMapping
    public ResponseEntity<PriorityQueue<Double>> getNumbers() {
        return ResponseEntity.ok(numbersListManager.getNumbers());
    }

}
