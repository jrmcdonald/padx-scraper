package com.jrmcdonald.padx.common;

import java.util.function.Predicate;
import org.jsoup.nodes.Element;

/**
 * Monster Predicates
 * 
 * @author Jamie McDonald
 * @since 0.2
 */
public final class MonsterPredicates {

    private MonsterPredicates() {}

    /**
     * Determine if the <tr> element is one to keep
     */
    public static Predicate<Element> isDesirableEvolutionChartRow() {
        return e -> !("Evolution Chart".equalsIgnoreCase(e.text()))
                && e.getElementsByAttributeValue("src", "img/evolvearrow5.png").isEmpty()
                && e.getElementsByClass("devolution").isEmpty();
    }

    /** 
     * Determine if the <tr> element contains a final evolution
     */
    public static Predicate<Element> isFinalEvolve() {
        return e -> !(e.getElementsByClass("finalevolve").isEmpty());
    }

    /**
     * Determine if the element contains an awoken evolution
     */
    public static Predicate<Element> isAwokenEvolve() {
        return e -> !(e.getElementsByClass("awokenevolve").isEmpty());
    }

    /**
     * Determine if the element contains an ultimate to ultimate evolution
     */
    public static Predicate<Element> isUltimateToUltimate() {
        return e -> e.getElementsByClass("awokenevolve").first().getElementsByAttributeValueContaining("alt", "Reincarnated").isEmpty();
    }

    /** 
     * Determine if the element contains a reincarnation evolution
     */
    public static Predicate<Element> isReincarnation() {
        return e -> !(e.getElementsByClass("awokenevolve").first().getElementsByAttributeValueContaining("alt", "Reincarnated").isEmpty());
    }
    
    /**
     * Determine if the element contains the supplied monster id
     */
    public static Predicate<Element> containsMonsterBookId(long id) {
        return e -> !(e.getElementsByAttributeValue("data-original", "img/book/" + id + ".png").isEmpty());
    }

}