package com.github.jnorthrup.parser.fsm

import com.github.jnorthrup.parser.primitives. op
import kotlin.coroutines.experimental.AbstractCoroutineContextElement
import kotlin.coroutines.experimental.CoroutineContext

class Grammar(vararg val tokens:  op ) : AbstractCoroutineContextElement(Grammar) {
    companion object Key : CoroutineContext.Key<Grammar>
}