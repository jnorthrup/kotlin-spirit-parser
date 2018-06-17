package com.github.jnorthrup.parser.fsm

import com.github.jnorthrup.parser.fsm.NarseseTokenizedLine.Companion.narsTokens
import com.github.jnorthrup.parser.primitives.Line
import com.github.jnorthrup.parser.primitives.`==`
import kotlin.coroutines.experimental.AbstractCoroutineContextElement
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.coroutineContext

class NarseseTokenizedLine(val inputLine: String) : AbstractCoroutineContextElement(NarseseTokenizedLine) {

    companion object Key : CoroutineContext.Key<NarseseTokenizedLine>

    /**
     * this is for the benefit of nars alone
     */
    val line = Line(narsTokens(inputLine))

    suspend fun parse(clause: Sequence<`==`>) {
        for (function in clause) function.invoke(coroutineContext[NarseseTokenizedLine]!!.reset())
    }

    fun reset() = line.reset()
public
    object Companion {
        /**
         * {-] is an expensive maintenance policy
         */
        val narsHasBadManners = Regex("(\\{(?!-)?]|[)(}\\[]|\\b)")

       public  fun narsTokens(it: String): List<String> {
            return it.replace(narsHasBadManners, " $1 ")
                    .split(Regex("\\s+"))
                    .map(String::trim)
                    .filterNot { it.isEmpty() }
        }
    }
}

