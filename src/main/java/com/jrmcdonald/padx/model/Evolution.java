package com.jrmcdonald.padx.model;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Evolution {

    private Long evolution;
    private boolean ultimate;
    private boolean reincarnation;
    private HashMap<Long, AtomicLong> materials;

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
    public HashMap<Long, AtomicLong> getMaterials() {
      if (materials == null) {
        materials = new HashMap<Long, AtomicLong>();
      }
      return materials;
    }

    /**
     * @param materials the materials to set
     */
    public void setMaterials(HashMap<Long, AtomicLong> materials) {
      this.materials = materials;
    }

    public void putOrIncrementMaterial(long materialId) {
      getMaterials().putIfAbsent(materialId, new AtomicLong(0));
      getMaterials().get(materialId).incrementAndGet();
    }

    @Override
    public String toString() {
        return String.format(
                "Evolution[evolution=%d, ultimate='%s', materials='%s']",
                evolution, ultimate, materials);
    }

}