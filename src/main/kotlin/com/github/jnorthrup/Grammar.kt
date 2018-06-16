package com.github.jnorthrup

import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.AbstractCoroutineContextElement
import kotlin.coroutines.experimental.*


/**


### GRAMMAR RULE                          BRIEF EXPLANATION

```
<task>  ::= [<budget>] <sentence>              // task to be processed
<sentence> ::= <statement>"." [<tense>][<truth>]  // judgment to be remembered
| <statement>"?" [<tense>]           // question to be answered
| <statement>"!" [<truth>]           // goal to be realized
<statement> ::= "<"<term> <relation> <term>">"     // two terms related to each other
| <term>                             // a term can name a statement
<relation> ::= "-->"                              // inheritance
| "<->"                              // similarity
| "{--"                              // instance
| "--]"                              // property
| "{-]"                              // instance-property
| "==>"                              // implication
| "=/>"                              // predictive implication
| "=|>"                              // concurrent implication
| "=\>"                              // retrospective implication
| "<=>"                              // equivalence
| "</>"                              // predictive equivalence
| "<|>"                              // concurrent equivalence
<term> ::= <word>                             // an atomic constant term
| <variable>                         // an atomic variable term
| <`compound_term`>                    // a term with internal structure
| <statement>                        // a statement can serve as a term
<`compound_term`> ::= "{" <term> {","<term>} "}"         // extensional set
| "[" <term> {","<term>} "]"         // intensional set
| "(&," <term> {","<term>} ")"       // extensional intersection
| "(|," <term> {","<term>} ")"       // intensional intersection
| "(-," <term> "," <term> ")"        // extensional difference
| "(~," <term> "," <term> ")"        // intensional difference
| "(*," <term> {","<term>} ")"       // product
| "(/," <term> {","<term>} ")"       // extensional image
| "(\," <term> {","<term>} ")"       // intensional image
| "(--," <term> ")"                  // negation
| "(||," <term> {","<term>} ")"      // disjunction
| "(&&," <term> {","<term>} ")"      // conjunction
| "(&/," <term> {","<term>} ")"      // sequential events
| "(&|," <term> {","<term>} ")"      // parallel events
<variable> ::= "#"<word>                          // independent variable
| "#"<word> "(" {"#"<word>} ")"      // dependent variable
| "#"                                // anonymous term as place holder
| "?"<word>                          // query variable for term to be find
<tense> ::= ":/:"                              // future event
| ":|:"                              // present event
| ":\:"                              // past event
<truth> ::= "%"<frequency>[";"<confidence>]"%" // two numbers in [0,1]x(0,1)
<budget> ::= "$"<priority>[";"<durability>]"$"  // two numbers in [0,1]x(0,1)
<word> : string in an alphabet
```
 */


typealias Line = Sequence<CharSequence>

typealias `==` = suspend (Line) -> Any

fun lit(s: String): `==` = { s like it }
val numerical: `==` = { "\\d+" like it }
val word: `==` = { it.first() }
val durability: `==` = numerical
val priority: `==` = numerical
val frequency: `==` = numerical
val confidence: `==` = numerical
val budget: `==` = `@`(lit("\$"), priority, `*`(lit(";"), durability), lit("\$"))
val truth: `==` = `@`(lit("%"), frequency, `*`(lit(";"), confidence), lit("%")) // two numbers in [0,1]x(0,1)
val variable: `==` = `*`(`@`(lit("#"), word)// independent variable
        , `@`(lit("#"), word, lit("("), seq(lit("#"), word), lit(")"))      // dependent variable
        , lit("#")                                // anonymous term as place holder
        , `@`(lit("?"), word))                      // query variable for term to be find
val tense: `==` = `*`(lit(":/:")                               // future event
        , lit(":|:")                           // present event
        , lit(":\\:")                           // past event
)

val  term: `==`
    get() = `*`(word                             // an atomic constant term
            , variable                          // `an atomic variable term
            , compound_term                     // a term with internal structure
            , statement // `a statement can serve as a term
    )
val statement: `==`
    get() = `*`(`@`(lit("<"), term, relation, term, lit(">"))     // two terms related to each other
            , term                              // a term can name a statement
    )
