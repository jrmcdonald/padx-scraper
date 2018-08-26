package com.jrmcdonald.padx.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.jrmcdonald.padx.common.Constants;
import com.jrmcdonald.padx.model.Monster;
import com.jrmcdonald.padx.model.Status;
import com.jrmcdonald.padx.model.Status.StatusEnum;
import com.jrmcdonald.padx.repositories.StatusRepository;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * Monster Data Loader
 * 
 * <p>Instance of {@link CommandLineRunner} that loads monster data into memory
 * 
 * @author Jamie McDonald
 * @since 0.2
 */
@Component
@Profile("!unit-test") // do not run this class during testing scenarios
public class MonsterDataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MonsterDataLoader.class);

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private StatusRepository statusRepository;

    /**
     * Overridden run method
     * 
     * @param args
     */
    @Override
    public void run(String... args) {
        try {
            long startTime = System.nanoTime();
            logger.info("Started execution");

            statusRepository.save(new Status(StatusEnum.UPDATING, new Date(), 0L));

            // fetch monster ids and submit a new MonsterDataTask for each
            ArrayList<Future<Monster>> results = fetchMonsterIds().stream()
                    .map(id -> applicationContext.getBean(MonsterDataTask.class, id))
                    .map(task -> taskExecutor.submit(task))
                    .collect(Collectors.toCollection(ArrayList::new));

            // ensure all have completed
            for (Future<Monster> result : results) {
                try {
                    result.get();
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            }

            taskExecutor.shutdown();

            statusRepository.save(new Status(StatusEnum.READY, new Date(), results.size()));

            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000 / 1000;

            logger.info("Finished execution. Completed in {}s", duration);
        } catch (Exception e) {
            logger.error("An unexpected exception occurred: ", e);
        }
    }
    
    /**
     * Parse the monster book for all the available monster ids
     * 
     * @return a list of monster ids
     */
    private ArrayList<Long> fetchMonsterIds() throws IOException {
        logger.info("Loading monster catalogue HTML");
        Document doc = Jsoup.connect(Constants.PADX_BASE_URL + Constants.PADX_FRAGMENT_MONSTER_BOOK).maxBodySize(0).get();

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