package com.github.jnorthrup.parser.overloads

import com.github.jnorthrup.parser.fsm.SemanticActions
import com.github.jnorthrup.parser.primitives.*
import kotlin.coroutines.experimental.coroutineContext

operator fun op.plus(p: op): op = this then p
operator fun op.plus(p: String) = this then p()
operator fun op.times(p: op) = this then first(p)
operator fun op.div(p: op) = first(this, p)
operator fun op.div(p: String) = this / p()
operator fun op.get(vararg x: op) = this + opt(*x)
operator fun op.rangeTo(s: op) = this[seq(s + this)]
operator fun op.rangeTo(s: String) = this.rangeTo(s())
operator fun op.not(): op = negate(this)
operator fun String.plus(p: op) = this() + p
operator fun String.div(p: op) = this() / p
operator fun String.div(p: String) = this / p()
operator fun String.get(vararg x: op) = this() + opt(*x)
/**string-literal*/
operator fun String.invoke(): op = { line -> line.first().takeIf(this::equals) ?: throw ParseFail(line, this) }

operator fun Unit.times(p: op) = first(p)
operator fun Unit.plus(x: op) = x
operator fun Unit.get(vararg x: op) = opt(*x)

/** semantic actions must always be attached within a suspend function */
suspend operator fun op.invoke(r: (Any) -> Unit) = this.also { coroutineContext[SemanticActions]?.set(this, r) }

