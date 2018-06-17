package com.github.jnorthrup.parser.primitives
import com.github.jnorthrup.parser.fsm.TokenizedLine
import com.github.jnorthrup.parser.overloads.`==`
import kotlin.coroutines.experimental.coroutineContext


fun opt(vararg of: `==`): `==` = {
    for (function in of) {
        val reset = coroutineContext[TokenizedLine]!!.reset()
        val invoke = function.invoke(reset)
    }
}

/*one or more*/
fun seq(delim: String, vararg of: `==`): `==` = {
    for (function in of) {
        val reset = coroutineContext[TokenizedLine]!!.reset()
        val invoke = function.invoke(reset)
    }
}

/* kleen star anyOf*/
fun first(vararg of: `==`): `==` = {
    for (function in of) {
        val reset = coroutineContext[TokenizedLine]!!.reset()
        val invoke = function.invoke(reset)
    }
}


/**allOf*/
fun allOf(vararg of: `==`): `==` = {
    var kill = false
    val res = mutableListOf<Any>()
    val iterator = of.iterator()
    loop@ while (iterator.hasNext() && !kill) {
        val f = iterator.next()
        val next = f(it)
        when {
            next == Unit -> {
                kill = true
                break@loop
            }
            else -> {
                res += next
            }
        }

    }
    when {
        kill -> Unit.also { coroutineContext[TokenizedLine]!!.reset() }
        else -> res
    }

}