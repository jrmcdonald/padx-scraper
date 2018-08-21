package com.jrmcdonald.padx.model;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Monster Model
 * 
 * @author Jamie McDonald
 * @since 0.2
 */
@Entity
public class Monster {

    /**
     * Non auto-generated id field.
     */
    @Id
    private Long id;

    /**
     * Monster name
     */
    private String name;

    /**
     * Monster type
     */
    private String type;
    
    /** 
     * Set of {@link Evolution} objects
     */
    @OneToMany(mappedBy = "monster", fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, orphanRemoval = true)
    private Set<Evolution> evolutions = new LinkedHashSet<Evolution>();

    /**
     * @return the id
     */
    public Long getId() {
      return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
      this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
      return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
      this.name = name;
    }

    /**
     * @return the type
     */
    public String getType() {
      return type;
    }
    
    /**
     * @param type the type to set
     */
    public void setType(String type) {
      this.type = type;
    }

    /**
     * @return the evolutions
     */
    public Set<Evolution> getEvolutions() {
      return evolutions;
    }
    
    /**
     * @param evolutions the evolutions to set
     */
    public void setEvolutions(Set<Evolution> evolutions) {
      this.evolutions = evolutions;
      evolutions.forEach(evo -> evo.setMonster(this));
    }

    /**
     * Add an {@link Evolution} to the {@link #evolutions} set and associate it with this object
     */
    public void addEvolution(Evolution evolution) {
      evolutions.add(evolution);
      evolution.setMonster(this);
    }

    @Override
    public String toString() {
        return String.format(
                "Monster[id=%d, name='%s', evolutions='%s']",
                id, name, evolutions);
    }

}