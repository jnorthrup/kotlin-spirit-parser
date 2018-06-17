package com.github.jnorthrup.parser.overloads

import com.github.jnorthrup.parser.fsm.SemanticActions
import com.github.jnorthrup.parser.primitives.*
import kotlin.coroutines.experimental.coroutineContext


operator fun `==`.plus(p: `==`) = { line: Line -> this(line)?.let { first: Any -> p(line)?.let { sequenceOf(first, it) } } }

operator fun `==`.plus(p: String) = this + p()
operator fun String.plus(p: `==`) = (this.invoke() + p)
operator fun `==`.times(p: `==`) = { this + first(p) }


operator fun `==`.times(p: String) = this * p()
operator fun `==`.div(p: `==`) = first(this, p)
operator fun `==`.div(p: String) = this / p()
operator fun String.div(p: `==`) = first(this(), p)
operator fun String.div(p: String) = this / p()
operator fun `==`.rangeTo(s: String) = seq(s(), this)
infix fun `==`.`|`(p: `==`) = this / p
infix fun `==`.`|`(p: String) = this / p
infix fun String.`|`(p: `==`) = this / p
infix fun String.`|`(p: String) = this / p
operator fun String.invoke(): `==` = { it: Line -> if (this.equals(it.first())) this else Unit }
operator fun Nothing.plus(x: `==`) = x
operator fun String.get(vararg x: `==`): `==` = { this + opt(*x) } as `==`
operator fun `==`.get(vararg x: `==`): `==` = { this + opt(*x) } as `==`
operator fun Any.get(vararg x: `==`): `==` = { opt(*x) } as `==`

/**
 * actions must always be attached within a suepnd function
 */
suspend operator fun `==`.invoke(r: (Any) -> Unit) = this.also { coroutineContext[SemanticActions]?.set(this, r) }
