package com.jrmcdonald.padx.common;

import static com.jrmcdonald.padx.common.MonsterPredicates.isAwokenEvolve;
import static com.jrmcdonald.padx.common.MonsterPredicates.isReincarnation;
import static com.jrmcdonald.padx.common.MonsterPredicates.isUltimateToUltimate;
import static org.assertj.core.api.Assertions.assertThat;

import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 * MonsterPredicatesTest
 */
public class MonsterPredicatesTest {

    private final String AWOKEN_EVOLVE_HTML = "<td colspan=\"2\"> <table> <tbody> <tr> <td class=\"awokenevolve\"> <div class=\"evolveframe\"> <a href=\"monster.asp?n=2927\"> <img src=\"img/spacer.gif\" data-original=\"img/book/2927.png\" class=\"onload\" title=\"No.2927 Horus Armor X Dragon Caller, Ace\" alt=\"Horus Armor X Dragon Caller, Ace\"> </a> <div class=\"eframenum\"> 2927 </div> </div> </td> <td class=\"finalawokenevolve nowrap\">&nbsp;&nbsp;Ultimate Evolution <br> <a href=\"monster.asp?n=4476\"> <img src=\"img/spacer.gif\" data-original=\"img/book/4476.png\" class=\"onload\" title=\"No.4476 Passionate Dragon Caller, Ace's Gem\" alt=\"No.4476 Passionate Dragon Caller, Ace's Gem\"> </a> <a href=\"monster.asp?n=1176\"> <img src=\"img/spacer.gif\" data-original=\"img/book/1176.png\" class=\"onload\" title=\"No.1176 Keeper of Gold\" alt=\"No.1176 Keeper of Gold\"> </a> <a href=\"monster.asp?n=234\"> <img src=\"img/spacer.gif\" data-original=\"img/book/234.png\" class=\"onload\" title=\"No.234 Mystic Mask\" alt=\"No.234 Mystic Mask\"> </a> <a href=\"monster.asp?n=246\"> <img src=\"img/spacer.gif\" data-original=\"img/book/246.png\" class=\"onload\" title=\"No.246 Dub-rubylit\" alt=\"No.246 Dub-rubylit\"> </a> <a href=\"monster.asp?n=249\"> <img src=\"img/spacer.gif\" data-original=\"img/book/249.png\" class=\"onload\" title=\"No.249 Dub-topalit\" alt=\"No.249 Dub-topalit\"> </a> </td> </tr> </tbody> </table> </td>";

    @Test
    public void testIsAwokenEvolve() {

        Element td = new Element("td");
        td.html(AWOKEN_EVOLVE_HTML);

        boolean result = isAwokenEvolve().test(td);

        assertThat(result).isTrue();
    }

    @Test
    public void testIsUltimateToUltimateEvolve() {

        Element td = new Element("td");
        td.html(AWOKEN_EVOLVE_HTML);

        boolean result = isUltimateToUltimate().test(td);

        assertThat(result).isTrue();
    }

    @Test
    public void testIsNotReincarnationEvolve() {

        Element td = new Element("td");
        td.html(AWOKEN_EVOLVE_HTML);

        boolean result = isReincarnation().test(td);

        assertThat(result).isFalse();
    }
}