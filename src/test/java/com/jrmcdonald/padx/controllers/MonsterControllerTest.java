package com.jrmcdonald.padx.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrmcdonald.padx.common.MonsterTest;
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
 * Monster Controller Test
 * 
 * @author Jamie McDonald
 * @since 0.2
 */
@RunWith(SpringRunner.class)
@WebMvcTest(MonsterController.class)
public class MonsterControllerTest extends MonsterTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MonsterRepository monsterRepository;

    /**
     * List of all possible monster objects for these tests.
     */
    private List<Monster> allMonsters = new ArrayList<Monster>();

    /** 
     * Set up some initial test data.
     */
    @Before
    public void init() {
        allMonsters.add(loadMonsterById(238L));
        allMonsters.add(loadMonsterById(1955L));
    }

    /**
     * Test that the /api/monsters endpoint correctly returns all monsters.
     */
    @Test
    public void givenMonsters_whenGetMonsters_thenReturnJsonArray() throws Exception {

        given(monsterRepository.findAll()).willReturn(allMonsters);

        MvcResult result = mvc.perform(get("/api/monsters").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
            
        assertThat(mapper.writeValueAsString(allMonsters)).isEqualTo(result.getResponse().getContentAsString());
    }

    /**
     * Test that the /api/monsters/238 endpoint returns the correct monster.
     */
    @Test
    public void givenMonsters_whenGetMonsterById_thenReturnJson() throws Exception {

        given(monsterRepository.findById(238L)).willReturn(Optional.ofNullable(allMonsters.get(0)));

        MvcResult result = mvc.perform(get("/api/monsters/238").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
            
        assertThat(mapper.writeValueAsString(allMonsters.get(0))).isEqualTo(result.getResponse().getContentAsString());
    }

    /**
     * Test that the /api/monsters/239 endpoint returns a 404 not found.
     */
    @Test
    public void givenMonsters_whenGetInvalidMonsterById_thenReturnError() throws Exception {

        given(monsterRepository.findById(239L)).willReturn(Optional.ofNullable(null));

        mvc.perform(get("/api/monsters/239").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}