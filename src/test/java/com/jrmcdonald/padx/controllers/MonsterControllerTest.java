package com.jrmcdonald.padx.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrmcdonald.padx.common.MonsterTest;
import com.jrmcdonald.padx.model.Evolution;
import com.jrmcdonald.padx.model.Monster;
import com.jrmcdonald.padx.repositories.MonsterRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * MonsterControllerTest
 */
@RunWith(SpringRunner.class)
@WebMvcTest(MonsterController.class)
public class MonsterControllerTest extends MonsterTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MonsterRepository monsterRepository;

    private List<Monster> allMonsters = new ArrayList<Monster>();

    @Before
    public void init() {
        Monster monster = new Monster();
        monster.setId(238L);
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

        Monster monster2 = new Monster();
        monster2.setId(1955L);
        monster2.setType("Physical / God");
        monster2.setName("Awoken Lakshmi");
        
        Evolution evolution2 = new Evolution();
        evolution2.setEvolution(3242L);
        evolution2.setReincarnation(true);
        evolution2.putOrIncrementMaterial(162L);
        evolution2.putOrIncrementMaterial(162L);
        evolution2.putOrIncrementMaterial(162L);
        evolution2.putOrIncrementMaterial(162L);
        evolution2.putOrIncrementMaterial(162L);
        monster2.addEvolution(evolution);

        allMonsters.add(monster);
        allMonsters.add(monster2);
    }

    @Test
    public void givenMonsters_whenGetMonsters_thenReturnJsonArray() throws Exception {

        given(monsterRepository.findAll()).willReturn(allMonsters);

        MvcResult result = mvc.perform(get("/api/monsters").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
            
        assertThat(mapper.writeValueAsString(allMonsters)).isEqualTo(result.getResponse().getContentAsString());
    }

    @Test
    public void givenMonsters_whenGetMonsterById_thenReturnJson() throws Exception {

        given(monsterRepository.findById(238L)).willReturn(Optional.ofNullable(allMonsters.get(0)));

        MvcResult result = mvc.perform(get("/api/monsters/238").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
            
        assertThat(mapper.writeValueAsString(allMonsters.get(0))).isEqualTo(result.getResponse().getContentAsString());
    }

    @Test
    public void givenMonsters_whenGetInvalidMonsterById_thenReturnError() throws Exception {

        given(monsterRepository.findById(239L)).willReturn(Optional.ofNullable(null));

        mvc.perform(get("/api/monsters/239").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}