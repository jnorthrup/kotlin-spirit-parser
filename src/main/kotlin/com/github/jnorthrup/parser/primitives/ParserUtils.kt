package com.github.jnorthrup.parser.primitives

import com.github.jnorthrup.parser.overloads.RewindableSequence

typealias Line = RewindableSequence<String>
typealias  op = (Line) -> Any?


infix fun op.then(next: op): op = { line: Line ->
    (!line).let { clone ->
        sequenceOf(op@ this, next).map { it(line) ?: (throw ParseFail.also { line - clone }) }
    }
}

fun tx(function: op): op = { line: Line ->
    (!line).let { clone -> function(line) ?: throw ParseFail.also { line - clone } }
}

/** roll through a list or operands until one is not null. */
fun first(vararg of: op): op = { line ->
    (!line).let { clone ->
        var r: Any? = null
        for (f in of) {
            try {
                r = f(line)
                if (null != r) break
            } catch (pf: ParseFail) {
                null.also { line.copy(clone) }
            }
        }
        r ?: throw ParseFail
    }
}

/**
 * simple regexer
 */
fun re(lit: String): op = { line: Line ->
}


fun negate(function: op): op = { it: Line ->
}

/**delimitted sequence @return Sequence of success */
fun seq(of: op): op = { line: Line ->
}

/** one or Unit placeholder
 * @return Sequence of success */
fun opt(of: op): op = { line: Line ->
}


open class Signal : Throwable()

object Eol : Signal()
object ParseFail : Signal()
object GotoNext : Signal()
object ParseSuccess : Signal()

