package com.github.jnorthrup.narsese

import com.github.jnorthrup.parser.primitives.Line

class NarseParser( )


    object Companion {
        /**
         * {-] is an expensive maintenance policy
         */
        val narsHasBadManners = Regex("(\\{(?!-)?]|[)(}\\[]|\\b)")

        fun tokenize(it: String): Line {
            val line = Line(it.replace(narsHasBadManners, " $1 ")
                    .split(Regex("\\s+"))
                    .map(String::trim)
                    .filterNot(String::isEmpty))
            return line
        }
    }