package com.cs501.cs501app.assignment4.cintent

import androidx.lifecycle.ViewModel
import java.util.*

class CrimeListViewModel : ViewModel() {
    val crimes = mutableListOf<Crime>()
    init {
        for (i in 0 until 100) {
            val crime = Crime(
                id = UUID.randomUUID(),
                title ="Crime #$i",
                date = Date(),
                isSolved = i % 2 == 0,
                requiresPolice = i % 2 == 0
            )
            crimes += crime
        }
    }
}