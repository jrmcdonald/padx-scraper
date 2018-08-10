package com.jrmcdonald.padx.model;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Monster {

    @Id
    private Long id;
    private String name;
    private String type;
    
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