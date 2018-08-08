package com.jrmcdonald.padx.service;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.Callable;

import com.jrmcdonald.padx.common.Constants;
import com.jrmcdonald.padx.common.InvalidMonsterException;
import com.jrmcdonald.padx.common.MonsterHelpers;
import com.jrmcdonald.padx.model.Monster;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * MonsterServiceThread
 */
@Component
@Scope("prototype")
public class MonsterDataTask implements Callable<Monster> {

    private static final Logger logger = LoggerFactory.getLogger(MonsterDataTask.class); 

    @Value("${connection.retries}")
    private int maxRetries;

    private final long id;

    public MonsterDataTask(long id) {
        this.id = id;
    }

    @Override
    public Monster call() {
        Monster monster = null;

        try {
            logger.info("Loading data for monster {}",  id);

            monster = new Monster();
            monster.setId(id);

            Document doc = null;

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

            monster.setName(MonsterHelpers.getMonsterNameFromDoc(doc));
            monster.setType(MonsterHelpers.getMonsterTypeFromDoc(doc));

            Elements filteredRows = MonsterHelpers.filterEvolutionTableRows(doc);
            Element sourceRow = MonsterHelpers.findSourceRow(id, filteredRows);
            int rowIndex = filteredRows.indexOf(sourceRow);

            MonsterHelpers.determineEvolutionsForMonster(id, monster, filteredRows, sourceRow, rowIndex);
        } catch (InvalidMonsterException ex) {
            logger.warn("Unable to process data for monster id {}: {}", id, ex);
        } catch (IOException ex) {
            logger.error("Unable to load data for monster id {}: {}", id, ex);
        }

        return monster;
    }
}