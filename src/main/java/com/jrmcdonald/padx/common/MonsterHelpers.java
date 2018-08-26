package com.jrmcdonald.padx.common;

import com.jrmcdonald.padx.exceptions.InvalidMonsterException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Monster Helpers
 * 
 * @author Jamie McDonald
 * @since 0.2
 */
public final class MonsterHelpers {

    /**
     * Should only be used statically
     */
    private MonsterHelpers() {}

    /** 
     * Find the monster name in the supplied HTML document.
     * 
     * @param doc the HTML document
     * @return the monster name
     * @throws InvalidMonsterException if the document does not contain the name
     */
    public static String getMonsterNameFromDoc(Document doc) throws InvalidMonsterException {
        return doc.getElementsByTag("span")
                .stream()
                .filter(e -> "Name".equalsIgnoreCase(e.text()))
                .findFirst()
                .map(Element::parent)
                .map(Element::nextElementSibling)
                .map(Element::text)
                .orElseThrow(() -> new InvalidMonsterException("INVALID_NAME_TEXT"));
    }

    /** 
     * Find the monster type in the supplied HTML document.
     * 
     * @param doc the HTML document
     * @return the monster type
     * @throws InvalidMonsterException if the document does not contain the type
     */
    public static String getMonsterTypeFromDoc(Document doc) throws InvalidMonsterException {
        return doc.getElementsByClass("ptitle")
                .stream()
                .filter(e -> "Type:".equalsIgnoreCase(e.text()))
                .findFirst()
                .map(Element::nextElementSibling)
                .map(Element::text)
                .orElseThrow(() -> new InvalidMonsterException("INVALID_MONSTER_TYPE"));
    }
}