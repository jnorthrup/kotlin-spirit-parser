package com.github.jnorthrup

import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.AbstractCoroutineContextElement
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.coroutineContext


class Parser {
    val publish: Channel<Any> = Channel()
    fun parse(input: Sequence<String>) {

        launch(WorldInput(input) + Grammar(budget, word)) {
            for (inputLine in input) {
                launch(TokenizedLine(inputLine)) {


                }
            }
        }

    }
}

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


class Grammar(vararg val tokens: `==`) : AbstractCoroutineContextElement(Grammar) {
    companion object Key : CoroutineContext.Key<Grammar>
}