package com.jrmcdonald.padx.service;

import java.io.IOException;
import java.util.concurrent.Callable;

import com.jrmcdonald.padx.common.Constants;
import com.jrmcdonald.padx.common.EvolutionHelpers;
import com.jrmcdonald.padx.common.MonsterHelpers;
import com.jrmcdonald.padx.exceptions.InvalidMonsterException;
import com.jrmcdonald.padx.model.Monster;
import com.jrmcdonald.padx.repositories.MonsterRepository;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
     * Property specified time to wait between retries
     */
    @Value("${connection.waittime}")
    private int waitTime;

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
            logger.debug("Loading data for monster {}.",  id);

            Document pdxHTML = loadDocumentFromPDX(id);
            Document skyHTML = loadDocumentFromSKY(id);

            monster = parseMonsterDetails(id, pdxHTML, skyHTML);

            // store in the database
            monsters.save(monster);
            logger.info("Loaded data for monster {}.", id);
        } catch (InvalidMonsterException | IOException ex) {
            logger.error("Unable to build monster id {}:", id, ex);
        }

        return monster;
    }

    /**
     * Parse monster details from the supplied documents.
     * 
     * @param id the id of the monster
     * @param pdxHTML the PDX html document
     * @param skyHTML the Skyozora html document
     * @return a new monster object
     * @throws InvalidMonsterException
     */
    private Monster parseMonsterDetails(long id, Document pdxHTML, Document skyHTML) throws InvalidMonsterException {
        Monster monster = new Monster();
        monster.setId(id);

        // set basic monster details
        monster.setName(MonsterHelpers.getMonsterNameFromDoc(pdxHTML));
        monster.setType(MonsterHelpers.getMonsterTypeFromDoc(pdxHTML));

        // determine evolutions
        EvolutionHelpers.determineEvolutionsForMonster(monster, skyHTML);

        return monster;
    }

    /**
     * Load PDX data for the supplied monster id.
     * 
     * @param id the monster id
     * @return the loaded document
     * @throws IOException if there was an error fetching the document
     */
    private Document loadDocumentFromPDX(long id) throws IOException {
        return loadDocumentFromURL(Constants.PADX_BASE_URL + Constants.PADX_FRAGMENT_MONSTER + id);
    }

    /**
     * Load Skyozora data for the supplied monster id.
     * 
     * @param id the monster id
     * @return the loaded document
     * @throws IOException if there was an error fetching the document
     */
    private Document loadDocumentFromSKY(long id) throws IOException {
        return loadDocumentFromURL(Constants.SKY_BASE_URL + Constants.SKY_FRAGMENT_MONSTER + id);
    }

    /**
     * Use jsoup to get the document for the supplied URL.
     * 
     * @param url the url to fetch
     * @return the loaded document
     * @throws IOException if there was an error fetching the document
     */
    private Document loadDocumentFromURL(String url) throws IOException {
        Document doc = null;

        for (int i = 0; i < maxRetries; i++) {
            try{
                doc = Jsoup.connect(url).maxBodySize(0).get();
                break; 
            }
            catch (IOException e){
                logger.debug("Connection error whilst fetching {} on attempt {}, waiting {}ms and retrying.", url, i, waitTime);

                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException iex) {
                    logger.error("Thread interrupted during sleep: ", iex);
                    Thread.currentThread().interrupt();
                }
                
                if (i == maxRetries - 1) {
                    throw e;
                }
            }                 
        }

        return doc;
    }
}