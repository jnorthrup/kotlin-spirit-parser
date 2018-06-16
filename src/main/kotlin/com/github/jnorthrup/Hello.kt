package com.github.jnorthrup

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * https://github.com/opennars/opennars/wiki/Single-Step-Testing-Cases#single-step-testing-cases

<p>
<pre>
There are example files in https://github.com/opennars/opennars/tree/1.6.4_devel/nal/Examples, showing conclusions of single-step inference.

For each level of NAL1 to NAL6, there are two files. The "edited" file only contains the conclusions produced by the rules defined at that level, while the "unedited" files contains the actual outputs for the given input. Different versions of the system may need to adjust the exact timing of input, so the exact input and output lines may be different from the following record, which was produced by Open-NARS 1.5.5.

Each file contains a sequence of examples. An example looks like the following:

 ********** revision
IN: <bird --> swimmer>. %1.00;0.90% {0 : 1}
IN: <bird --> swimmer>. %0.10;0.60% {0 : 2}
1
OUT: <bird --> swimmer>. %0.87;0.91% {1 : 1;2}
A Line starting with '*' indicates the beginning of a new example, and will initialize the system when feed into the demo as input. A Line starting with '/' is a comment. A Line starting with an optional "IN:" is an input task to the system, and a Line starting with "OUT:" is output from the system. A Line containing a single number indicates the number of inference cycles between in/out lines. Spaces, tabs, and blank lines are ignored.

String like "<bird --> swimmer>. %0.87;0.91%" is a Narsese sentence, and the grammar is specified in Input Output Format. The following "{1 : 1;2}" is a 'stamp' automatically attached by the system, to indicate the creation time and evidential base of that sentence.

To run the demonstration of Open-NARS under nars-dist, the user can copy/paste an example into the input window, then click "OK" to run the example. In this process, the output lines and the stamps will be ignored. The user can also load a complete example file, by choosing it from the Main Window, using "File/Load experience". Since the input/output Line produced in this way will be quickly overwritten in the display area, it is usually necessary to save it into a file, using "File/Save experience". The first time this item is selected, the user can open a (new or existing) file to catch the user/system communication. To close the file, select "File/Save experience" again.

With this file load/save function, each example file comes with two versions. The "edited" version is manually edited to only contain the relevant information for each example with approximate English translations, so is easier to understand; the "unedited" version contains all actual user-system communication as displayed then either version is loaded into the system.

The examples are grouped according to the order they are introduced in the book Non-Axiomatic Logic: A Model of Intelligent Reasoning. They are not self-explained, and the readers should consult the book to fully understand them.

Example-NAL1 shows the basic rules on atomic terms and the inheritance relation, including revision, deduction, abduction, induction, and question answering.

Example-NAL2 shows the inference on variants of inheritance (similarity, instance, property, etc.) and sets, and include rules like comparison and analogy.

Example-NAL3 shows compound term composition and decomposition rules, where the conpound terms are intersections or differences of simpler terms.

Example-NAL4 shows inference on ordinary (non-inheritance) relations, where the conpound terms are products and images.

Example-NAL5 shows basic higher-order inference, where a statement is treated as a term. Some of the inference here is isomorphic to the inference introduced in NAL1 and NAL3, and some others, like detachment and conditional inference, are new.

Example-NAL6 shows the use of variable terms in higher-order inference, including the rules for the unification, elimination, and introduction of variable terms.

The following files in the ZIP file of NARS 1.3.3 show the inference rules of NAL-7 and NAL-8, which will be added into the implementation in future versions.

Example-NAL7 shows temporal inference on events, which is roughly the rules of NAL5 and NAL6, plus temporal information.

Example-NAL8 shows procedural inference. It consists of 4 files, and is explained further in ProceduralExamples.
</pre>

 */

fun main(args: Array<String>) {

    val infile = args.first()

    // FSM
    val lineSeq = Files.readAllLines(Paths.get(infile)).asSequence()
    /*
        A Line starting with '*' indicates the beginning of a new example, and will initialize the system when feed into the demo as input. A Line starting with '/' is a comment. A Line starting with an optional "IN:" is an input task to the system, and a Line starting with "OUT:" is output from the system. A Line containing a single number indicates the number of inference cycles between in/out lines. Spaces, tabs, and blank lines are ignored.
    */

    for (s in lineSeq) {
        if (s.trim().startsWith("*")) break
    }

    var commandStack = mutableListOf<String>()
    val testPhase = mutableListOf<String>()
    loop@
    /*
           String like "<bird --> swimmer>. %0.87;0.91%" is a Narsese sentence, and the grammar is specified in Input Output Format. The following "{1 : 1;2}" is a 'stamp' automatically attached by the system, to indicate the creation time and evidential base of that sentence.
       */
    for (s in lineSeq) {

        val sideffects = when {
            s.trim().startsWith("OUT:") -> {
                val tokenToUse = s.split(Regex("\\s+"), 2).last()
                testPhase + tokenToUse
            }
            s.trim().startsWith("IN:") -> {
                val tokenToUse = s.split(Regex("\\s+"), 2).last()
                 commandStack + tokenToUse
            }
            s.trim() matches Regex("\\d+") -> {
                 ("resolving "+commandStack.toString() )
             }
            else -> {
                continue@loop
            }
        }
        System.err.println("; "+sideffects)

    }
}


