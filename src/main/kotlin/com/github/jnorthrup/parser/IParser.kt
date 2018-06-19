package com.github.jnorthrup.parser

import kotlinx.coroutines.experimental.channels.Channel

interface IParser {
    val publish: Channel<Any>
    suspend fun parse( )
}