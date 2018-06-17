package com.github.jnorthrup.narsese

import com.github.jnorthrup.parser.fsm.Grammar
import com.github.jnorthrup.parser.fsm.TokenizedLine
import com.github.jnorthrup.parser.fsm.WorldInput
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch

class Parser {
    val publish: Channel<Any> = Channel()
    fun parse(input: Sequence<String>) {

        launch(WorldInput(input) + Grammar(task )) {
            for (inputLine in input) {
                launch(TokenizedLine(inputLine)) {


                }
            }
        }

    }
}