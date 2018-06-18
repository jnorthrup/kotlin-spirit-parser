package com.github.jnorthrup.parser.fsm

import com.github.jnorthrup.parser.primitives. op
import kotlin.coroutines.experimental.AbstractCoroutineContextElement
import kotlin.coroutines.experimental.CoroutineContext

/**
 * this CoroutineContext holds, and enables execution, of semantic actions.
 */

class SemanticActions(val onSuccess: MutableMap< op , (Any) -> Unit>) : AbstractCoroutineContextElement(SemanticActions) {
    operator fun set(operand:  op , value: (Any) -> Unit) {
        onSuccess[operand] = value
    }
    companion object Key : CoroutineContext.Key<SemanticActions>
}