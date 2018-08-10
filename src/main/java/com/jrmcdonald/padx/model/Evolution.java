package com.jrmcdonald.padx.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Evolution {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Long evolution;
    private boolean ultimate;
    private boolean reincarnation;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name="material_id")
    @Column(name="material_count")
    @CollectionTable(name="evolution_materials", joinColumns=@JoinColumn(name="evolution_id"))
    private Map<Long, AtomicLong> materials = new HashMap<Long, AtomicLong>();

    @JsonIgnore
    @ManyToOne
    private Monster monster;

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
     * @return the evolution
     */
    public Long getEvolution() {
      return evolution;
    }

    /**
     * @param evolution the evolution to set
     */
    public void setEvolution(Long evolution) {
      this.evolution = evolution;
    }

    /**
     * @return the ultimate
     */
    public boolean isUltimate() {
      return ultimate;
    }

    /**
     * @param ultimate the ultimate to set
     */
    public void setUltimate(boolean ultimate) {
      this.ultimate = ultimate;
    }

    /**
     * @return the reincarnation
     */
    public boolean isReincarnation() {
      return reincarnation;
    }

    /**
     * @param reincarnation the reincarnation to set
     */
    public void setReincarnation(boolean reincarnation) {
      this.reincarnation = reincarnation;
    }

    /**
     * @return the materials
     */
    public Map<Long, AtomicLong> getMaterials() {
      return materials;
    }

    /**
     * @param materials the materials to set
     */
    public void setMaterials(Map<Long, AtomicLong> materials) {
      this.materials = materials;
    }

    /**
     * @param materialId the material to add or increment
     */
    public void putOrIncrementMaterial(long materialId) {
      getMaterials().putIfAbsent(materialId, new AtomicLong(0));
      getMaterials().get(materialId).incrementAndGet();
    }

    /**
     * @return the monster
     */
    public Monster getMonster() {
      return monster;
    }

    /**
     * @param monster the monster to set
     */
    public void setMonster(Monster monster) {
      this.monster = monster;
    }

    @Override
    public String toString() {
        return String.format(
                "Evolution[evolution=%d, ultimate='%s', materials='%s']",
                evolution, ultimate, materials);
    }

}