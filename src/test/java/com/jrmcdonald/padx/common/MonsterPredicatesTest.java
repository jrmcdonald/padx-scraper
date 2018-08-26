package com.jrmcdonald.padx.common;

import static com.jrmcdonald.padx.common.MonsterPredicates.containsMonsterHrefAttr;
import static org.assertj.core.api.Assertions.assertThat;

import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Monster Predicates Test
 * 
 * @author Jamie McDonald
 * @since 0.2
 */
@RunWith(SpringRunner.class)
public class MonsterPredicatesTest extends BaseTest {

    /**
     * Test the containsMonsterHrefAttr predicate against matching HTML.
     */
    @Test
    public void givenMatchingHTML_containsMonsterHrefAttr_thenReturnsTrue() {
        Element a = new Element("a");
        a.attr("href", "pets/123");

        assertThat(containsMonsterHrefAttr().test(a)).isTrue();
    }

    /**
     * Test the containsMonsterHrefAttr predicate against non matching HTML.
     */
    @Test
    public void givenNonMatchingHTML_containsMonsterHrefAttr_thenReturnsFalse() {
        Element a = new Element("a");
        a.attr("href", "monster/123");

        assertThat(containsMonsterHrefAttr().test(a)).isFalse();
    }
}