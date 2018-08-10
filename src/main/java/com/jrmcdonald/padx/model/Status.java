package com.jrmcdonald.padx.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Status
 */
@Entity
public class Status {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    private StatusEnum status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    private long count;

    public Status() {}

    public Status(StatusEnum status, Date timestamp, long count) {
        this.status = status;
        this.timestamp = timestamp;
        this.count = count;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the status
     */
    public StatusEnum getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the count
     */
    public long getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(long count) {
        this.count = count;
    }

    public enum StatusEnum {
        READY,
        UPDATING,
        ERROR
    }
}