package com.github.jnorthrup.parser.overloads

/**
 *
 * if the sequence is overrun, there is a list exception thrown.
 *
 */
class RewindableSequence<T : Any>(val origin: List<T>, var mark: Int = 0, var pos: Int = 0) : Sequence<T> {
    override fun iterator(): Iterator<T> = generateSequence {  origin.takeIf {origin.size>pos  }?.let{  origin[pos++] } }.iterator()
    fun mark() = apply { mark = pos }
    fun reset() = apply { pos = mark }
    fun rewind() = apply { mark = 0;pos = 0 }
    fun clone() = RewindableSequence(origin, mark)
    fun copy(tmp: RewindableSequence<T>) {
        assert(tmp.origin === origin)
        pos = tmp.pos
        mark = tmp.mark
    }

    /**
     * if this happens, the door is open for a codesmell.
     */
    class ReuseError : Throwable()
}