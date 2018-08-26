package com.jrmcdonald.padx.repositories;

import java.util.Optional;

import com.jrmcdonald.padx.model.Status;

import org.springframework.data.repository.CrudRepository;

/**
 * Status Repository
 * 
 * @author Jamie McDonald
 * @since 0.2
 */
public interface StatusRepository extends CrudRepository<Status, Long> {

    /**
     * Return the latest status entry ordered by timestamp descending
     */
    Optional<Status> findFirstByOrderByTimestampDesc();
    
}