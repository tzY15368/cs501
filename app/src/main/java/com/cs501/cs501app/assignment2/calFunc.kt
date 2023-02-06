package com.cs501.cs501app.assignment2

import android.util.Log
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.math.sqrt

object calFunc {

    // the only interface for doing calculations, caller should catch exceptions
    fun evalExpr(expr: String): String {
        Log.d("CalFunc", "Evaluating expr=$expr")
        return calc(cal(expr))
    }
    private fun isOperator(s : String) : Boolean {
        return s == "+" || s == "-" || s == "*" || s == "/"
    }

    private fun countPoint(s : String) : Boolean {
        var counter = 0
        for(c in s) {
            if(c=='.')
                counter++
        }

        return counter <= 1
    }

    private fun endWithPoint(s : String) : Boolean {

        return s[s.length-1] == '.'
    }
    private fun cal(s: String): Stack<String> {
        val stacka = Stack<String>()
        val stackb = Stack<String>()
        var temp = String()
        val hashMap = HashMap<String, Int>()
        hashMap.put("+", 0);
        hashMap.put("-", 0);
        hashMap.put("*", 1);
        hashMap.put("/", 1);
        hashMap.put("√", 2);
        for (i in 0 until s.length) {
            val c = s[i]
            val m = c + ""
            if (Character.isDigit(c) || c == '.') {
                if (i == s.length - 1) {
                    temp += m
//                    digitCount++
                    println(temp)
                    stacka.push(temp)
                    temp = ""
                } else
                    temp += m
            } else {
                stacka.push(temp)
                temp = ""
                when (c) {
                    '+', '-' -> if (!stackb.isEmpty() && stackb.peek() != "+" && stackb.peek() != "-") {
                        val t = stackb.pop()
                        stacka.push(t)
                        stackb.push(m)
//                        operateCount++
                    } else {
                        stackb.push(m)
//                        operateCount++
                    }
                    '*', '/' -> {
                        stackb.push(m)
//                        operateCount++
                    }
                    '√' -> {
                        stackb.push(m)
//                        operateCount++
                    }
                    '%' -> {
                        stackb.push(m)
                    }
                }
            }
        }
        while (!stackb.isEmpty()) {
            val q = stackb.pop()
            stacka.push(q)
        }


        return stacka
    }

    private fun calc(stacka: Stack<String>)
            : String {
        var digitCount = 0
        var operateCount = 0

        val arr = ArrayList<String>()
        while (!stacka.isEmpty()) {
            val t = stacka.pop()
            Log.e("1",t)
            arr.add(t)

            try {
                assert(countPoint(t) && !endWithPoint(t))
            } catch (e: AssertionError) {
                throw Exception("Decimal point invalid!")
            }

            if(isOperator(t))
                operateCount++
            else digitCount++
        }

        val arr1 = ArrayList<String>()

        try {
            assert(operateCount < digitCount)
        } catch (e: AssertionError) {
            Log.d("CalFunc", "operateCount=$operateCount, digitCount=$digitCount")
            throw Exception("Invalid input!")
        }

        for (i in arr.indices.reversed()) {
            val j = arr1.size
            when (arr[i]) {
                "√" -> {
                    val decimal = BigDecimal(arr1.removeAt(j - 1))
                    val a = sqrt(decimal.toDouble())
                    arr1.add(a.toString())
                }
                "+" -> {
                    val a =
                        BigDecimal(arr1.removeAt(j - 2)).add(BigDecimal(arr1.removeAt(j - 2)))
                    arr1.add(a.toString())
                }
                "-" -> {
                    val b =
                        BigDecimal(arr1.removeAt(j - 2)).subtract(BigDecimal(arr1.removeAt(j - 2)))
                    arr1.add(b.toString())
                }
                "*" -> {
                    val c =
                        BigDecimal(arr1.removeAt(j - 2)).multiply(BigDecimal(arr1.removeAt(j - 2)))
                    arr1.add(c.toString())
                }
                "/" -> {
                    val d1 = BigDecimal(arr1.removeAt(j - 2))
                    val d2 = BigDecimal(arr1.removeAt(j - 2))

                    try {
                        assert(d2 != BigDecimal(0))
                    } catch (e: AssertionError) {
                        throw Exception("Cannot divide by zero")
                    }

                    val d = d1.divide(d2, 6, RoundingMode.DOWN)
                    arr1.add(d.toString())
                }
                "%" -> {
                    val d1 = arr1.removeAt(j - 2).toFloatOrNull()
                    val d2 = arr1.removeAt(j - 2).toFloatOrNull()
                    try {
                        assert(d1 != null && d2 != null)
                    } catch (e: AssertionError) {
                        throw Exception("Cannot mod between other data type")
                    }
                    try {
                        assert(d2 != 0f)
                    } catch (e: AssertionError) {
                        throw Exception("Cannot divide by zero")
                    }

                    val d = d2?.let { d1?.mod(it) ?: 0 }
                    arr1.add(d.toString())
                }
                else -> arr1.add(arr[i])
            }
        }
        // If the result is an integer, remove the decimal point
        if (arr1[0].contains(".")) {
            val decimal = BigDecimal(arr1[0])
            val a = decimal.setScale(0, RoundingMode.DOWN)
            if (a.toDouble() == decimal.toDouble())
                arr1[0] = a.toString()
        }
        return arr1[0]
    }
}