package com.jrmcdonald.padx.common;

import java.util.Optional;
import java.util.stream.Collectors;

import com.jrmcdonald.padx.common.Constants.EvolutionType;
import com.jrmcdonald.padx.exceptions.InvalidMonsterException;
import com.jrmcdonald.padx.model.Evolution;
import com.jrmcdonald.padx.model.Monster;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
     * Determine evolutions for the supplied monster. Updates the supplied {@link Monster} object.
     * 
     * @param id the monster id
     * @param monster the Monster
     * @param filtereredRows the set of desirable rows
     * @param sourceRow the row containing the base monster
     * @param rowIndex the row index of the sourceRow
     * @throws InvalidMonsterException
     */
    public static void determineEvolutionsForMonster(long id, Monster monster, Elements filteredRows, Element sourceRow,
        int rowIndex) throws InvalidMonsterException {
        EvolutionType evoType = MonsterHelpers.determineEvolutionType(id, filteredRows, sourceRow, rowIndex);

        switch (evoType) {
            case NORMAL:
                determineNormalEvolution(id, monster, filteredRows, sourceRow, rowIndex);
                break;
            case ULTIMATE:
                determineUltimateEvolutions(monster, filteredRows);
                break;
            case REINCARNATION:
                determineReincarnatedEvolution(monster, sourceRow);
                break;
            case ULTIMATE_TO_ULTIMATE:
                determineUltimateToUltimateEvolution(monster, sourceRow);
                break;
            case FINAL:
                // no evolutions
            default:
                // no evolutions
                break;
        }
    }

    /**
     * Generate Ultimate to Ultimate evolution details. Updates the supplied monster.
     * 
     * @param monster the monster
     * @param sourceRow the row containing the base monster 
     * @throws InvalidMonsterException 
     */
    private static void determineUltimateToUltimateEvolution(Monster monster, Element sourceRow) throws InvalidMonsterException {
        Evolution evo = new Evolution();
        evo.setUltimate(true);

        Element evolutionCell = getEvolutionCellByClass(sourceRow, "awokenevolve");
        evo.setEvolution(findAndParseMonsterId(evolutionCell));

        Element materialsCell = evolutionCell.nextElementSibling();
        addMaterialsToEvolution(materialsCell, evo);

        monster.addEvolution(evo);
    }


    /**
     * Generate Reincarnation evolution details. Updates the supplied monster.
     * 
     * @param monster the monster
     * @param sourceRow the row containing the base monster 
     * @throws InvalidMonsterException 
     */
    private static void determineReincarnatedEvolution(Monster monster, Element sourceRow) throws InvalidMonsterException {
        Evolution evo = new Evolution();
        evo.setReincarnation(true);

        Element evolutionCell = getEvolutionCellByClass(sourceRow, "awokenevolve");
        evo.setEvolution(findAndParseMonsterId(evolutionCell));

        Element materialsCell = evolutionCell.nextElementSibling();
        addMaterialsToEvolution(materialsCell, evo);

        monster.addEvolution(evo);
    }

    /**
     * Generate Ultimate evolution details. Updates the supplied monster.
     * 
     * @param monster the monster
     * @param sourceRow the row containing the base monster 
     * @throws InvalidMonsterException 
     */
    private static void determineUltimateEvolutions(Monster monster, Elements filteredRows) throws InvalidMonsterException {
        Elements evoRows = filteredRows.stream()
                .filter(MonsterPredicates.isFinalEvolve())
                .collect(Collectors.toCollection(Elements::new));
        
        for (Element row : evoRows) {
            Evolution evo = new Evolution();
            evo.setUltimate(true);

            Element evolutionCell = getEvolutionCellByClass(row, "evolve");
            evo.setEvolution(findAndParseMonsterId(evolutionCell)); 
            
            Element materialsCell = evolutionCell.previousElementSibling();
            addMaterialsToEvolution(materialsCell, evo);

            monster.addEvolution(evo);
        }
    }

    /**
     * Generate normal evolution details. Updates the supplied monster.
     * 
     * @param monster the monster
     * @param sourceRow the row containing the base monster 
     * @throws InvalidMonsterException 
     */
    private static void determineNormalEvolution(long id, Monster monster, Elements filteredRows, Element sourceRow,
        int rowIndex) throws InvalidMonsterException {
        Evolution evo = new Evolution();
        
        Elements evolveCells = sourceRow.getElementsByClass("evolve");

        Integer sourceCellIndex = evolveCells.stream()
                .filter(MonsterPredicates.containsMonsterBookId(id))
                .findFirst()
                .map(e -> evolveCells.indexOf(e))
                .orElse(null);

        Element evolutionCell = evolveCells.get(sourceCellIndex + 1);

        evo.setEvolution(findAndParseMonsterId(evolutionCell)); 

        Element evoMatsRow = filteredRows.get(rowIndex + 1);

        Element evoMatsCell = evoMatsRow.children().get(sourceCellIndex);

        addMaterialsToEvolution(evoMatsCell, evo);

        monster.addEvolution(evo);
    }

    /**
     * Get the evolution element matching the supplied class from the specified row.
     * 
     * @param row the element to search
     * @param className the class name to look for
     * @return the matching element
     */
    private static Element getEvolutionCellByClass(Element row, String className) {
        return row.getElementsByClass(className).first();
    }

    /**
     * Determine the evolution type.
     * 
     * @param id the id of the base monster
     * @param filteredRows the set of desirable rows
     * @param sourceRow the row containing the base monster
     * @param rowIndex the index of the row containing the base monster
     * @return the evolution type
     * @throws InvalidMonsterException
     */
    private static EvolutionType determineEvolutionType(long id, Elements filteredRows, Element sourceRow,
        int rowIndex) throws InvalidMonsterException {
    EvolutionType evoType = EvolutionType.NORMAL;

        // if it is row index 0, then it is either a normal, or ultimate evolution
        if (rowIndex == 0) {
            Elements evolveCells = sourceRow.getElementsByClass("evolve");

            Element sourceCell = findSourceCell(id, evolveCells);

            int cellIndex = evolveCells.indexOf(sourceCell);

            // if it is the last cell in the row, then it is an ultimate evolution
            if (cellIndex == evolveCells.size() - 1) {
                evoType = EvolutionType.ULTIMATE;
            }
        } else {
            Elements evolveCells = sourceRow.children();

            if (elementsContainReincarnation(evolveCells)) {
                evoType = EvolutionType.REINCARNATION;
            } else if (elementsContainUltimateToUltimate(evolveCells)) {
                evoType = EvolutionType.ULTIMATE_TO_ULTIMATE;
            } else {
                evoType = EvolutionType.FINAL;
            }
        }

        return evoType;
    }

    /**
     * Finds the base monster in the supplied cells.
     * 
     * @param id the base monster id
     * @param cells the cells to search
     * @return the cell containing the base monster
     * @throws InvalidMonsterException
     */
    private static Element findSourceCell(long id, Elements cells) throws InvalidMonsterException {
        return cells.stream()
                .filter(MonsterPredicates.containsMonsterBookId(id))
                .findFirst()
                .orElseThrow(() -> new InvalidMonsterException("UNABLE_TO_FIND_MONSTER_IN_CELLS"));
    }

    /**
     * Find the row containing the base monster in the set of filtered rows.
     * 
     * @param id the base monster id
     * @param filteredRows the set of desirable rows
     * @return the row containing the base monster
     * @throws InvalidMonsterException
     */
    public static Element findSourceRow(long id, Elements filteredRows) throws InvalidMonsterException {
        return filteredRows.stream()
                .filter(MonsterPredicates.containsMonsterBookId(id))
                .findFirst()
                .orElseThrow(() -> new InvalidMonsterException("MONSTER_NOT_IN_EVO_TABLE"));
    }

    /**
     * Filter the supplied HTML document to find the desirable rows in the evolution table.
     * 
     * @param doc the HTML document
     * @return a set of filtered rows
     * @throws InvalidMonsterException if the evolution table is invalid
     */
    public static Elements filterEvolutionTableRows(Document doc) throws InvalidMonsterException {
        return Optional.ofNullable(doc.getElementById("evolve"))
                .map(Element::nextElementSibling)
                .map(e -> e.getElementById("tablestat"))
                .map(e -> e.child(0))
                .map(Element::children)
                .orElseThrow(() -> new InvalidMonsterException("INVALID_EVOLUTION_TABLE"))
                .stream()
                .filter(MonsterPredicates.isDesirableEvolutionChartRow())
                .collect(Collectors.toCollection(Elements::new));
    }

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

    /** 
     * Find the monster id in the supplied HTML document.
     * 
     * @param doc the HTML document
     * @return the monster id 
     * @throws InvalidMonsterException if the document does not contain the id
     */
    private static long findAndParseMonsterId(Element element) throws InvalidMonsterException {
        return element.getElementsByTag("a").stream()
                .findFirst()
                .map(a -> a.attr("href"))
                .map(s -> s.split("="))
                .map(a -> a[1])
                .map(Long::parseLong)
                .orElseThrow(() -> new InvalidMonsterException("UNABLE_TO_PARSE_ID_FROM_HREF"));
    }

    /**
     * Find evolution materials in the supplied element and add them to the evolution.
     * 
     * @param element the element to search
     * @param evo the evolution to add materials to
     */
    private static void addMaterialsToEvolution(Element element, Evolution evo) {
        for (Element a : element.getElementsByTag("a")) {
            long materialId = Long.parseLong(a.attr("href").split("=")[1]);
            evo.putOrIncrementMaterial(materialId);
        }
    }

    /**
     * Check if the elements contain an ultimate to ultimate evolution.
     * 
     * @param elements the elements to search
     * @return true or false
     */
    private static boolean elementsContainUltimateToUltimate(Elements elements) {
        return elements.stream()
                .filter(MonsterPredicates.isAwokenEvolve())
                .filter(MonsterPredicates.isUltimateToUltimate())
                .count() > 0;
    }

    /**
     * Check if the elements contain a reincarnation evolution.
     * 
     * @param elements the elements to search
     * @return true or false
     */
    private static boolean elementsContainReincarnation(Elements elements) {
        return elements.stream()
                .filter(MonsterPredicates.isAwokenEvolve())
                .filter(MonsterPredicates.isReincarnation())
                .count() > 0;
    }
}