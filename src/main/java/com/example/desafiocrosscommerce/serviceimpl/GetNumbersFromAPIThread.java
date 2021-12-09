package com.example.desafiocrosscommerce.serviceimpl;

import com.example.desafiocrosscommerce.database.Database;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.xml.crypto.Data;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class GetNumbersFromAPIThread extends Thread {

    private Database database;
    private int page;
    private Semaphore semaphore;
    private String url = "http://challenge.dienekes.com.br/api/numbers?page={id}";
    private HttpHeaders headers;
    HttpEntity entity;
    private RestTemplate restTemplate;


    public GetNumbersFromAPIThread(RestTemplateBuilder restTemplateBuilder, Integer page, Semaphore semaphore, Database database) {
        this.restTemplate = restTemplateBuilder.build();
        this.page = page;
        this.semaphore = semaphore;
        this.database = database;

        createRequestHeader();
    }

    private void createRequestHeader() {
        headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        entity = new HttpEntity(headers);
    }

    @Override
    public void run() {
        Map response;
        try {
            response = this.restTemplate.exchange(url, HttpMethod.GET, entity, Map.class, page++).getBody();
        } catch (Exception e) {
            System.out.println(e);
            --page;
            return;
        }
        List numbers = (List) response.get("numbers");
        if (!numbers.isEmpty()) {
            try {
                this.semaphore.acquire();
                this.database.addNumbersInBatch(numbers);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.semaphore.release();
            }
            System.out.println("Salvo page: " + this.page);
        }
    }
}
