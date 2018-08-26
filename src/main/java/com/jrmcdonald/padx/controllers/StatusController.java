package com.jrmcdonald.padx.controllers;

import java.util.Date;

import com.jrmcdonald.padx.model.Status;
import com.jrmcdonald.padx.model.Status.StatusEnum;
import com.jrmcdonald.padx.repositories.StatusRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Status Controller
 * 
 * <p>Exposes REST endpoints for the API status.
 * 
 * @author Jamie McDonald
 * @since 0.2
 */
@RestController
@RequestMapping("/api")
public class StatusController {

    @Autowired
    private StatusRepository status;

    /**
     * Return a JSON representation of the API status.
     */
    @RequestMapping("/status")
    public Status getLatestStatus() {
        return status.findFirstByOrderByTimestampDesc().orElse(new Status(StatusEnum.ERROR, new Date(), 0L));
    }
}