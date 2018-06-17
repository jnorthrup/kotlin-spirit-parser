package com.github.jnorthrup.parser.overloads

import com.github.jnorthrup.parser.primitives.allOf
import com.github.jnorthrup.parser.primitives.first
import com.github.jnorthrup.parser.primitives.opt
import com.github.jnorthrup.parser.primitives.seq


typealias `~` = Unit
typealias Line = Sequence<CharSequence>
typealias `==` = suspend (Line) -> Any

operator fun `==`.plus(p: `==`) = allOf(this, p)
operator fun `==`.plus(p: String) = this + p()
operator fun String.plus(p: `==`) = (this.invoke() + p)
operator fun `==`.times(p: `==`) = this + first(p)
operator fun `==`.times(p: String) = this * p()
operator fun `==`.div(p: `==`) = first(this, p)
operator fun `==`.div(p: String) = this / p()
operator fun String.div(p: `==`) = first(this(), p)
operator fun String.div(p: String) = this / p()
operator fun `==`.rangeTo(s: String) = seq(s, this)
infix fun `==`.`|`(p: `==`) = this / p
infix fun `==`.`|`(p: String) = this / p
infix fun String.`|`(p: `==`) = this / p
infix fun String.`|`(p: String) = this / p
operator fun String.invoke(): `==` = { if (this.equals(it.first())) this else Unit }
operator fun Unit.plus(x: `==`) = x
operator fun String.get(vararg x: `==`) = { this + opt(*x) } as `==`
operator fun `==`.get(vararg x: `==`) = { this + opt(*x) } as `==`
operator fun Any.get(vararg x: `==`) = { opt(*x) } as `==`

