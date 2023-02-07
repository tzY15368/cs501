package com.cs501.cs501app.assignment2

import org.junit.Assert.*

import org.junit.Test

class calFuncTest {

    @Test
    fun evalExprPlus() {
        assertEquals("5", calFunc.evalExpr("2+3"))
    }

    @Test
    fun evalExprSub() {
        assertEquals("1", calFunc.evalExpr("3-2"))
    }

    @Test
    fun evalExprMulti() {
        assertEquals("6", calFunc.evalExpr("2*3"))
    }

    @Test
    fun evalExprDiv() {
        assertEquals("1.500000", calFunc.evalExpr("3/2"))
    }
}