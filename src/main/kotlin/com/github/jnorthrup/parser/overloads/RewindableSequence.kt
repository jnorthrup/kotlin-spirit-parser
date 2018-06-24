package com.github.jnorthrup.parser.overloads


/**
 *
 * if the sequence is overrun, there is a list exception thrown.
 *
 */
class RewindableSequence<T : Any>(val origin: List<T>, var mark: Int = 0, var pos: Int = mark) : Sequence<T> {
    override fun iterator(): Iterator<T> = generateSequence {
        when {
            origin.size > pos -> origin[pos++]
            else -> null
        }

    }.iterator()

    constructor(vararg x: T) : this(listOf<T>(*x))

    fun mark() = apply { mark = pos }
    fun reset() = apply { pos = mark }
    fun rewind() = apply { mark = 0;pos = 0 }
    /**
     * use for save state before a tx
     * @return a clone and not the line.
     */
    operator fun not() = RewindableSequence(origin, mark)

    /**
     * rollback to this clone's state.  copy
     *
     */
    operator fun remAssign(tmp: RewindableSequence<T>) {
        assert(tmp.origin === origin)
        pos = tmp.pos
        mark = tmp.mark
    }

    override fun toString(): String {
        return arrayOf(super.toString(), origin.toString(), mark, pos).contentDeepToString()
    }
}

