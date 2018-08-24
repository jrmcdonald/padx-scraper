package com.jrmcdonald.padx.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrmcdonald.padx.model.Evolution;
import com.jrmcdonald.padx.model.Monster;

/**
 * Monster Test
 * 
 * <p>Defines helper methods for testing {@link Monster} objects.
 * 
 * @author Jamie McDonald
 * @since 0.2
 */
public class MonsterTest extends BaseTest {

    /**
     * Assert that two monster objects are equal.
     * 
     * @param source monster object
     * @param comparator monster object
     */
    public void assertThatMonstersAreEqual(Monster source, Monster comparator) {
        compare(source, comparator);
    }

    /**
     * Assert that a provided monster object compares to a json string representation.
     * 
     * @param source monster object
     * @param comparator string
     */
    public void assertThatMonstersAreEqual(Monster source, String comparator) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            compare(source, mapper.readValue(comparator, Monster.class)); 
        } catch (IOException ex) {
           ex.printStackTrace();
           fail("Exception occurred during comparison");
        }
    }

    /**
     * Perform a deep comparison of two monster objects.
     * 
     * @param source monster object
     * @param comparator monster object
     */
    private void compare(Monster source, Monster comparator) {
        assertThat(source).isEqualToIgnoringGivenFields(comparator, "evolutions");

        List<Evolution> sourceEvolutions = new ArrayList<Evolution>(source.getEvolutions());
        Map<Long, Evolution> comparatorEvolutions = comparator.getEvolutions().stream()
                .collect(Collectors.toMap(Evolution::getEvolution, e -> e));

        assertThat(sourceEvolutions.size()).isEqualTo(comparatorEvolutions.size());

        for (Evolution sourceEvo : sourceEvolutions) {
            Evolution comparatorEvo = comparatorEvolutions.get(sourceEvo.getEvolution());

            assertThat(comparatorEvo).isNotNull();
            assertThat(sourceEvo).isEqualToIgnoringGivenFields(comparatorEvo, "id", "monster", "materials");

            Map<Long, AtomicLong> sourceMaterials = sourceEvo.getMaterials();
            Map<Long, AtomicLong> comparatorMaterials = comparatorEvo.getMaterials();

            assertThat(sourceMaterials.size()).isEqualTo(comparatorMaterials.size());

            for (Long id : sourceMaterials.keySet()) {
                assertThat(comparatorMaterials.containsKey(id)).isTrue();
                assertThat(comparatorMaterials.get(id).get()).isEqualTo(sourceMaterials.get(id).get());
            }
        };
    }
}