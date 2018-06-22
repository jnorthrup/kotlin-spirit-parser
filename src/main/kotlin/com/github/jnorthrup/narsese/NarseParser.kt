package com.github.jnorthrup.narsese

import com.github.jnorthrup.parser.primitives.Line

object NarseParser {


    /**
     * {-] is an expensive maintenance policy
     */
    val narsHasBadManners = Regex("(\\{(?!-)?]|[)(}\\[]|\\b)")

    fun tokenize(it: String): Line {
        return Line(it.replace(narsHasBadManners, " $1 ")
                .split(Regex("\\s+"))
                .map(String::trim)
                .filterNot(String::isEmpty))
    }
}