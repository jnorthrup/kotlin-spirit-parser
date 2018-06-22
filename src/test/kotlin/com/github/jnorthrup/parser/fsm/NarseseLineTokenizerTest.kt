package com.github.jnorthrup.parser.fsm

import com.github.jnorthrup.narsese.NarseParser.tokenize
import org.junit.Test
import kotlin.test.assertEquals


class NarseseLineTokenizerTest {

    @Test
    fun testNarsTokenSplitter() {
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
                "a{-}d")

        val control = listOf("a {-] _",
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

        fun spacify(x:Sequence<String>)= x.joinToString { " " }
        val expected = input.map(::tokenize).map(::spacify)

        assertEquals(
                control,
                expected
        )
    }
}