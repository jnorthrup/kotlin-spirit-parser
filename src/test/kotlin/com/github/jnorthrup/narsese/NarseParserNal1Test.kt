package com.github.jnorthrup.narsese

import com.github.jnorthrup.narsese.NarseParser.tokenize
import com.github.jnorthrup.parser.overloads.*
import com.github.jnorthrup.parser.primitives.Line
import com.github.jnorthrup.parser.primitives.re
import org.junit.Before
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

class NarseParserNal1Test {
    lateinit var input: Sequence<String>
    @Before
    fun setup() {

        val resourceAsStream = NarseParserNal1Test::class.java.classLoader.getResourceAsStream("Examples/Example-NAL1-edited.txt")

        val bufferedReader = BufferedReader(InputStreamReader(resourceAsStream))

        input = generateSequence {
            return@generateSequence bufferedReader.readLine()
        }
    }

    @Test //java.lang.Exception: Method parseNal1() should be void
    fun loadNal1() {

        input.forEach {
            print(it)

        }

    }

    @Test //java.lang.Exception: Method parseNal1() should be void
    fun parseNal1() {

        val experiment = (re("\\*+"))[re(".*")..Unit]
        val comment = ("//" / "OUT: ")[re(".*")..Unit]
        val grammar = (experiment / comment / sentence / task)

        input.toList().subList(0, 2).forEach {
            grammar % tokenize(it)
        }

    }

    @Test
    fun testThen() = ("a"() + "b") % Line("a", "b")

    @Test
    fun parseComment() = "//"[re(".*")..Unit] % Line("//", "comment", "here")

    @Test
    fun parseComment1() = ("//" / "OUT: ")[re(".*")..Unit] % Line("//")

}
