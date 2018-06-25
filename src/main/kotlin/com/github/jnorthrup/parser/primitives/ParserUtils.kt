package com.github.jnorthrup.parser.primitives

import com.github.jnorthrup.parser.overloads.RewindableSequence

/**
 * the rewindable sequence
 *
 */
typealias Line = RewindableSequence<String>

/**
 * the main lambda that parses the Line
 */
typealias  op = (Line) -> Any?

/**
 * validates a pair of op in order
 */
infix fun op.then(next: op): op = { line: Line ->
    (!line).let { clone: Line ->
        var res = emptyList<Any?>()
        try {
            var it1 = this(line)

            if (it1 != null) res = res + it1
            else {
                throw ParseFail
            }
            it1 = next(line)
            if (it1 != null) res + it1
            else {
                throw ParseFail
            }
        } catch (fail: Exception) {
            line %= clone
            throw ParseFail
        }

    }
}


/** one or placeholder
 * @return  one element, potentially emptySequence*/
fun opt(of: op): op = { line: Line ->
    (!line).let { clone: Line ->
        var res = emptyList<Any?>()
        try {
            System.err.println("%%% opt begin")

            var it1 = of(line)
            if (it1 != null) {
                System.err.println("%%% opt adding" + it1)
                res += it1
            }
        } catch (x: Signal) {
            System.err.println("%%% opt exitting via signal " + x)
            line %= clone
        }

        System.err.println("%%% opt returning " + res)
        res
    }
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
    val regex = Regex(lit)

    val takeIf = line.mark().first().takeIf { s ->
        s.matches(regex)
    }
    if (takeIf != null) takeIf
    else {
        line.reset()
        throw  ParseFail
    }
}

/**
 * string literal test
 */
fun lit(s: String): op = { line ->
    val first = line.mark().first()
    val takeIf = first.takeIf(s::equals)
    if (takeIf != null) {
        System.err.println("%%% " + s + " == " + first + " @ " + line.toString())
        takeIf
    } else {

        System.err.println("%%% " + s + " != " + first + " @ " + line.toString())
        line.reset()
        throw   ParseFail
    }


}

/**
 * negates a match and returns success
 *
 */
fun negate(function: op): op = { line: Line ->
    (!line).let { clone ->
        val any = try {
            function(line)
        } catch (e: Throwable) {
            null
        }
        if (any == null) {
            line %= clone
            emptySequence<Any?>()
        } else {
            line %= clone
            throw ParseFail
        }
    }
}


/** sequence
 *  @return Sequence of success */
fun seq(of: op): op = { line: Line ->
    System.err.println("%%% seq starts with " + line.toString())


    val toList = lazySeq(line, of).toList()
    System.err.println("%%% seq returning " + "" + toList)
    val takeUnless = toList.takeUnless(List<Any>::isEmpty)
    if (takeUnless != null) {
        System.err.println("%%% seq returning " + toList + " : " + line.toString())
        takeUnless
    } else {
        System.err.println("%%% seq throwing ParseFail state: " + line.toString())
        throw ParseFail
    }
}

/**
 * this parses a seuqnce forever, until null or signal
 *
 */
fun lazySeq(line: Line, of: op): Sequence<Any> = generateSequence {
    (!line).let { clone ->
        try {
            try {

                val of1 = of(line)
                System.err.println("%%% lazySeg+= " + of1)
                of1
            } catch (eol: NoSuchElementException) {
                System.err.println("%%% lazySeg hit eol ")
                throw Eol
            }
        } catch (e: Signal) {
            if (e !is Eol) {
                System.err.println("%%% lazySeg caught signal " + e)
//                line %= clone
            }
            System.err.println("%%% lazySeg ending " + line.toString())
            null
        }
    }
}


/**
 * these are monads affecting conrol flow
 */
open class Signal : Throwable()

object Eol : Signal()
object ParseFail : Signal()
object GotoNext : Signal()
object ParseSuccess : Signal()

