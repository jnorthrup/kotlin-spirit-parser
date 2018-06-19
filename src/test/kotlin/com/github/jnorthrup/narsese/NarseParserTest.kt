package com.github.jnorthrup.narsese

import kotlinx.coroutines.experimental.launch
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

class NarseParserTest {


    @Test
    suspend fun parseNal1():Unit=
        launch {

            val parser = NarseParser()
            parser.parse(
                    generateSequence {
                        BufferedReader(InputStreamReader(
                                (NarseParserTest::class.java.classLoader.getResourceAsStream("/Examples/Example-NAL1-edited.txt"))
                        )).readLine()
                    }
            )
            for (publish in parser.publish) {
                print("" + publish)
            }
        }.join()

}