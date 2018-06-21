package com.github.jnorthrup.parser.fsm

import com.github.jnorthrup.parser.primitives.Line
import com.github.jnorthrup.parser.primitives.first
import kotlin.coroutines.experimental.AbstractCoroutineContextElement
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.coroutineContext


class LineTokenizer(val tokenize: (String) -> Line) : AbstractCoroutineContextElement(LineTokenizer) {
    companion object Key : CoroutineContext.Key<LineTokenizer>
    var line: Line? = null/*by lazy {tokenize(input)}*/

    suspend fun receive(input: String) {
        line = tokenize(input)
        val p = coroutineContext[Grammar]!!.tokens
        first(*p)
    }

    val reset = line?.reset()
}

