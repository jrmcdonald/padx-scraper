package com.jrmcdonald.padx.repositories;

import java.util.Optional;

import com.jrmcdonald.padx.model.Status;

import org.springframework.data.repository.CrudRepository;

/**
 * StatusRepository
 */
public interface StatusRepository extends CrudRepository<Status, Long> {

    Optional<Status> findFirstByOrderByTimestampDesc();
    
}