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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MonsterHelpers {

    private static final Logger logger = LoggerFactory.getLogger(MonsterHelpers.class);

    private MonsterHelpers() {}

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
            case FINAL:
                // no evolutions
            default:
                // no evolutions
                break;
        }
    }

    private static void determineReincarnatedEvolution(Monster monster, Element sourceRow) throws InvalidMonsterException {
        Evolution evo = new Evolution();
        evo.setReincarnation(true);

        Element evolutionCell = getEvolutionCellByClass(sourceRow, "awokenevolve");
        evo.setEvolution(findAndParseMonsterId(evolutionCell));

        Element materialsCell = evolutionCell.nextElementSibling();
        addMaterialsToEvolution(materialsCell, evo);

        monster.addEvolution(evo);
    }

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

    private static Element getEvolutionCellByClass(Element row, String className) {
        return row.getElementsByClass(className).first();
    }

    private static EvolutionType determineEvolutionType(long id, Elements filteredRows, Element sourceRow,
        int rowIndex) throws InvalidMonsterException {
    EvolutionType evoType = EvolutionType.NORMAL;

        if (rowIndex == 0) {
            Elements evolveCells = sourceRow.getElementsByClass("evolve");

            Element sourceCell = findSourceCell(id, evolveCells);

            int cellIndex = evolveCells.indexOf(sourceCell);

            if (cellIndex == evolveCells.size() - 1) {
                evoType = EvolutionType.ULTIMATE;
            }
        } else {
            if (elementsContainIdAndReincarnation(filteredRows, id)) {
                Elements evolveCells = sourceRow.children();

                if (elementsContainIdAndReincarnation(evolveCells, id)) {
                    evoType = EvolutionType.FINAL;
                } else {
                    evoType = EvolutionType.REINCARNATION;
                }
            } else {
                evoType = EvolutionType.FINAL;
            }
        }

        return evoType;
    }

    private static Element findSourceCell(long id, Elements cells) throws InvalidMonsterException {
        return cells.stream()
                .filter(MonsterPredicates.containsMonsterBookId(id))
                .findFirst()
                .orElseThrow(() -> new InvalidMonsterException("UNABLE_TO_FIND_MONSTER_IN_CELLS"));
    }

    public static Element findSourceRow(long id, Elements filteredRows) throws InvalidMonsterException {
        return filteredRows.stream()
                .filter(MonsterPredicates.containsMonsterBookId(id))
                .findFirst()
                .orElseThrow(() -> new InvalidMonsterException("MONSTER_NOT_IN_EVO_TABLE"));
    }

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

    public static String getMonsterTypeFromDoc(Document doc) throws InvalidMonsterException {
        return doc.getElementsByClass("ptitle")
                .stream()
                .filter(e -> "Type:".equalsIgnoreCase(e.text()))
                .findFirst()
                .map(Element::nextElementSibling)
                .map(Element::text)
                .orElseThrow(() -> new InvalidMonsterException("INVALID_MONSTER_TYPE"));
    }

    private static long findAndParseMonsterId(Element element) throws InvalidMonsterException {
        return element.getElementsByTag("a").stream()
                .findFirst()
                .map(a -> a.attr("href"))
                .map(s -> s.split("="))
                .map(a -> a[1])
                .map(Long::parseLong)
                .orElseThrow(() -> new InvalidMonsterException("UNABLE_TO_PARSE_ID_FROM_HREF"));
    }

    private static void addMaterialsToEvolution(Element element, Evolution evo) {
        for (Element a : element.getElementsByTag("a")) {
            long materialId = Long.parseLong(a.attr("href").split("=")[1]);
            evo.putOrIncrementMaterial(materialId);
        }
    }

    private static boolean elementsContainIdAndReincarnation(Elements elements, long id) {
        return elements.stream()
                .filter(MonsterPredicates.isReincarnation())
                .filter(MonsterPredicates.containsMonsterBookId(id))
                .count() > 0;
    }
}