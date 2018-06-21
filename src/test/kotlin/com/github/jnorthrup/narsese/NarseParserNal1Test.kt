package com.github.jnorthrup.narsese

import com.github.jnorthrup.narsese.Companion.tokenize
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

        input = generateSequence<String> {
            return@generateSequence bufferedReader.readLine()
        }
    }

    @Test //java.lang.Exception: Method parseNal1() should be void
    fun loadNal1() {

        input.forEach {
            print(it);

        }

    }

    @Test
    fun tokenizeNal1() {
        val narseParser = NarseParser()

        val s = input.first()
        val tokenize = tokenize(s)
        tokenize.forEach {
            println(it)
        }

    }

    @Test
    fun parseExperiment() {
        val narseParser = NarseParser()

        val s = input.first()
        val tokenize = tokenize(s)
        val ex = experiment(tokenize)
        println(ex)
    }
}