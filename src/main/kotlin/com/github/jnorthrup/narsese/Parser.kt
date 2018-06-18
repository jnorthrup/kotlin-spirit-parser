package com.github.jnorthrup.narsese

import com.github.jnorthrup.narsese.Parser.Companion.tokenize
import com.github.jnorthrup.parser.fsm.Grammar
import com.github.jnorthrup.parser.fsm.LineTokenizer
import com.github.jnorthrup.parser.fsm.SemanticActions
import com.github.jnorthrup.parser.fsm.WorldInput
import com.github.jnorthrup.parser.overloads.RewindableSequence
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch


class Parser {
    val publish: Channel<Any> = Channel()
    fun parse(input: Sequence<String>) {
        val worldInput = WorldInput(input)
        val grammar = Grammar(task)
        val lineTokenizer = LineTokenizer(::tokenize)
        val semanticActions = SemanticActions(mutableMapOf())
        launch(worldInput + grammar + lineTokenizer+ semanticActions) {
            for (inputLine in input) {


            }
        }

    }


    object Companion {
        /**
         * {-] is an expensive maintenance policy
         */
        val narsHasBadManners = Regex("(\\{(?!-)?]|[)(}\\[]|\\b)")

        fun tokenize(it: String) = RewindableSequence(it.replace(narsHasBadManners, " $1 ")
                .split(Regex("\\s+"))
                .map(String::trim)
                .filterNot(String::isEmpty))
    }
}