val sentence: `==` = `*`(`@`(statement, lit("."), opt(tense), opt(truth)) // judgment to be remembered
        , `@`(statement, lit("?"), opt(tense))// question to be answered
        , `@`(statement, lit("!"), opt(truth))// goal to be realized
)


val task: `==` = `@`(opt(budget), sentence)              // task to be processed
val relation: `==` = `*`(lit("-->")                              // inheritance
        , lit("<->")                            // similarity
        , lit("{--")                            // instance
        , lit("--]")                            // property
        , lit("{-]")                            // instance-property
        , lit("==>")                            // implication
        , lit("=/>")                            // predictive implication
        , lit("=|>")                            // concurrent implication
        , lit("=\\>")                              // retrospective implication
        , lit("<=>")                            // equivalence
        , lit("</>")                            // predictive equivalence
        , lit("<\\|>")                            // concurrent equivalence
)
val compound_term: `==` = `*`(lit("{"), term, seq(lit(","), term), lit("}")          // extensional set
        , `@`(lit("["), term, seq(lit(","), term), lit("]"))    // intensional set
        , `@`(lit("(&,"), term, seq(lit(","), term), lit(")"))    // extensional intersection
        , `@`(lit("(|,"), term, seq(lit(","), term), lit(")"))    // intensional intersection
        , `@`(lit("(-,"), term, lit(","), term, lit(")"))    // extensional difference
        , `@`(lit("(~,"), term, lit(","), term, lit(")"))    // intensional difference
        , `@`(lit("(*,"), term, seq(lit(","), term), lit(")"))    // product
        , `@`(lit("(/,"), term, seq(lit(","), term), lit(")"))    // extensional image
        , `@`(lit("(\\,"), term, seq(lit(","), term), lit(")"))    // intensional image
        , `@`(lit("(--,"), term, lit(")"))      // negation
        , `@`(lit("(||,"), term, seq(lit(","), term), lit(")"))    // disjunction
        , `@`(lit("(&&,"), term, seq(lit(","), term), lit(")"))    // conjunction
        , `@`(lit("(&/,"), term, seq(lit(","), term), lit(")"))    // sequential events
        , `@`(lit("(&|,"), term, seq(lit(","), term), lit(")"))    // parallel events
)


fun opt(vararg of: `==`): `==` = {
    for (function in of) {
        val reset = coroutineContext[TokenizedLine]!!.reset()
        val invoke = function.invoke(reset)
    }
}

/*one or more*/
fun seq(vararg of: `==`): `==` = {
    for (function in of) {
        val reset = coroutineContext[TokenizedLine]!!.reset()
        val invoke = function.invoke(reset)
    }
}

/* kleen star anyOf*/
fun `*`(vararg of: `==`): `==` = {
    for (function in of) {
        val reset = coroutineContext[TokenizedLine]!!.reset()
        val invoke = function.invoke(reset)
    }
}

infix fun String.like(line: Line) = line.first().let {
    when {
        it matches Regex(this) -> it
        else -> Unit
    }
}


/**allOf*/
fun `@`(vararg of: `==`): `==` = {
    var kill = false
    val res = mutableListOf<Any>()
    val iterator = of.iterator()
    loop@ while (iterator.hasNext() && !kill) {
        val f = iterator.next()
        val next = f(it)
        when {
            next == Unit -> {
                kill = true
                break@loop
            }
            else -> {
                res += next
            }
        }

    }
    when {
        kill -> Unit.also { coroutineContext[TokenizedLine]!!.reset() }
        else -> res
    }

}


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
        get() = field

    val line = inputLine.split(Regex("\\s+"))
    val limit = line.lastIndex

    fun advance() = generateSequence {
        mark = pos
        when {pos < limit -> line[pos++]
            else -> null
        }
    }


    fun reset(): Sequence<String> {
        pos = mark;
        return this.advance()
    }


    suspend fun parse(clause: Sequence<`==`>) {
        for (function in clause) function.invoke(coroutineContext[TokenizedLine]!!.reset())
    }
}


class Grammar(vararg val tokens: `==`) : AbstractCoroutineContextElement(Grammar) {
    companion object Key : CoroutineContext.Key<Grammar>
}