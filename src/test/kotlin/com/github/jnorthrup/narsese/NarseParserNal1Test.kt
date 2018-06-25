package com.github.jnorthrup.narsese

import com.github.jnorthrup.parser.overloads.get
import com.github.jnorthrup.parser.overloads.invoke
import com.github.jnorthrup.parser.overloads.plus
import com.github.jnorthrup.parser.overloads.rem
import com.github.jnorthrup.parser.primitives.Line
import com.github.jnorthrup.parser.primitives.re
import org.junit.Test


class NarseParserNal1Test {
    @Test
    fun testThen() = ("a"() + "b") % Line("a", "b")

    @Test
    fun parseComment() = "//"[(re(".*"))] % Line("//", "comment", "here")

//    @Test
//    fun parseComment1() = ("//"() + seq(re(".*"))) % Line("//", "comment", "here")

//    @Test
//    fun parseComment2() = ("//"[seq(re(".*"))]) % Line("//", "comment", "here")
}
