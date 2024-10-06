package com.zavediaev.obrc

import com.zavediaev.obrc.attempt1.calc as attempt1
import com.zavediaev.obrc.attempt2.calc as attempt2
import com.zavediaev.obrc.attempt3.calc as attempt3
import com.zavediaev.obrc.attempt4.calc as attempt4
import com.zavediaev.obrc.attempt5.calc as attempt5
import com.zavediaev.obrc.attempt6.calc as attempt6

fun main(args: Array<String>) {
    val path = args.firstOrNull() ?: "/Users/zavediaev-work/workspace/1brc/measurements.txt"
    // fastest file read: ~1.5 sec

//    attempt1(path)
//    attempt2(path)
//    attempt3(path)
    attempt4(path) // best
//    attempt5(path)
//    attempt6(path)
}