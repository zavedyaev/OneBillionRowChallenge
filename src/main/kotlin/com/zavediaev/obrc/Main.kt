package com.zavediaev.obrc

import com.zavediaev.obrc.attempt4.calc as attempt4


/**
 * compiled to jar using ./gradlew shadowJar
 * to start:
 * time java -cp OneBillionRowChallengeKotlin-1.0-SNAPSHOT-all.jar com.zavediaev.MainKt
 */

fun main(args: Array<String>) {
    val path = args.firstOrNull() ?: "/Users/zavediaev-work/workspace/1brc/measurements.txt"

    // time when running java -cp

//    attempt1(path)
//    attempt2(path)
//    attempt3(path)
    attempt4(path) // best
//    attempt5(path)
//    attempt6(path)


    // fastest file read: 1.5 sec - so processing takes ~ 4 sec
}