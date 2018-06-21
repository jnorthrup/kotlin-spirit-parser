package com.github.jnorthrup.parser.primitives

import com.github.jnorthrup.parser.overloads.RewindableSequence

typealias Line = RewindableSequence<String>
typealias  op = (Line) -> Any?

infix fun (op).then(p: op): (Line) -> op = { line: Line ->
    var r = this
    r(line)?.let {
        r = p
        r(line)
    }
            ?: throw ParseFail(line, r)
    r
}

/**
 * simple regexer
 */
fun re(lit: String): op = {
     it.first().takeIf {
        it.matches(Regex(lit))
    } ?: throw ParseFail(it, lit)
}


fun negate(function: op):op = { it: Line ->
    val b = try {
        val any = function(it.clone())
        true
    } catch (success: ParseFail) {
        false
    }
    if (b) throw  ParseFail(it, "negation failed for " + function)
    emptySequence<Any?>()
}

/**delimitted sequence @return Sequence of success */
fun seq(of: op): op = { line: Line ->
    var sentinal=false

    val seq = generateSequence {

        try {
            of(line.mark()).also { sentinal = true }
        } catch (e: Throwable) {
            null.also{line.reset()}
        }
    }.toList()
    when {(sentinal)->seq
    else  ->throw ParseFail(line)}
}

/** roll through a list or operands until one is not null. */
fun first(vararg p: op): op = { line: Line ->
    val tmp = line.clone()
    var res: Any? = null
    for (f in p) res = f(tmp.reset()) ?: continue
    res?.apply { line.copy(tmp) }
            ?: throw ParseFail(line, *p)
}

/** one or Unit placeholder
 * @return Sequence of success */
fun opt(vararg of: op): op = {
    try {
        first(*of)
    } catch (e: ParseFail) {
        Unit
    }
}

class ParseFail(val line: Line, vararg val of: Any) : Throwable()
