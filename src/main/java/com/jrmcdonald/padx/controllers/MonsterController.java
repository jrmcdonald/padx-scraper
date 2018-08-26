package com.jrmcdonald.padx.controllers;

import com.jrmcdonald.padx.exceptions.MonsterNotFoundException;
import com.jrmcdonald.padx.model.Monster;
import com.jrmcdonald.padx.repositories.MonsterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Monster Controller
 * 
 * <p>Exposes REST endpoints for accessing monster data.
 * 
 * @author Jamie McDonald
 * @since 0.2
 */
@RestController
@RequestMapping("/api")
public class MonsterController {

    @Autowired
    private MonsterRepository monsters;

    /**
     * Return a JSON representation of all monsters.
     */
    @RequestMapping("/monsters")
    public Iterable<Monster> getMonsters() {
        return monsters.findAll();
    }

    /**
     * Return a JSON representation of a single monster.
     */
    @RequestMapping("/monsters/{id}")
    public Monster getMonsterById(@PathVariable long id) {
        return monsters.findById(id).orElseThrow(() -> new MonsterNotFoundException());
    }
}