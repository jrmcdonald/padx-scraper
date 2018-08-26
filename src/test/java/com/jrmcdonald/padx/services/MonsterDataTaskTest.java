package com.jrmcdonald.padx.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.jrmcdonald.padx.common.MonsterTest;
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
 * Monster Data Task Test
 * 
 * @author Jamie McDonald
 * @since 0.2
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

    /**
     * Test that normal evolutions are loaded properly.
     */
    @Test
    public void givenMonsterId238_whenSubmitTask_returnsMonsterWithNormalEvolutions() {
        genericTest(238L);
    }

    /**
     * Test that ultimate evolutions are loaded properly.
     */
    @Test
    public void givenMonsterId239_whenSubmitTask_returnsMonsterWithUltimateEvolutions() {
        genericTest(239L);
    }

    /**
     * Test that reincarnation evolutions are loaded properly.
     */
    @Test
    public void givenMonsterId1955_whenSubmitTask_returnsMonsterWithReincarnationEvolutions() {
        genericTest(1955L);
    }

    /**
     * Test that no evolutions are loaded properly.
     */
    @Test
    public void givenMonsterId1115_whenSubmitTask_returnsMonsterWithNoEvolutions() {
        genericTest(1115L);
    }

    /**
     * Test that sideways ultimate evolutions are loaded properly. Fixes issue #4.
     */
    @Test
    public void givenMonsterId995_whenSubmitTask_returnsMonsterWithUltimateEvolutions() {
        genericTest(995L);
    }

    /**
     * Test that downwards ultimate evolutions are loaded properly. Fixes issue #15.
     */
    @Test
    public void givenMonsterId2969_whenSubmitTask_returnsMonsterWithUltimateEvolutions() {
        genericTest(2969L);
    }

    /**
     * Test that a monster with no evolution table loads properly.
     */
    @Test
    public void givenMonsterId150_whenSubmitTask_returnsMonsterWithNoEvolutions() {
        genericTest(150L);
    }

    /**
     * Generic test method to fetch the monster id and compare the result.
     * 
     * @param id the id to fetch
     */
	private void genericTest(long id) {
        Monster fetchedMonster = callMonsterDataTask(id);
        Monster sourceMonster = loadMonsterById(id);

        assertThatMonstersAreEqual(sourceMonster, fetchedMonster);

        Optional<Monster> foundMonster = monsterRepository.findById(id);
        assertThat(foundMonster).isPresent();
        assertThatMonstersAreEqual(sourceMonster, foundMonster.get());
	}

    /**
     * Submit the threaded MonsterDataTask.
     * 
     * @param id the id to submit
     */
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