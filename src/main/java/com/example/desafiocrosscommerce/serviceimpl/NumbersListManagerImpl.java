package com.example.desafiocrosscommerce.serviceimpl;

import com.example.desafiocrosscommerce.database.Database;
import com.example.desafiocrosscommerce.service.NumbersListManager;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;

import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Service
public class NumbersListManagerImpl implements NumbersListManager {

    private final Database database;
    private final ExecutorService pool;
    private final Semaphore semaphore;
    private final RestTemplateBuilder restTemplate;


    public NumbersListManagerImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder;
        this.database = new Database();
        this.semaphore = new Semaphore(1);

        pool = Executors.newFixedThreadPool(64);

        saveNumbersFromAPI();

    }

    private void saveNumbersFromAPI() {
        System.out.println("************Iniciando a busca dos números************");
        long initial = System.currentTimeMillis();

        executeAllThreads();

        awaitThreads();

        long end = System.currentTimeMillis();
        System.out.println("************Fim da busca dos números************");
        System.out.println("************Time expended in milli seconds:" + (end - initial) + "************");
    }

    private void executeAllThreads() {
        Integer page = 0;
        while (page < 10000) {
            ++page;
            pool.execute(new GetNumbersFromAPIThread(this.restTemplate, page, this.semaphore, this.database));
        }
    }

    private void awaitThreads() {
        try {
            pool.awaitTermination(1, TimeUnit.MICROSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.shutdown();
    }

    @Override
    public PriorityQueue<Double> getNumbers() {
        return database.getNumbers();
    }
}
