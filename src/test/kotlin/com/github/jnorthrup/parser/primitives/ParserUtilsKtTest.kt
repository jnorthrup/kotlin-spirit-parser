package com.github.jnorthrup.parser.primitives

import com.github.jnorthrup.parser.overloads.*
import org.junit.Test


class ParserUtilsKtTest {
    @Test
    fun testThen() = ("a"() + "b") % Line(listOf("a", "b"))

    @Test
    fun testThenThen0() = ("a"() + "b" + "c") % Line(listOf("a", "b", "c"))

    @Test
    fun testThenThen1() = ("a"() + "b" + "c") % Line(listOf("a", "b", "c"))

    @Test
    fun testThenThenOr() = ("a"() + "b" + "c" / "d") % Line(listOf("a", "b", "d"))

    @Test
    fun testThenThenOrFail() = try {
        ("a"() + "b" + "c" / "d") % Line(listOf("a", "b", "a"))
    } catch (success: ParseFail) {
    }

    @Test
    fun testNot() = (!"d"()) % Line(listOf("a"))

    @Test
    fun testNotThen() = (!"d" + "a") % Line(listOf("a"))

    @Test
    fun testSeq() = seq("a"()) % Line(listOf("a", "a", "a"))

    @Test
    fun testThenSeq() = ("b"() + seq("a"())) % Line(listOf("b", "a", "a", "a"))

    @Test
    fun testOptThenSeq0() = (Unit["-"()] + "b"() + seq("a"())) % Line(listOf("b", "a", "a", "a"))

    @Test
    fun testLineResume() {
        val line = Line("b", "a", "a", "a")
        (Unit["-"()] + "b"()) % line
        seq("a"()) % line
    }

    @Test
    fun testOptThenSeq1() = (Unit["-"()] + "b"() + seq("a"())) % (Line(listOf("-", "b", "a", "a", "a")))

    @Test
    fun testSeqThenOp0() = (seq("a"()) + Unit["-"()] + "b"()) % (Line(listOf("a", "a", "b")))

    @Test
    fun testSeqThenOpt1() = (seq("a"())["-"()] + "b"()) % (Line(listOf("a", "-", "b")))

    @Test
    fun testSeqThenOpt2() = (seq("a"())["-"()] + "b"()) % Line("a", "a", "a", "b")
}
