package com.github.jnorthrup.narcheese

import com.github.jnorthrup.parser.overloads.*

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
| <compound_term>                    // a term with internal structure
| <statement>                        // a statement can serve as a term
<compound_term> ::= "{" <term> {","<term>} "}"         // extensional set
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
val numeric: `==` = { it.first().let { if (it.matches(Regex("\\d+"))) it else Unit } }
val word: `==` = { it.first().let { if (it.matches(Regex("\\w+"))) it else Unit } }
val sentence // judgment to be remembered
    get() = statement + (
            ("."[tense][truth]) /
                    ("?"[tense]) /   /* question to be answered*/
                    ("!"[truth]))   // goal to be realized

val statement       // two terms related to each other
    get() = ("<" + term + relation + term + ">") / term  // a term can name a statement
val relation: `==` = "-->" /   /* inheritance*/
        "<->" /   /* similarity*/
        "{--" /   /* instance*/
        "--]" /   /* property*/
        "{-]" /   /* instance-property*/
        "==>" /   /* implication*/
        "=/>" /   /* predictive implication*/
        "=|>" /   /* concurrent implication*/
        "=\\>" /     /* retrospective implication*/
        "<=>" /   /* equivalence*/
        "</>" /   /* predictive equivalence*/
        "<|>"               /* concurrent equivalence*/
val compound_term =
        ("{"() + term.."," + "}") /             // extensional set
                "[" + term.."," + "]" /       // intensional set
                "(" + "--" + "," + term + ")" /                     // negation
                ("("() + ("-" /   // extensional difference
                        "~") + "," + term + "," + term + ")") /    // intensional difference
                (("&"() /    // extensional intersection
                        "|" /    // intensional intersection
                        "*" /    // product
                        "/" /    // extensional image
                        "\\" /    // intensional image
                        "||" /    // disjunction
                        "&&" /    // conjunction
                        "&/" / // sequential events
                        "&|") + "," + term.."," + ")")                         // parallel events
val variable: `==` = ("#" + word) /  // independent variable
        ("#" + word + "("["#"() + word] + ")") /  // dependent variable
        "#" /           // anonymous term as place holder
        ("?" + word)// query variable for term to be find
val term: `==`                          // a statement can serve as a term
    get() = word /                          // an atomic constant term
            variable /         // an atomic variable term
            compound_term /         // a term with internal structure
            statement
val tense: `==` = ":/:"() /   // future event
        ":|:" /   // present event
        ":\\:"                  // past event
val frequency = numeric
val priority = numeric
val durability = numeric
val confidence = numeric
val budget: `==` = "$"() + numeric..";"+ "$"  // two numbers in [0,1]x(0,1)
val truth: `==` = "%"() + numeric..";"+ "%" // two numbers in [0,1]x(0,1)
val task = `~`[budget] + sentence
