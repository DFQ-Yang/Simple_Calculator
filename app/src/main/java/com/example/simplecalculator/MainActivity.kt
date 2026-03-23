package com.example.simplecalculator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplecalculator.ui.theme.SimpleCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Calculator()
                }
            }
        }
    }
}

@Composable
fun Calculator() {
    var expression by remember{ mutableStateOf("") }
    var result: Double by remember{ mutableStateOf(0.0) }

    Row(Modifier.fillMaxWidth()){
        if(!expression.isEmpty()){
            Text(
                text = expression,
                fontSize = 50.sp
            )
        }
        else{
            Text(
                text = "$result",
                fontSize = 50.sp
            )
        }
    }

    Column(verticalArrangement = Arrangement.Bottom) {
        repeat(5) { rowIndex ->
            Row(
                modifier = Modifier.fillMaxWidth()
                ) {
                repeat(4) { colIndex ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .border(1.dp, Color.Gray)
                    ) {
                        when (rowIndex) {
                            0 if colIndex == 0 -> Button(onClick = {
                                expression = ""
                                result = 0.0
                            }) { Text("C") }
                            0 if colIndex == 1 -> Button(onClick = {
                                expression = expression.dropLast(1)
                            }) { Text("BackSpace") }
                            0 if colIndex == 2 -> Button(onClick = {
                                expression += '%'
                            }) { Text("%") }
                            0 if colIndex == 3 -> Button(onClick = {
                                expression += '/'
                            }) { Text("/") }
                            1 if colIndex == 0 -> Button(onClick = {
                                expression += '7'
                            }) { Text("7") }
                            1 if colIndex == 1 -> Button(onClick = {
                                expression += '8'
                            }) { Text("8") }
                            1 if colIndex == 2 -> Button(onClick = {
                                expression += '9'
                            }) { Text("9") }
                            1 if colIndex == 3 -> Button(onClick = {
                                expression += '*'
                            }) { Text("*") }
                            2 if colIndex == 0 -> Button(onClick = {
                                expression += '4'
                            }) { Text("4") }
                            2 if colIndex == 1 -> Button(onClick = {
                                expression += '5'
                            }) { Text("5") }
                            2 if colIndex == 2 -> Button(onClick = {
                                expression += '6'
                            }) { Text("6") }
                            2 if colIndex == 3 -> Button(onClick = {
                                expression += '-'
                            }) { Text("-") }
                            3 if colIndex == 0 -> Button(onClick = {
                                expression += '1'
                            }) { Text("1") }
                            3 if colIndex == 1 -> Button(onClick = {
                                expression += '2'
                            }) { Text("2") }
                            3 if colIndex == 2 -> Button(onClick = {
                                expression += '3'
                            }) { Text("3") }
                            3 if colIndex == 3 -> Button(onClick = {
                                expression += '+'
                            }) { Text("+") }
                            4 if colIndex == 0 -> Button(onClick = {
                                expression += 'e'
                            }) { Text("e") }
                            4 if colIndex == 1 -> Button(onClick = {
                                expression += '0'
                            }) { Text("0") }
                            4 if colIndex == 2 -> Button(onClick = {
                                expression += '.'
                            }) { Text(".") }
                            4 if colIndex == 3 -> Button(onClick = {
                                try {
                                    result = _calculate_result(expression)
                                    expression = result.toString()
                                }catch (e: Exception){
                                    e.message?.let { Log.e("Exception",it) }
                                }
                            }) { Text("=") }
                        }
                    }
                }
            }
        }
    }
}
fun _calculate_result(str: String): Double {
    if (str.isEmpty()) return 0.0
    var prevE = false
    var value = "0"
    val values = mutableListOf<Double>()
    val operations = mutableListOf<Char>()
    for (i in str){

        // if param i is num
        if(i.code in 48..57 || i.code == 46){
            // behind E cannot be num
            if(prevE){
                throw IllegalArgumentException("num or . cannot followed with e")
            }
            value += i
        }
        // if param i is E
        else if (i == 'e'){
            if(value != "0"){
                value = (value.toDouble() * Math.E).toString()
            }
            else{
                value = Math.E.toString()
            }
            prevE = true
        }
        // if param i is %
        else if (i == '%'){
            if(value != "0"){
                value = (value.toDouble() / 100).toString()
            }
            else{
                value = "0"
            }
        }
        // if param i is operation notion
        else{
            if(value != "0"){
                values.add(value.toDouble())
                value = "0"
            }
            operations.add(i)
            prevE = false
        }
    }
    values.add(value.toDouble())

    // do multiple or divide first
    var index = 0
    while ( index < operations.size){
        when(operations[index]){
            '*' -> {
                if(values.isEmpty()) return 0.0
                values[index] *= values[index + 1]
                values.removeAt(index + 1)
                operations.removeAt(index)
            }
            '/' -> {
                if(values.isEmpty()) return 0.0
                values[index] /= values[index + 1]
                values.removeAt(index + 1)
                operations.removeAt(index)
            }
            else -> index++
        }
    }

    // do add or minus second
    index = 0
    while (index < operations.size){
        when(operations[index]){
            '+' -> {
                if(values.isEmpty()) return 0.0
                values[index] += values[index + 1]
                values.removeAt(index + 1)
                operations.removeAt(index)
            }
            '-' -> {
                if(values.isEmpty()) return 0.0
                values[index] -= values[index + 1]
                values.removeAt(index + 1)
                operations.removeAt(index)
            }
            else -> index++
        }
    }
    return values[0]
}