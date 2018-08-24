package com.jrmcdonald.padx.common;

import static com.jrmcdonald.padx.common.MonsterPredicates.isAwokenEvolve;
import static com.jrmcdonald.padx.common.MonsterPredicates.isReincarnation;
import static com.jrmcdonald.padx.common.MonsterPredicates.isUltimateToUltimate;
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
    public void testIsAwokenEvolve() {
        Element td = new Element("td");
        td.html(getResourceAsString(AWOKEN_EVOLVE_HTML));

        boolean result = isAwokenEvolve().test(td);
        assertThat(result).isTrue();
    }

    /**
     * Test the isUltimateToUltimate predicate against matching HTML.
     */
    @Test
    public void testIsUltimateToUltimateEvolve() {
        Element td = new Element("td");
        td.html(getResourceAsString(AWOKEN_EVOLVE_HTML));

        boolean result = isUltimateToUltimate().test(td);
        assertThat(result).isTrue();
    }
    
    /**
     * Test the isUltimateToUltimate predicate against non matching HTML.
     */
    @Test
    public void testIsNotUltimateToUltimateEvolve() {
        Element td = new Element("td");
        td.html(getResourceAsString(REINCARNATED_EVOLVE_HTML));

        boolean result = isUltimateToUltimate().test(td);
        assertThat(result).isFalse();
    }

    /**
     * Test the isReincarnation predicate against matching HTML.
     */
    @Test
    public void testIsReincarnationEvolve() {
        Element td = new Element("td");
        td.html(getResourceAsString(REINCARNATED_EVOLVE_HTML));

        boolean result = isReincarnation().test(td);
        assertThat(result).isTrue();
    }

    /**
     * Test the isReincarnation predicate against non matching HTML.
     */
    @Test
    public void testIsNotReincarnationEvolve() {
        Element td = new Element("td");
        td.html(getResourceAsString(AWOKEN_EVOLVE_HTML));

        boolean result = isReincarnation().test(td);
        assertThat(result).isFalse();
    }
}