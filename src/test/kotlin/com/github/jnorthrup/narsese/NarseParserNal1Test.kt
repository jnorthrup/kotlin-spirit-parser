package com.github.jnorthrup.narsese

/*
class NarseParserNal1Test {
    lateinit var input: Sequence<String>
    @Before
    fun setup() {

        val resourceAsStream = NarseParserNal1Test::class.java.classLoader.getResourceAsStream("Examples/Example-NAL1-edited.txt")

        val bufferedReader = BufferedReader(InputStreamReader(resourceAsStream))

        input = generateSequence {
            return@generateSequence bufferedReader.readLine()
        }
    }

    @Test //java.lang.Exception: Method parseNal1() should be void
    fun loadNal1() {

        input.forEach {
            print(it)

        }

    }

    @Test
    fun tokenizeNal1() {

        val s = input.first()
        val tokenize = tokenize(s)
        tokenize.forEach {
            println(it)
        }

    }

    */
/**
     * this appears tp the be the perfect terminated line parse with the right amount of catch/goto
     *
 *//*

    @Test
    fun parseExperiment() {
        val s = input.first()
        val line = tokenize(s)
        val grammar: op = experiment
        top@
        try {
            for (r: Any? in generateSequence {
                grammar(line)
            }) {
                when (r) {
                    is Iterable<*> -> r.forEach(::println)
                    is Sequence<Any?> -> for (any in r) println(any)
                    else -> println(arrayOf(r).contentDeepToString())
                }

            }
        } catch (_: Signal) {
            null
        }
    }

    @Test
    fun testOr() {
        val comment = ("//" / "OUT:") + seq (re(".*"))
        val grammar = comment / task / experiment / sentence
        top@

        for (line in input.map(::tokenize)) {
            line % "+"

            for (r: Any? in generateSequence {
                grammar(line)
            }) when (r) {
                is Iterable<*> -> r.forEach(::println)
                is Sequence<Any?> -> for (any in r) {
                    println(any)
                }
                else -> println(arrayOf(r).contentDeepToString())
            }
        }

    }
}

*/
