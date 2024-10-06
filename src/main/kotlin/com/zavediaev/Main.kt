package com.zavediaev

fun main(args: Array<String>) {
    val path = args.firstOrNull() ?: "/Users/zavediaev-work/workspace/1brc/measurements.txt"

    // time when running java -cp

//    attempt1(path) // 3:50.75 = 230.75
//    attempt2(path) // 2:38.56 = 158.56
//    attempt3(path) // 42.968
//    attempt4(path) // 5.251
//    attempt5(path) //  5.997
    attempt6(path) // 5.678


    // fastest file read: 1.5 sec - so processing takes ~ 4 sec
}