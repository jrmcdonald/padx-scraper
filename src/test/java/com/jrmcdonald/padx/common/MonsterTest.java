package com.jrmcdonald.padx.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrmcdonald.padx.model.Monster;

import org.springframework.test.context.ActiveProfiles;

/**
 * MonsterTest
 */
@ActiveProfiles("unit-test")
public class MonsterTest {

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
        assertThat(source.getEvolutions().containsAll(comparator.getEvolutions()));
    }
}