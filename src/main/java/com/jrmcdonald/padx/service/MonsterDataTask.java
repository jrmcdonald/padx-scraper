package com.jrmcdonald.padx.service;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.Callable;

import com.jrmcdonald.padx.common.Constants;
import com.jrmcdonald.padx.common.MonsterHelpers;
import com.jrmcdonald.padx.exceptions.InvalidMonsterException;
import com.jrmcdonald.padx.model.Monster;
import com.jrmcdonald.padx.repositories.MonsterRepository;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Monster Data Task
 * 
 * <p>Callable thread to execute the parsing of monster data
 * 
 * @author Jamie McDonald
 * @since 0.2
 */
@Component
@Scope("prototype")
public class MonsterDataTask implements Callable<Monster> {

    private static final Logger logger = LoggerFactory.getLogger(MonsterDataTask.class); 

    @Autowired
    MonsterRepository monsters;

    /**
     * Property specified number of retries
     */
    @Value("${connection.retries}")
    private int maxRetries;

    /**
     * Monster id to fetch
     */
    private final long id;

    /** 
     * Constructor
     * 
     * @param id the monster id
     */
    public MonsterDataTask(long id) {
        this.id = id;
    }

    /** 
     * Call method to fetch and parse the monster HTML for the specified id
     * 
     * @return the fetched {@link Monster} object
     */
    @Override
    public Monster call() {
        Monster monster = null;

        try {
            logger.info("Loading data for monster {}",  id);

            monster = new Monster();
            monster.setId(id);

            Document doc = null;

            // Attempt to connect and load the HTML into the doc object
            for (int i = 0; i < maxRetries; i++) {
                try{
                    doc = Jsoup.connect(Constants.BASE_URL + Constants.FRAGMENT_MONSTER + id).maxBodySize(0).get();
                    break; 
                }
                catch (SocketTimeoutException e){
                    if (i == maxRetries - 1) {
                        throw e;
                    }
                }                 
            }

            // set basic monster details
            monster.setName(MonsterHelpers.getMonsterNameFromDoc(doc));
            monster.setType(MonsterHelpers.getMonsterTypeFromDoc(doc));

            // filter out undesirable rows
            Elements filteredRows = MonsterHelpers.filterEvolutionTableRows(doc);
            Element sourceRow = MonsterHelpers.findSourceRow(id, filteredRows);
            int rowIndex = filteredRows.indexOf(sourceRow);

            // determine evolutions
            MonsterHelpers.determineEvolutionsForMonster(id, monster, filteredRows, sourceRow, rowIndex);
        } catch (InvalidMonsterException ex) {
            logger.warn("Unable to process data for monster id {}: {}", id, ex);
        } catch (IOException ex) {
            logger.error("Unable to load data for monster id {}: {}", id, ex);
        }

        // store in the database
        monsters.save(monster);

        return monster;
    }
}