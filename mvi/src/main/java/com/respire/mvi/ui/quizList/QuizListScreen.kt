package com.respire.mvi.ui.quizList

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.respire.mvi.R
import com.respire.mvi.ui.quizList.state.QuizListEffect
import com.respire.mvi.ui.quizList.state.QuizListEvent
import com.respire.mvi.ui.quizList.state.QuizListState

@Composable
fun QuizListScreen(onQuizSelected: (quizId: String) -> Unit) {
    val viewModel = hiltViewModel<QuizListViewModel>()
    val state by viewModel.uiState.collectAsState()
    observeEffects(viewModel, onQuizSelected)
    LaunchedEffect(Unit) {
        viewModel.onEvent(QuizListEvent.LoadListEvent)
    }
    QuizListUI(state, viewModel::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizListUI(state: QuizListState, onEvent: (event: QuizListEvent) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    ),
                title = { Text(stringResource(R.string.select_quiz_title)) },
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.padding(padding))
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(padding),
                        // Adds 8.dp of space vertically between each item
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        // Optional: if you also want padding around the whole list
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(state.quizzes.size) { index ->
                            val gradientColors =
                                listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary
                                )
                            val interactionSource = remember { MutableInteractionSource() }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = ripple(),
                                        onClick = { onEvent(QuizListEvent.ClickOnItemEvent(state.quizzes[index].id)) }
                                    ),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(
                                    width = 2.dp,
                                    brush = Brush.horizontalGradient(colors = gradientColors)
                                ),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Row {
                                    Text(
                                        modifier = Modifier.padding(16.dp),
                                        text = state.quizzes[index].title
                                    )
                                }
                            }
                        }
                    }
                }
                state.error?.let {
                    Toast.makeText(LocalContext.current, it, Toast.LENGTH_LONG).show()
                }
            }
        }
    )
}

@Composable
fun observeEffects(viewModel: QuizListViewModel, onQuizSelected: (quizId: String) -> Unit) {
    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect {
            when (it) {
                is QuizListEffect.SelectItemEvent -> {
                    onQuizSelected(it.quizId)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizListUIPreview() {
    QuizListUI(QuizListState(), {})
}

