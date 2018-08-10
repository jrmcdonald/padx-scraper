package com.jrmcdonald.padx.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import com.jrmcdonald.padx.common.MonsterTest;
import com.jrmcdonald.padx.model.Evolution;
import com.jrmcdonald.padx.model.Monster;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * MonsterDataRepiositoryTest
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class MonsterDataRepositoryTest extends MonsterTest {

    @Autowired
    private MonsterRepository monsterRepository;

    @Test
    public void testPersistAndFindMonsterById() {
        Monster monster = new Monster();
        monster.setId(1L);
        monster.setName("Tyra");
        monster.setType("Dragon");

        Evolution evolution = new Evolution();
        evolution.setEvolution(2L);
        evolution.putOrIncrementMaterial(152L);
        evolution.putOrIncrementMaterial(152L);

        // check that putOrIncrementMaterial increments correctly
        assertThat(evolution.getMaterials().get(152L).longValue()).isEqualTo(2L);

        monster.addEvolution(evolution);

        // persist the monster
        monsterRepository.save(monster);

        // check that it persists correctly
        Optional<Monster> foundMonster = monsterRepository.findById(1L);
        assertThat(foundMonster).isPresent();

        assertThatMonstersAreEqual(monster, foundMonster.get());
    }
    
}