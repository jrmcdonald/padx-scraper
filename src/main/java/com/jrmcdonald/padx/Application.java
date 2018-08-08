package com.jrmcdonald.padx;

import com.jrmcdonald.padx.service.MonsterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    MonsterService service;

    @Override
    public void run(String... args) throws Exception {
        System.exit(service.execute());
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
