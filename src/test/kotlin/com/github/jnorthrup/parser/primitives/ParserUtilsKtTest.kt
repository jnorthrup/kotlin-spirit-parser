package com.github.jnorthrup.parser.primitives

import com.github.jnorthrup.parser.overloads.*
import org.junit.Assert
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
    fun testThenThenOrFail() {
        var success: Boolean = false
        try {
            ("a"() + "b" + "c" / "d") % Line("a", "b", "a")
        } catch (perfect: ParseFail) {
            success = true
        }
        Assert.assertTrue(success)
    }

    @Test
    fun testNot() = !"d"() % Line("a")

    @Test
    fun testre() = re(".*") % Line("a")

    @Test
    fun testSeqLit() = seq(("a")()) % Line("a", "a", "c", "jimi", "hedrix")

    @Test
    fun testSeqRe() = seq(re(".*")) % Line("a", "b", "c", "jimi", "hedrix")

    @Test
    fun testNotThen() = (!"d" + "a") % Line("a")

    @Test
    fun testSeq() = seq("a"()) % Line("a", "a", "a")

    @Test
    fun testThenSeq() = ("b"["a"]) % Line("b", "a", "a", "a")

    @Test
    fun testOptThenSeq0() = (Unit["-"] + "b" + "a"..Unit) % Line("b", "a", "a", "a")

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

    /**
     * a..[-]b
     */
    @Test
    fun testSeqThenOpt2() = (("a"..Unit)["-"] + "b"()) % Line("a", "a", "a", "b")

    @Test
    fun testRangeOfOne() = ("a"()..":"()) % Line("a", "b")

    @Test
    fun testRangeOfThree() = ("a"()..":"()) % Line("a", ":", "a", ":", "a", "b")

    @Test
    fun testRangeFailOn0() {
        var success = false
        try {
            ("a"()..":"()) % Line("b", "a", "a", "b")
        } catch (e: ParseFail) {
            success = true
        }
        Assert.assertTrue(success)
    }

    @Test
    fun testRangeOf3ThenPartialSuccess() = ("a"()..":"()) % Line("a", ":", "a", ":", "a", ":", "b")
}
