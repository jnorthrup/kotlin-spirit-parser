package com.github.jnorthrup.parser.fsm

import com.github.jnorthrup.parser.overloads.`==`
import kotlin.coroutines.experimental.AbstractCoroutineContextElement
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.coroutineContext


class WorldInput(val lines: Sequence<CharSequence>) : AbstractCoroutineContextElement(WorldInput) {
    companion object Key : CoroutineContext.Key<WorldInput>
}


class TokenizedLine(val inputLine: String) : AbstractCoroutineContextElement(TokenizedLine) {
    companion object Key : CoroutineContext.Key<TokenizedLine>

    var pos = 0
    var mark: Int = 0

    val line = inputLine.split(Regex("\\s+"))
    val limit = line.lastIndex

    fun advance() = generateSequence {
        mark = pos
        when {pos < limit -> line[pos++]
            else -> null
        }
    }


    fun reset(): Sequence<String> {
        pos = mark
        return this.advance()
    }


    suspend fun parse(clause: Sequence<`==`>) {
        for (function in clause) function.invoke(coroutineContext[TokenizedLine]!!.reset())
    }
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