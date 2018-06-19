package com.github.jnorthrup.parser.fsm

import com.github.jnorthrup.narsese.NarseParser
import org.junit.Test
import kotlin.test.assertEquals


class NarseseLineTokenizerTest {

    @Test
    fun testNarsTokens() {
        val input = listOf("a{-]_",
                "a{-](silly)",
                "#Nancy",
                "a{-]",
                " (-]",
                " (--]",
                "a{-)",
                "a[-]",
                "a{]",
                "a-dh-d",
                "a<h>d",
                "a{-}d");

        var control = listOf("a {-] _",
                "a {-] ( silly )",
                "# Nancy",
                "a {-]",
                "( -]",
                "( --]",
                "a {- )",
                "a [ -]",
                "a {]",//TODO: retire this aberation of a grammar rather than tweek on this particular token cornercase.
                "a - dh - d",
                "a < h > d",
                "a {- } d")
        /* to update this test input.map(NarseseTokenizedLine.Companion::tokenize).map { it.joinToString(" ", "\"", "\"") }.forEach(::println);
         */

        val expected = input.map(NarseParser.Companion::tokenize).map { it.joinToString(" ") }
        assertEquals(
                control,
                expected
        )
    }
}