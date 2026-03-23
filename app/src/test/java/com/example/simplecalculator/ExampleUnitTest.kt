package com.example.simplecalculator

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val str : String = "1+1"
        assertEquals(2.0, _calculate_result(str), 0.0001)
    }
    @Test
    fun time_isCorrect(){
        val str : String = "7*7"
        assertEquals(49.0, _calculate_result(str), 0.0001)
    }
    @Test
    fun e_isCorrect(){
        val str : String = "e*e"
        println(_calculate_result(str))
    }
    @Test
    fun complexTest(){
        val str : String = "7+2*7/8+2.3"
        assertEquals(11.05, _calculate_result(str), 0.00001)
    }
}