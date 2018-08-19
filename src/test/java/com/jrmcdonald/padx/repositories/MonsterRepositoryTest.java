package com.jrmcdonald.padx.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import com.jrmcdonald.padx.Application;
import com.jrmcdonald.padx.model.Evolution;
import com.jrmcdonald.padx.model.Monster;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * MonsterRepositoryTest
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = Application.class, 
                      initializers = ConfigFileApplicationContextInitializer.class)
public class MonsterRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MonsterRepository monsterRepository;

    @Test
    public void whenFindById_thenReturnsMonster() {
        Monster monster = new Monster();
        monster.setId(1L);
        monster.setName("Tyra");
        monster.setType("Dragon");

        Evolution evolution = new Evolution();
        evolution.setEvolution(2L);
        evolution.setMonster(monster);
        evolution.putOrIncrementMaterial(1234L);
        evolution.putOrIncrementMaterial(1234L);
        evolution.putOrIncrementMaterial(2345L);

        assertThat(evolution.getMaterials().get(1234L).longValue()).isEqualTo(2L);

        monster.addEvolution(evolution);

        entityManager.persist(monster);
        entityManager.flush();

        Optional<Monster> foundMonster = monsterRepository.findById(1L);
        assertThat(foundMonster).isPresent();
        assertThat(foundMonster.get()).isEqualToComparingFieldByField(monster);
    }
    
}