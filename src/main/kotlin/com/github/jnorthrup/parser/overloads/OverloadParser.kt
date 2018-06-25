package com.github.jnorthrup.parser.overloads

import com.github.jnorthrup.parser.fsm.SemanticActions
import com.github.jnorthrup.parser.primitives.*
import kotlin.coroutines.experimental.coroutineContext


operator fun op.plus(p: op): op = this then p
operator fun op.plus(p: String) = this + p()
operator fun op.times(p: op) = this + first(p)
operator fun op.div(p: op) = first(this, p)
operator fun op.div(p: String) = this / p()
operator fun op.get(x: op) = this + opt(x)
operator fun op.get(x: String) = this.get(x())
operator fun op.rangeTo(s: op) = this[seq(s + this)]
operator fun op.rangeTo(s: String) = this..s()
operator fun op.rangeTo(more: Unit) = seq(this)
operator fun op.not(): op = negate(this)


/**string-literal*/
operator fun String.invoke(): op = lit(this)
operator fun String.not(): op = !this()
operator fun String.plus(p: op) = this() + p
operator fun String.div(p: op) = this() / p
operator fun String.div(p: String) = this / p()
operator fun String.get(x: op) = this() + opt(x)
operator fun String.get(x: String) = this() + opt(x())
operator fun String.rangeTo(s: String) = this()..s()
operator fun String.rangeTo(f: op) = this()..f
operator fun String.rangeTo(more: Unit) = this()..Unit

/**
 * Unit is an abbreviation for <emptyspace we cant express in kotlin operators>
 */

operator fun Unit.times(p: op) = first(p)
operator fun Unit.plus(x: op) = x
operator fun Unit.get(x: op) = opt(x)
operator fun Unit.get(x: String) = opt(x())
/** semantic actions must always be attached within a suspend function */
suspend operator fun op.invoke(r: (Any) -> Unit) = this.also { coroutineContext[SemanticActions]?.set(this, r) }

fun deep(x: Any?): String {
    return when (x) {
        is Sequence<*> -> deep(x.toList().toTypedArray())
        is Collection<*> -> deep((x).toTypedArray())
        is Array<*> -> x.map { deep(it) }.joinToString(",", "[", "]")
        else -> x.toString()
    }
}

/**
 * execution syntactic sugar
 */
operator fun Line.plus(f: op) {
    f(this)
}

/**
 * debug output
 */
infix operator fun Line.rem(t: String) = (!this).joinToString(t)


/**
 * debug syntactic sugar
 */
operator fun op.rem(line: Line) {


    val stackTrace = Thread.currentThread().stackTrace

    val name = (op@ this).javaClass.enclosingMethod.name
    println(stackTrace[2].methodName + "%" + name + "%" + line % " " + "%" + deep(this(line)))
}