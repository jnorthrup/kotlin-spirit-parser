package com.github.jnorthrup.parser.fsm

import org.junit.Test
import kotlin.test.assertEquals


class NarseseTokenizedLineTest {

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

    var control=listOf(     "a {-] _",
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
        /* to update this test input.map(NarseseTokenizedLine.Companion::narsTokens).map { it.joinToString(" ", "\"", "\"") }.forEach(::println);
         */

        val expected = input.map(NarseseTokenizedLine.Companion::narsTokens).map { it.joinToString(" ") }
        assertEquals(
                control,
                expected
        );




    }}