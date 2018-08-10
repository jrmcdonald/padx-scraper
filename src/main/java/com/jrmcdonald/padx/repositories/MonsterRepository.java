package com.jrmcdonald.padx.repositories;

import com.jrmcdonald.padx.model.Monster;
import org.springframework.data.repository.CrudRepository;

/**
 * MonsterRepository
 */
public interface MonsterRepository extends CrudRepository<Monster, Long> {
    
}