package com.jrmcdonald.padx.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Evolution Test
 *
 * @author Jamie McDonald
 * @since 0.3
 */
public class EvolutionTest {

    /**
     * Test that the putOrIncrement method of the Evolution class
     * correctly increments the material count.
     */
    @Test
    public void givenMaterials_whenPutOrIncrement_thenCountIncreases() {
        Evolution evolution = new Evolution();
        evolution.putOrIncrementMaterial(152L);
        evolution.putOrIncrementMaterial(152L);

        assertThat(evolution.getMaterials().get(152L).longValue()).isEqualTo(2L);
    }
}