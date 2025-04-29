package com.androsuperbooster.horoscope.ui.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun BaseScreen(uiState: BaseUiState, content: @Composable (uiState: BaseUiState) -> Unit = {}) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is BaseUiState.LoadingState -> {
                CircularProgressIndicator()
            }

            is BaseUiState.ContentState<*> -> {
                content(uiState)
            }

            else -> {}
        }

//        AnimatedVisibility(
//            visible = uiState is BaseUiState.LoadingState,
//            enter = fadeIn(),
//            exit = fadeOut()
//        ) {
//            CircularProgressIndicator()
//        }
//
//        AnimatedVisibility(
//            visible = uiState is BaseUiState.ContentState<*>,
//            enter = fadeIn(),
//            exit = fadeOut()
//        ) {
//            content(uiState)
//        }
    }
}