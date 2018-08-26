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

    /**
     * Should only be called statically
     */
    private MonsterPredicates() {}

    /**
     * Check if the link element contains the "pets/" string.
     * 
     * @return predicate
     */
    public static Predicate<Element> containsMonsterHrefAttr() {
        return e -> e.attr("href").contains(Constants.SKY_FRAGMENT_MONSTER);
    }

    /**
     * Check if the document has an evolution list.
     * 
     * @return predicate
     */
    public static Predicate<Element> containsEvolutionList() {
        return e -> e.getElementsContainingOwnText("此寵物沒有進化").isEmpty();
    }

}