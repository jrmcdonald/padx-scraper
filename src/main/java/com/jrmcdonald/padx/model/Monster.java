package com.jrmcdonald.padx.model;

import java.util.ArrayList;

public class Monster {

    private Long id;
    private String name;
    private String type;
    private ArrayList<Evolution> evolutions;

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
    public ArrayList<Evolution> getEvolutions() {
      if (evolutions == null) {
        evolutions = new ArrayList<Evolution>();
      }
      return evolutions;
    }
    
    /**
     * @param evolutions the evolutions to set
     */
    public void setEvolutions(ArrayList<Evolution> evolutions) {
      this.evolutions = evolutions;
    }

    @Override
    public String toString() {
        return String.format(
                "Monster[id=%d, name='%s', evolutions='%s']",
                id, name, evolutions);
    }

}