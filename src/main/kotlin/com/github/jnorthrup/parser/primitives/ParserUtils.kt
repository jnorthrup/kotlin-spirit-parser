package com.github.jnorthrup.parser.primitives

import com.github.jnorthrup.parser.overloads.RewindableSequence


typealias `~` = Unit
typealias Line = RewindableSequence<String>
typealias `==` = (Line) -> Any?

/**
 * zero or one or throw
 *
 * @return Sequence of success
 *
 */
fun opt(vararg of: `==`): `==` = { throw  ParseFail(it, *of) }

/**delimitted sequence
 *
 * @return Sequence of success
 *
 */

fun seq(vararg of: `==`): `==` = { throw  ParseFail(it, *of) }


/**
 * roll through a list or operands until one is not null.
 *
 *
 */

fun first(vararg p: `==`): `==` = { line: Line ->
    var res: Any? = null
    for (f in p) {
        res = f(line.reset()) ?: continue

    }
    res ?: throw ParseFail(line)
}


class ParseFail(line: Line, vararg of: `==`) : Throwable()
