package com.github.jnorthrup.parser.fsm

import com.github.jnorthrup.parser.overloads.RewindableSequence
import com.github.jnorthrup.parser.primitives.Line
import com.github.jnorthrup.parser.primitives.`==`
import kotlin.coroutines.experimental.AbstractCoroutineContextElement
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.coroutineContext
import kotlin.text.Regex.Companion.fromLiteral


class WorldInput(val lines: Sequence<CharSequence>) : AbstractCoroutineContextElement(WorldInput) {
    companion object Key : CoroutineContext.Key<WorldInput>
}


class TokenizedLine(val inputLine: String) : AbstractCoroutineContextElement(TokenizedLine) {

    companion object Key : CoroutineContext.Key<TokenizedLine>


    val replace =  inputLine    .split(Regex("\\s+"))

    val line = RewindableSequence(replace)

}

/**
 * this CoroutineContext holds, and enables execution, of semantic actions.
 */

class SemanticActions(val onSuccess: MutableMap<`==`, (Any) -> Unit>) : AbstractCoroutineContextElement(SemanticActions) {

    operator fun set(suspendFunction1: `==`, value: (Any) -> Unit) {
        onSuccess[suspendFunction1] = value
    }

    companion object Key : CoroutineContext.Key<SemanticActions>
}

class Grammar(vararg val tokens: `==`) : AbstractCoroutineContextElement(Grammar) {
    companion object Key : CoroutineContext.Key<Grammar>
}
