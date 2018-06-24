package com.github.jnorthrup.parser.primitives

import com.github.jnorthrup.parser.overloads.*
import org.junit.Test


class ParserUtilsKtTest {
    @Test
    fun testThen() = ("a"() + "b") % Line("a", "b")

    @Test
    fun testThenThen0() = ("a"() + "b" + "c") % Line("a", "b", "c")

    @Test
    fun testThenThen1() = ("a"() + "b" + "c") % Line("a", "b", "c")

    @Test
    fun testThenThenOr() = ("a"() + "b" + "c" / "d") % Line("a", "b", "d")

    @Test
    fun testThenThenOrFail() = try {
        ("a"() + "b" + "c" / "d") % Line("a", "b", "a")
    } catch (success: ParseFail) {
    }

    @Test
    fun testNot() = (!"d"()) % Line("a")

    @Test
    fun testNotThen() = (!"d" + "a") % Line("a")

    @Test
    fun testSeq() = seq("a"()) % Line("a", "a", "a")

    @Test
    fun testThenSeq() = ("b"() + seq("a"())) % Line("b", "a", "a", "a")

    @Test
    fun testOptThenSeq0() = (Unit["-"()] + "b"() + seq("a"())) % Line("b", "a", "a", "a")

    @Test
    fun testLineResume() {
        val line = Line("b", "a", "a", "a")
        (Unit["-"()] + "b") % line
        seq("a"()) % line
    }

    @Test
    fun testOptThenSeq1() = (Unit["-"()] + "b" + seq("a"())) % Line("-", "b", "a", "a", "a")

    @Test
    fun testSeqThenOp0() = (seq("a"()) + Unit["-"()] + "b"()) % Line("a", "a", "b")

    @Test
    fun testSeqThenOpt1() = seq("a"()["-"()] + "b"()) % Line("a", "-", "b")

    @Test
    fun testSeqThenOpt2() = (seq("a"())["-"()] + "b"()) % Line("a", "a", "a", "b")
}
