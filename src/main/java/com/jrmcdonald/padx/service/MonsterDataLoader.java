package com.jrmcdonald.padx.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.jrmcdonald.padx.common.Constants;
import com.jrmcdonald.padx.model.Monster;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * MonsterDataLoader
 */
@Component
public class MonsterDataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MonsterDataLoader.class);

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run(String... args) {
        try {
            long startTime = System.nanoTime();
            logger.info("Started execution");

            ArrayList<Future<Monster>> results = fetchMonsterIds().stream()
                    .map(id -> applicationContext.getBean(MonsterDataTask.class, id))
                    .map(task -> taskExecutor.submit(task))
                    .collect(Collectors.toCollection(ArrayList::new));

            for (Future<Monster> result : results) {
                try {
                    result.get();
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            }

            taskExecutor.shutdown();

            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000 / 1000;

            logger.info("Finished execution. Completed in {}s", duration);
        } catch (Exception e) {
            logger.error("An unexpected exception occurred: ", e);
        }
    }
    
    private ArrayList<Long> fetchMonsterIds() throws IOException {
        logger.info("Loading monster catalogue HTML");
        Document doc = Jsoup.connect(Constants.BASE_URL + Constants.FRAGMENT_MONSTER_BOOK).maxBodySize(0).get();

        logger.info("Parsing HTML for monster links");
        Elements monsterLinks = doc.select("div.indexframe > a[href^='monster.asp?n=']");

        logger.info("Found {} monster links, parsing for ids", monsterLinks.size());
        ArrayList<Long> monsterIds = new ArrayList<Long>();
        
        monsterLinks.forEach(link -> {
            if(link.hasAttr("href")) {
                String[] parts = link.attr("href").split("=");
                
                if (parts.length == 2) {
                    try {
                        monsterIds.add(Long.parseLong(parts[1]));
                    } catch (NumberFormatException nfe) {
                        logger.warn("Link with href {} contains an unparseable id, skipping", link.attr("href"));
                    } 
                } else {
                    logger.warn("Link with href {} contains an unparseable id, skipping", link.attr("href"));
                }
            }
        });

        logger.info("Found {} monster ids", monsterIds.size());
        
        return monsterIds;
    }
}