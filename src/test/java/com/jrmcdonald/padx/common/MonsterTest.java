package com.jrmcdonald.padx.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrmcdonald.padx.model.Evolution;
import com.jrmcdonald.padx.model.Monster;

/**
 * MonsterTest
 */
public class MonsterTest extends BaseTest {

    public void assertThatMonstersAreEqual(Monster source, Monster comparator) {
        compare(source, comparator);
    }

    public void assertThatMonstersAreEqual(Monster source, String comparator) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            compare(source, mapper.readValue(comparator, Monster.class)); 
        } catch (IOException ex) {
           ex.printStackTrace();
           fail("Exception occurred during comparison");
        }
    }

    private void compare(Monster source, Monster comparator) {
        assertThat(source).isEqualToIgnoringGivenFields(comparator, "evolutions");

        List<Evolution> sourceEvolutions = new ArrayList<Evolution>(source.getEvolutions());
        List<Evolution> comparatorEvolutions = new ArrayList<Evolution>(comparator.getEvolutions());

        assertThat(sourceEvolutions.size()).isEqualTo(comparatorEvolutions.size());

        for (int i = 0; i < sourceEvolutions.size(); i++) {
            Evolution sourceEvo = sourceEvolutions.get(i);
            Evolution comparatorEvo = comparatorEvolutions.get(i);

            assertThat(sourceEvo).isEqualToIgnoringGivenFields(comparatorEvo, "monster");
        }
    }
}