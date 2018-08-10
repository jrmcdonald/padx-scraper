package com.jrmcdonald.padx.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.jrmcdonald.padx.common.MonsterTest;
import com.jrmcdonald.padx.model.Evolution;
import com.jrmcdonald.padx.model.Monster;
import com.jrmcdonald.padx.repositories.MonsterRepository;
import com.jrmcdonald.padx.service.MonsterDataTask;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * MonsterDataTaskTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MonsterDataTaskTest extends MonsterTest {

    @Autowired
    private ThreadPoolTaskExecutor testTaskExecutor;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MonsterRepository monsterRepository;

    @Test
    public void testLoadNormalEvolution() {
        long id = 238L;

        Monster monster = new Monster();
        monster.setId(id);
        monster.setType("God");
        monster.setName("Lakshmi");
        
        Evolution evolution = new Evolution();
        evolution.setEvolution(239L);
        evolution.putOrIncrementMaterial(234L);
        evolution.putOrIncrementMaterial(234L);
        evolution.putOrIncrementMaterial(172L);
        evolution.putOrIncrementMaterial(148L);
        evolution.putOrIncrementMaterial(160L);
        monster.addEvolution(evolution);

        genericTest(id, monster);
    }

    @Test
    public void testLoadUltimateEvolution() {
        long id = 239L;

        Monster monster = new Monster();
        monster.setId(id);
        monster.setType("God");
        monster.setName("Fortune Deity Lakshmi");
        
        Evolution evolution = new Evolution();
        evolution.setEvolution(1114L);
        evolution.setUltimate(true);
        evolution.putOrIncrementMaterial(148L);
        evolution.putOrIncrementMaterial(234L);
        evolution.putOrIncrementMaterial(247L);
        evolution.putOrIncrementMaterial(248L);
        evolution.putOrIncrementMaterial(251L);
        monster.addEvolution(evolution);

        Evolution evolution2 = new Evolution();
        evolution2.setEvolution(1115L);
        evolution2.setUltimate(true);
        evolution2.putOrIncrementMaterial(321L);
        evolution2.putOrIncrementMaterial(172L);
        evolution2.putOrIncrementMaterial(251L);
        evolution2.putOrIncrementMaterial(915L);
        evolution2.putOrIncrementMaterial(915L);
        monster.addEvolution(evolution2);

        Evolution evolution3 = new Evolution();
        evolution3.setEvolution(1955L);
        evolution3.setUltimate(true);
        evolution3.putOrIncrementMaterial(4459L);
        evolution3.putOrIncrementMaterial(4503L);
        evolution3.putOrIncrementMaterial(1326L);
        evolution3.putOrIncrementMaterial(1326L);
        evolution3.putOrIncrementMaterial(1326L);
        monster.addEvolution(evolution3);

        genericTest(id, monster);
    }

    @Test
    public void testLoadReincarnatedEvolution() {
        long id = 1955L;

        Monster monster = new Monster();
        monster.setId(id);
        monster.setType("Physical / God");
        monster.setName("Awoken Lakshmi");
        
        Evolution evolution = new Evolution();
        evolution.setEvolution(3242L);
        evolution.setReincarnation(true);
        evolution.putOrIncrementMaterial(162L);
        evolution.putOrIncrementMaterial(162L);
        evolution.putOrIncrementMaterial(162L);
        evolution.putOrIncrementMaterial(162L);
        evolution.putOrIncrementMaterial(162L);
        monster.addEvolution(evolution);

        genericTest(id, monster);
    }

    @Test
    public void testLoadNoEvolution() {
        long id = 1115L;

        Monster monster = new Monster();
        monster.setId(id);
        monster.setType("God / Balanced");
        monster.setName("Purple Lotus Chanter, Lakshmi");

        genericTest(id, monster);
    }

	private void genericTest(long id, Monster monster) {
		Monster fetchedMonster = callMonsterDataTask(id);
        assertThatMonstersAreEqual(monster, fetchedMonster);

        Optional<Monster> foundMonster = monsterRepository.findById(id);
        assertThat(foundMonster).isPresent();
        assertThatMonstersAreEqual(monster, foundMonster.get());
	}

	private Monster callMonsterDataTask(long id) {
		MonsterDataTask task = applicationContext.getBean(MonsterDataTask.class, id);
        Future<Monster> result = testTaskExecutor.submit(task);

        Monster monster = null;

        try {
            monster = result.get();            
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
            fail("Exception occurred during thread processing");
        }

        assertThat(monster).isNotNull();

        return monster;
	}
    
}