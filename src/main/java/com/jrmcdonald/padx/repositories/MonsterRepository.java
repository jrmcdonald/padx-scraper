package com.jrmcdonald.padx.repositories;

import com.jrmcdonald.padx.model.Monster;
import org.springframework.data.repository.CrudRepository;

/**
 * Monster Repository
 * 
 * @author Jamie McDonald
 * @since 0.2
 */
public interface MonsterRepository extends CrudRepository<Monster, Long> {
    
}