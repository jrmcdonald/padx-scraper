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
    private final String REINCARNATED_EVOLVE_HTML = "<tr><td colspan=\"2\" class=\"finalevolve nowrap\">Ultimate Evolution&nbsp;&nbsp;<br><a href=\"monster.asp?n=4459\"><img src=\"img/book/4459.png\" data-original=\"img/book/4459.png\" class=\"onload\" title=\"No.4459 Medium Water Gem\" alt=\"No.4459 Medium Water Gem\" style=\"display: inline;\"></a><a href=\"monster.asp?n=4503\"><img src=\"img/book/4503.png\" data-original=\"img/book/4503.png\" class=\"onload\" title=\"No.4503 Dragon Emperor, Buster Siegfried's Gem\" alt=\"No.4503 Dragon Emperor, Buster Siegfried's Gem\" style=\"display: inline;\"></a><a href=\"monster.asp?n=1326\"><img src=\"img/book/1326.png\" data-original=\"img/book/1326.png\" class=\"onload\" title=\"No.1326 Jewel of Water\" alt=\"No.1326 Jewel of Water\" style=\"display: inline;\"></a><a href=\"monster.asp?n=1326\"><img src=\"img/book/1326.png\" data-original=\"img/book/1326.png\" class=\"onload\" title=\"No.1326 Jewel of Water\" alt=\"No.1326 Jewel of Water\" style=\"display: inline;\"></a><a href=\"monster.asp?n=1326\"><img src=\"img/book/1326.png\" data-original=\"img/book/1326.png\" class=\"onload\" title=\"No.1326 Jewel of Water\" alt=\"No.1326 Jewel of Water\" style=\"display: inline;\"></a></td><td class=\"evolve\"><div class=\"evolveframe\"><a href=\"monster.asp?n=1955\"><img src=\"img/book/1955.png\" data-original=\"img/book/1955.png\" class=\"onload\" title=\"No.1955 Awoken Lakshmi\" alt=\"Awoken Lakshmi\" style=\"display: inline;\"></a><div class=\"eframenum\">1955</div></div></td><td><div class=\"arrowspace2\"><img src=\"img/evolvearrow7.png\" data-original=\"img/evolvearrow7.png\" class=\"onload\" style=\"display: inline;\"><div class=\"arrowspace6\"><img src=\"img/evolvearrow9.png\" data-original=\"img/evolvearrow9.png\" class=\"onload\" style=\"display: inline;\"></div><div class=\"reinlevel\" style=\"position: absolute; top: 57px; left: 16px;\">Lv.99</div></div></td><td colspan=\"2\"><table><tbody><tr><td class=\"awokenevolve\"><div class=\"evolveframe\"><img src=\"img/book/3242.png\" data-original=\"img/book/3242.png\" class=\"onload\" title=\"No.3242 Reincarnated Lakshmi\" alt=\"Reincarnated Lakshmi\" style=\"display: inline;\"><div class=\"eframenum\">3242</div></div></td><td class=\"finalawokenevolve nowrap\">&nbsp;&nbsp;Reincarnation Evolution<br><a href=\"monster.asp?n=162\"><img src=\"img/book/162.png\" data-original=\"img/book/162.png\" class=\"onload\" title=\"No.162 Blue Evolution Mask\" alt=\"No.162 Blue Evolution Mask\" style=\"display: inline;\"></a><a href=\"monster.asp?n=162\"><img src=\"img/book/162.png\" data-original=\"img/book/162.png\" class=\"onload\" title=\"No.162 Blue Evolution Mask\" alt=\"No.162 Blue Evolution Mask\" style=\"display: inline;\"></a><a href=\"monster.asp?n=162\"><img src=\"img/book/162.png\" data-original=\"img/book/162.png\" class=\"onload\" title=\"No.162 Blue Evolution Mask\" alt=\"No.162 Blue Evolution Mask\" style=\"display: inline;\"></a><a href=\"monster.asp?n=162\"><img src=\"img/book/162.png\" data-original=\"img/book/162.png\" class=\"onload\" title=\"No.162 Blue Evolution Mask\" alt=\"No.162 Blue Evolution Mask\" style=\"display: inline;\"></a><a href=\"monster.asp?n=162\"><img src=\"img/book/162.png\" data-original=\"img/book/162.png\" class=\"onload\" title=\"No.162 Blue Evolution Mask\" alt=\"No.162 Blue Evolution Mask\" style=\"display: inline;\"></a></td></tr></tbody></table></td><td></td></tr>";

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
    public void testIsNotUltimateToUltimateEvolve() {
        Element td = new Element("td");
        td.html(REINCARNATED_EVOLVE_HTML);

        boolean result = isUltimateToUltimate().test(td);
        assertThat(result).isFalse();
    }

    @Test
    public void testIsNotReincarnationEvolve() {
        Element td = new Element("td");
        td.html(AWOKEN_EVOLVE_HTML);

        boolean result = isReincarnation().test(td);
        assertThat(result).isFalse();
    }

    @Test
    public void testIsReincarnationEvolve() {
        Element td = new Element("td");
        td.html(REINCARNATED_EVOLVE_HTML);

        boolean result = isReincarnation().test(td);
        assertThat(result).isTrue();
    }
}