package com.example.desafiocrosscommerce.database;

import java.util.*;

public class Database {

    private PriorityQueue<Double> numbers;

    public Database() {
        numbers = new PriorityQueue<>();
    }

    public PriorityQueue<Double> getNumbers() {
        return numbers;
    }

    public void addNumbersInBatch(List<Double> batch) {
        try {
            numbers.addAll(batch);
        } catch (Exception e) {
            System.out.println("LogException: Database.addNumbersInBatch");
            System.out.println(e);
        }
    }
}
