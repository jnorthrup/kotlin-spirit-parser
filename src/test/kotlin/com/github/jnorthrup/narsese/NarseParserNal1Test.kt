package com.github.jnorthrup.narsese

import com.github.jnorthrup.narsese.NarseParser.tokenize
import com.github.jnorthrup.parser.overloads.div
import com.github.jnorthrup.parser.overloads.plus
import com.github.jnorthrup.parser.primitives.*
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

    @Test
    fun tokenizeNal1() {

        val s = input.first()
        val tokenize = tokenize(s)
        tokenize.forEach {
            println(it)
        }

    }

    /**
     * this appears tp the be the perfect terminated line parse with the right amount of catch/goto
     *
     */
    @Test
    fun parseExperiment() {
        val s = input.first()
        val line = tokenize(s)
        val grammar: op = experiment
        top@
        try {
            for (r: Any? in generateSequence {
                grammar(line)
            }) {
                when (r) {
                    is Iterable<*> -> (r).forEach(::println)
                    is Sequence<*> -> (r).forEach(::println)

                    else -> println(arrayOf(r).contentDeepToString())
                }

            }
        } catch (_: GotoNext) {
            null
        }
    }

    @Test
    fun testOr() {
        val comment = ("//" / "OUT:") + seq (re(".*"))
        val grammar = (comment / task / experiment / sentence)
        top@

        input.map(::tokenize).forEach { line ->
            line % "+"
            try {
                generateSequence { grammar(line) }
                        .forEach {
                            when (it) {
                                is Iterable<*> -> (it).forEach(::println)
                                is Sequence<*> -> (it).forEach(::println)
                                else -> println(arrayOf(it).contentDeepToString())
                            }

                        }

            } catch (e: Throwable) {
            }
        }

    }
}

infix operator fun Line.rem(t: String) = clone().joinToString(t).also(::println)
