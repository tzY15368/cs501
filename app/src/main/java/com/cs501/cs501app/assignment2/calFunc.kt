package com.cs501.cs501app.assignment2

import android.view.View
import com.cs501.cs501app.utils.Alert
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.math.sqrt

object calFunc {
    private var digitCount = 0
    private var operateCount = 0
    fun cal(s: String): Stack<String> {
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
                    digitCount++
                    stacka.push(temp)
                } else
                    temp += m
            }
            else {
                when (c) {
                    '+', '-' -> if (!stackb.isEmpty() && stackb.peek()!="+"&& stackb.peek()!="-") {
                        val t = stackb.pop()
                        stacka.push(t)
                        stackb.push(m)
                        operateCount++
                    } else {
                        stackb.push(m)
                        operateCount++
                    }
                    '*', '/' -> {
                        stackb.push(m)
                        operateCount++
                    }
                    '√' -> {
                        stackb.push(m)
                        operateCount++
                    }
                }
            }
        }
        while (!stackb.isEmpty()) {
            val q = stackb.pop()
            stacka.push(q)
        }

        try {
            assert(operateCount < digitCount)
        } catch (e : AssertionError) {
            throw Exception("Invalid input!")
        }

        return stacka
    }

    fun calc(stacka: Stack<String>)
            : String {
        val arr = ArrayList<String>()
        while (!stacka.isEmpty()) {
            val t = stacka.pop()
            arr.add(t)
        }
        val arr1 = ArrayList<String>()
        for (i in arr.indices.reversed()) {
            val j = arr1.size
            when (arr[i]) {
                "√" -> {
                    val decimal = BigDecimal(arr1.removeAt(j - 2))
                    val a = sqrt(decimal.toDouble())
                    arr1.add(a.toString())
                }
                "+" -> {
                    val a = BigDecimal(arr1.removeAt(j - 2)).add(BigDecimal(arr1.removeAt(j - 2)))
                    arr1.add(a.toString())
                }
                "-" -> {
                    val b = BigDecimal(arr1.removeAt(j - 2)).subtract(BigDecimal(arr1.removeAt(j - 2)))
                    arr1.add(b.toString())
                }
                "*" -> {
                    val c = BigDecimal(arr1.removeAt(j - 2)).multiply(BigDecimal(arr1.removeAt(j - 2)))
                    arr1.add(c.toString())
                }
                "/" -> {
                    val d1 = BigDecimal(arr1.removeAt(j - 2))
                    val d2 = BigDecimal(arr1.removeAt(j - 2))

                    try {
                        assert(d2 != BigDecimal(0))
                    } catch (e : AssertionError) {
                        throw Exception("Cannot divide by zero")
                    }

                    val d = d1.divide(d2,6,RoundingMode.DOWN)
                    arr1.add(d.toString())
                }

                else -> arr1.add(arr[i])
            }
        }
        return arr1[0]
    }
}