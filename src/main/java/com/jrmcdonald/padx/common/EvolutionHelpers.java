package com.jrmcdonald.padx.common;

import java.util.Optional;
import java.util.stream.Collectors;

import com.jrmcdonald.padx.exceptions.InvalidMonsterException;
import com.jrmcdonald.padx.model.Evolution;
import com.jrmcdonald.padx.model.Monster;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Evolution Helpers
 * 
 * @author Jamie McDonald
 * @since 0.3
 */
public final class EvolutionHelpers {

    /**
     * Should only be called statically.
     */
    protected EvolutionHelpers() {}

    /**
     * Determine evolutions for the supplied monster.
     * 
     * @param monster the monster to update
     * @param doc the document to find evolutions in
     * @throws InvalidMonsterException
     */
    public static void determineEvolutionsForMonster(Monster monster, Document doc) throws InvalidMonsterException {
        long id = monster.getId();
        
        if (MonsterPredicates.containsEvolutionList().test(doc)) {
            Element evoTable = getEvolutionList(doc);
            Element sourceLi = getSourceListItem(evoTable, id);

            Element evoList = sourceLi.getElementsByTag("ul").first();

            if (evoList != null) {
                for (Element evoListItem : evoList.children()) {
                    Evolution evo = new Evolution();

                    Elements evoLinks = getEvolutionLinksFromListItem(evoListItem);
                    
                    addEvolutionIdToEvolution(evo, evoLinks);
                    addMaterialsToEvolution(evo, evoLinks);

                    monster.addEvolution(evo);
                }
            }
        }
    }

    /**
     * Get the &lt;ul&gt; element containing evolution data.
     * 
     * @param doc the document to search
     * @return the element
     * @throws InvalidMonsterException
     */
    private static Element getEvolutionList(Document doc) throws InvalidMonsterException {
        return doc.getElementsContainingOwnText("進化素材及進化過程").stream()
            .findFirst()
            .map(Element::parent)
            .map(Element::nextElementSibling)
            .map(e -> e.getElementsByTag("ul"))
            .map(Elements::first)
            .orElseThrow(() -> new InvalidMonsterException("INVALID_EVOLUTION_TABLE"));
    }

    /**
     * Find the &lt;li&gt; element that contains the source monster.
     * 
     * @param evoList the &lt;ul&gt; to search
     * @param id the monster id to find
     * @return the element
     * @throws InvalidMonsterException
     */
    private static Element getSourceListItem(Element evoTable, long id) throws InvalidMonsterException {
        return evoTable.getElementsByAttributeValueEnding("href", Long.toString(id)).stream()
                .findFirst()
                .map(Element::parent)
                .orElseThrow(() -> new InvalidMonsterException("MONSTER_NOT_IN_EVO_TABLE"));
    }

    /**
     * Build the list of evolution link elements
     * 
     * @param evoListItem the &lt;li&lt; to search
     */
    private static Elements getEvolutionLinksFromListItem(Element evoListItem) {
        return evoListItem.children().stream()
                .filter(e -> e.tagName().equals("a"))
                .collect(Collectors.toCollection(Elements::new));
    }
    
    /**
     * Find and add the evolution id to the supplied evolution object.
     * 
     * @param evo the evolution object to update
     * @param evoLinks the list of links to parse
     */
    private static void addEvolutionIdToEvolution(Evolution evo, Elements evoLinks) {
        evoLinks.stream()
                .filter(MonsterPredicates.containsMonsterHrefAttr())
                .limit(1)
                .map(EvolutionHelpers::parseStringIdFromLink)
                .mapToLong(Long::parseLong)
                .forEachOrdered(evo::setEvolution);
    }

    /**
     * Find and add materials to the supplied evolution object.
     * 
     * @param evo the evolution object to update
     * @param evoLinks the list of links to parse
     */
    private static void addMaterialsToEvolution(Evolution evo, Elements evoLinks) {
        evoLinks.stream()
                .filter(MonsterPredicates.containsMonsterHrefAttr())
                .skip(1)
                .map(EvolutionHelpers::parseStringIdFromLink)
                .mapToLong(Long::parseLong)
                .forEachOrdered(evo::putOrIncrementMaterial);
    }

    /**
     * Parse the monster id from a link element.
     * 
     * @param link the &lt;a href...&gt; element to parse
     * @return string id or null if not able to parse a valid
     */
    private static String parseStringIdFromLink(Element link) {
        return Optional.ofNullable(link)
                .map(a -> a.attr("href"))
                .map(s -> s.split("/"))
                .map(a -> a[1])
                .orElse(null);
    }
}