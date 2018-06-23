package com.github.jnorthrup.parser.primitives

import com.github.jnorthrup.parser.overloads.div
import com.github.jnorthrup.parser.overloads.invoke
import com.github.jnorthrup.parser.overloads.plus
import org.junit.Test

class ParserUtilsKtTest {
    @Test
    fun testThen() {
        val x: op = "a"() + "b"
        val line: Line = Line(listOf("a", "b"))

        val x1 = x(line)
        (x1 as Sequence<Any?>).map { it }.forEach(::println)
        println(line.toString())
    }

    @Test
    fun testThenThen() {
        val x: op = "a"() + "b" + "c"
        val line: Line = Line(listOf("a", "b", "c"))

        val x1 = x(line)
        (x1 as Sequence<Any?>).forEach {
            println(
                    when (it) {is Sequence<Any?> -> it.toList()
                        else -> it
                    })
        }

        @Test
        fun testThenThen() {
            val x: op = "a"() + "b" + "c"
            val line: Line = Line(listOf("a", "b", "c"))

            val x1 = x(line)
            (x1 as Sequence<Any?>).forEach {
                println(
                        when (it) {is Sequence<Any?> -> it.toList()
                            else -> it
                        })
            }


        }
    }

    @Test
    fun testThenThenOr() {
        val x: op = "a"() + "b" + "c" / "d"
        val line: Line = Line(listOf("a", "b", "d"))

        val x1 = x(line)
        (x1 as Sequence<Any?>).forEach {
            println(

                    when (it) {is Sequence<Any?> -> it.toList()
                        else -> it
                    })
        }


    }

}