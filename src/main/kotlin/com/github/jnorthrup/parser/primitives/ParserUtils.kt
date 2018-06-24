package com.github.jnorthrup.parser.primitives

import com.github.jnorthrup.parser.overloads.RewindableSequence

typealias Line = RewindableSequence<String>
typealias  op = (Line) -> Any?


infix fun op.then(next: op): op = { line: Line ->
    (!line).let { clone: Line ->
        arrayOf(this, next).map {
            it(line) ?: ((throw ParseFail).also {
                line %= clone
            })
        }
    }
}

fun tx(function: op): op = { line: Line ->
    (!line).let { clone -> function(line) ?: throw ParseFail.also { line %= (clone) } }
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
                null.also { line %= clone }
            }
        }
        r ?: throw ParseFail.also { line %= clone }
    }
}

/**
 * simple regexer
 */
fun re(lit: String): op = { line: Line ->
    try {
        line.mark().first().takeIf({ s -> s.matches(Regex(lit)) })
    } catch (e: NoSuchElementException) {
    } ?: throw ParseFail.also { line.reset() }
}


fun negate(function: op): op = { line: Line ->
    (!line).let { clone ->
        val any = try {
            function(line)
        } catch (e: Exception) {
        }
        when (any) {
            null -> emptySequence<Any?>()
            else -> throw ParseFail.also { line %= clone }
        }
    }
}


/** sequence
 *  @return Sequence of success */
fun seq(of: op): op = { line: Line ->
    lazySeq(line, of).toList().takeUnless(List<Any>::isEmpty) ?: throw ParseFail
}

fun lazySeq(line: Line, of: op): Sequence<Any> = generateSequence {
    (!line).let { clone ->
        try {
            try {
                of(line)
            } catch (eol: NoSuchElementException) {
                throw Eol
            }
        } catch (e: Signal) {
            null.also { line %= clone }
        }
    }
}

/** one or placeholder
 * @return  one element, potentially emptySequence*/
fun opt(of: op): op = { line: Line ->
    (!line).let { clone ->
        try {
            try {
                of(line)
            } catch (eol: NoSuchElementException) {
                throw Eol
            }
        } catch (e: Signal) {
            null.also { line %= clone }
        }
    } ?: emptySequence<Any?>()
}


open class Signal : Throwable()

object Eol : Signal()
object ParseFail : Signal()
object GotoNext : Signal()
object ParseSuccess : Signal()

