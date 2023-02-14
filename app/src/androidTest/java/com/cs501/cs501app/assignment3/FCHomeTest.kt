package com.cs501.cs501app.assignment3

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.cs501.cs501app.assignment3.flashcard.FCHome
import org.junit.Rule
import org.junit.Test


class FCHomeTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<FCHome>()

    /*
    * Test if the generate button is enabled after 10 rounds
    * setup and teardowns are not necessary since createAndroidComposeRule<FCHome>() will
    * create a new instance of FCHome for each test, which is good enough for this test
     */
    @Test
    fun doTest(){

        for(i in 0..9){
            composeTestRule.onNodeWithTag("answerField").performClick()
            // type into the answerField
            composeTestRule.onNodeWithTag("answerField").performTextInput("test")
            composeTestRule.onNodeWithTag("submitBtn").performClick()
        }
        // check if generateBtn is enabled
        composeTestRule.onNodeWithTag("generateBtn").assertIsEnabled()
        composeTestRule.onNodeWithTag("generateBtn").performClick()
        // check if generateBtn is disabled
        composeTestRule.onNodeWithTag("generateBtn").assertIsNotEnabled()
        // check if round counter gets to 2
        composeTestRule.onNodeWithTag("roundCnt").assertTextContains("Round 2")
    }
}