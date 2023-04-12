package com.cs501.cs501app.buotg.view.user_setting

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FoldViewModel(initVal: List<Boolean>) : ViewModel() {
    private val showList = mutableListOf<MutableStateFlow<Boolean>>()

    init {
        // range over initVal and add into showlist
        for (b: Boolean in initVal) {
            showList.add(MutableStateFlow(b))
        }
    }

    val showListFlow: List<StateFlow<Boolean>> get() = showList

    fun toggleFold(index: Int) {
        showList[index].value = !showList[index].value
    }

    fun foldAll(){
        for (b in showList){
            b.value = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoldableListItem(
    itemIdx: Int,
    viewModel: FoldViewModel,
    foldState: State<Boolean>,
    modifier: Modifier = Modifier,
    headlineText: @Composable () -> Unit = {},
    supportingText: @Composable () -> Unit = {},
    foldedContent: @Composable () -> Unit = {},
    leadingContent: @Composable () -> Unit = {},
) {
    Column(
        modifier = modifier
    ) {
        ListItem(
            headlineText = headlineText,
            supportingText = supportingText,
            trailingContent = {
                Box(
                    modifier = Modifier
                        .clickable {
                            // if we're un-folding this one, fold everyone first
                            if (!foldState.value) {
                                viewModel.foldAll()
                            }
                            viewModel.toggleFold(itemIdx)
                        }
                ) {
                    Icon(
                        imageVector = if (foldState.value) {
                            Icons.Default.KeyboardArrowDown
                        } else {
                            Icons.Default.KeyboardArrowUp
                        },
                        contentDescription = "Expand",
                        modifier = Modifier
                            .size(36.dp)
                            .padding(8.dp)
                    )
                }
            },
            leadingContent = leadingContent,
        )
        if (foldState.value) {
            foldedContent()
        }
    }
}