package com.jrmcdonald.padx.common;

import static com.jrmcdonald.padx.common.MonsterPredicates.isAwokenEvolve;
import static com.jrmcdonald.padx.common.MonsterPredicates.isReincarnationEvolve;
import static com.jrmcdonald.padx.common.MonsterPredicates.isUltimateToUltimateEvolve;
import static org.assertj.core.api.Assertions.assertThat;

import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Monster Predicates Test
 * 
 * @author Jamie McDonald
 * @since 0.2
 */
@RunWith(SpringRunner.class)
public class MonsterPredicatesTest extends BaseTest {

    @Value("classpath:predicates/awoken_evolve.html")
    private Resource AWOKEN_EVOLVE_HTML;

    @Value("classpath:predicates/reincarnated_evolve.html")
    private Resource REINCARNATED_EVOLVE_HTML;

    /**
     * Test the isAwokenEvolve predicate against matching HTML.
     */
    @Test
    public void givenMatchingHTML_isAwokenEvolve_thenReturnTrue() {
        Element td = new Element("td");
        td.html(getResourceAsString(AWOKEN_EVOLVE_HTML));

        boolean result = isAwokenEvolve().test(td);
        assertThat(result).isTrue();
    }

    /**
     * Test the isUltimateToUltimateEvolve predicate against matching HTML.
     */
    @Test
    public void givenMatchingHTML_isUltimateToUltimateEvolve_thenReturnTrue() {
        Element td = new Element("td");
        td.html(getResourceAsString(AWOKEN_EVOLVE_HTML));

        boolean result = isUltimateToUltimateEvolve().test(td);
        assertThat(result).isTrue();
    }
    
    /**
     * Test the isUltimateToUltimateEvolve predicate against non matching HTML.
     */
    @Test
    public void givenMatchingHTML_isUltimateToUltimateEvolve_thenReturnFalse() {
        Element td = new Element("td");
        td.html(getResourceAsString(REINCARNATED_EVOLVE_HTML));

        boolean result = isUltimateToUltimateEvolve().test(td);
        assertThat(result).isFalse();
    }

    /**
     * Test the isReincarnationEvolve predicate against matching HTML.
     */
    @Test
    public void givenMatchingHTML_isReincarnationEvolve_thenReturnTrue() {
        Element td = new Element("td");
        td.html(getResourceAsString(REINCARNATED_EVOLVE_HTML));

        boolean result = isReincarnationEvolve().test(td);
        assertThat(result).isTrue();
    }

    /**
     * Test the isReincarnationEvolve predicate against non matching HTML.
     */
    @Test
    public void givenMatchingHTML_isReincarnationEvolve_thenReturnFalse() {
        Element td = new Element("td");
        td.html(getResourceAsString(AWOKEN_EVOLVE_HTML));

        boolean result = isReincarnationEvolve().test(td);
        assertThat(result).isFalse();
    }
}