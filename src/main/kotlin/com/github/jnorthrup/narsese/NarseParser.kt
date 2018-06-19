package com.github.jnorthrup.narsese

import com.github.jnorthrup.narsese.NarseParser.Companion.tokenize
import com.github.jnorthrup.parser.IParser
import com.github.jnorthrup.parser.fsm.Grammar
import com.github.jnorthrup.parser.fsm.LineTokenizer
import com.github.jnorthrup.parser.fsm.SemanticActions
import com.github.jnorthrup.parser.fsm.WorldInput
import com.github.jnorthrup.parser.overloads.RewindableSequence
import com.github.jnorthrup.parser.overloads.invoke
import com.github.jnorthrup.parser.primitives.op
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch

class NarseParser (val input: Sequence<String>, private val onSuccess: MutableMap<op, (Any) -> Unit> = mutableMapOf<op, (Any) -> Unit>()): IParser {
    override val publish: Channel<Any> = Channel()
    val worldInput = WorldInput(input)
    val lineTokenizer = LineTokenizer(::tokenize)
    val semanticActions = SemanticActions(onSuccess)
    override suspend fun parse() {
        val narseGrammar = Grammar(

                task { System.err.println("insert sideffects here") } ,
                term { it: Any -> System.err.println("term returns:" + it) }
        )
        launch(worldInput + narseGrammar + lineTokenizer + semanticActions) {
            input.forEach { inputLine ->
                lineTokenizer.receive(inputLine)
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
