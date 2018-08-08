package com.jrmcdonald.padx.common;

import java.util.function.Predicate;
import org.jsoup.nodes.Element;

public final class MonsterPredicates {

    private MonsterPredicates() {}

    public static Predicate<Element> isDesirableEvolutionChartRow() {
        return e -> !("Evolution Chart".equalsIgnoreCase(e.text()))
                && e.getElementsByAttributeValue("src", "img/evolvearrow5.png").isEmpty()
                && e.getElementsByClass("devolution").isEmpty();
    }

    public static Predicate<Element> isFinalEvolve() {
        return e -> !(e.getElementsByClass("finalevolve").isEmpty());
    }

    public static Predicate<Element> isReincarnation() {
        return e -> !(e.getElementsByClass("awokenevolve").isEmpty());
    }
    
    public static Predicate<Element> containsMonsterBookId(long id) {
        return e -> !(e.getElementsByAttributeValue("data-original", "img/book/" + id + ".png").isEmpty());
    }

}