package com.jrmcdonald.padx.model;

public class Material {

    private Long id;

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

    @Override
    public String toString() {
        return String.format(
                "Material[id=%d']",
                id);
    }

}