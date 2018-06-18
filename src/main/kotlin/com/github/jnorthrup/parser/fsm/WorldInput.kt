package com.github.jnorthrup.parser.fsm

import kotlin.coroutines.experimental.AbstractCoroutineContextElement
import kotlin.coroutines.experimental.CoroutineContext

class WorldInput(val lines: Sequence<CharSequence>) : AbstractCoroutineContextElement(WorldInput) {
    companion object Key : CoroutineContext.Key<WorldInput>
}