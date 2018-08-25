package com.jrmcdonald.padx.common;

import java.util.Optional;
import java.util.stream.Collectors;

import com.jrmcdonald.padx.exceptions.InvalidMonsterException;
import com.jrmcdonald.padx.model.Evolution;
import com.jrmcdonald.padx.model.Monster;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Evolution Helpers
 * 
 * @author Jamie McDonald
 * @since 0.3
 */
public final class EvolutionHelpers {

    private static Logger logger = LoggerFactory.getLogger(EvolutionHelpers.class);

    protected EvolutionHelpers() {}

    private static Element getEvolutionList(Document doc) throws InvalidMonsterException {
        return doc.getElementsContainingOwnText("進化素材及進化過程").stream()
            .findFirst()
            .map(Element::parent)
            .map(Element::nextElementSibling)
            .map(e -> e.getElementsByTag("ul"))
            .map(Elements::first)
            .orElseThrow(() -> new InvalidMonsterException("INVALID_EVOLUTION_TABLE"));
    }

    private static Element getSourceListItem(Element evoTable, long id) throws InvalidMonsterException {
        return evoTable.getElementsByAttributeValueEnding("href", Long.toString(id)).stream()
                .findFirst()
                .map(Element::parent)
                .orElseThrow(() -> new InvalidMonsterException("MONSTER_NOT_IN_EVO_TABLE"));
    }

    public static void determineEvolutionsForMonster(Monster monster, Document doc) throws InvalidMonsterException {
        long id = monster.getId();

        Element evoTable = getEvolutionList(doc);
        Element sourceLi = getSourceListItem(evoTable, id);

        logger.info(sourceLi.outerHtml());

        Elements evoListItems = sourceLi.getElementsByTag("ul").first().children();

        for (Element evoListItem : evoListItems) {
            Evolution evo = new Evolution();
            
            Elements evoLinks = evoListItem.children().stream()
                    .filter(e -> e.tagName().equals("a"))
                    .collect(Collectors.toCollection(Elements::new));
            
            long evolutionId = evoLinks.stream()
                    .skip(1)
                    .findFirst()
                    .map(EvolutionHelpers::parseIdFromLink)
                    .orElseThrow(() -> new InvalidMonsterException("UNABLE_TO_PARSE_ID_FROM_HREF"));
            
            evo.setEvolution(evolutionId);

            evoLinks.stream()
                    .skip(2)
                    .map(EvolutionHelpers::parseIdFromLink)
                    .forEachOrdered(evo::putOrIncrementMaterial);

            monster.addEvolution(evo);
        }
    }
    
    private static long parseIdFromLink(Element link) {
        return Optional.ofNullable(link)
                .map(a -> a.attr("href"))
                .map(s -> s.split("/"))
                .map(a -> a[1])
                .map(Long::parseLong)
                .orElse(null);
    }
}