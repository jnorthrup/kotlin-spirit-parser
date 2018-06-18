package com.github.jnorthrup.parser.primitives

import com.github.jnorthrup.parser.overloads.RewindableSequence


typealias Line = RewindableSequence<String>
typealias  op = (Line) -> Any?

infix fun (op).then(p: op): (Line) -> op {
    return { line: Line ->
        var r = this
        r(line)?.let {
            r = p
            r(line)
        }
                ?: throw ParseFail(line, r)
        r
    }
}

/**
 * simple regexer
 */
fun re(lit: String): op = {
    val token = it.first()
    token.takeIf {
        it.matches(Regex.fromLiteral(lit))
    } ?: throw ParseFail(it, lit)
}


/**delimitted sequence
 *
 * @return Sequence of success
 *
 */

fun seq(vararg of: op): op = { throw  ParseFail(it, *of) }

/**
 * roll through a list or operands until one is not null.
 *
 *
 */

fun first(vararg p: op): op = { line: Line ->
    val tmp = line.clone()

    var res: Any? = null
    for (f in p) {
        res = f(tmp.reset()) ?: continue

    }
    res?.apply{ line.copy(tmp) }
            ?: throw ParseFail(line, *p)
}


/**
 * one or Unit placeholder
 *
 * @return Sequence of success
 *
 */
fun opt(vararg of: op): op = {
    try {
        first(*of)
    } catch (e: ParseFail) {
        Unit
    }
}

class ParseFail(val line: Line,  vararg val of: Any) : Throwable()
