package com.androsuperbooster.horoscope_feature.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.androsuperbooster.horoscope_feature.domain.model.CurrencyEntity
import com.androsuperbooster.horoscope_feature.ui.base.BaseUiState
import com.androsuperbooster.horoscope_feature.ui.theme.BaseAppTheme

@Composable
fun MainScreen() {
    val viewModel = hiltViewModel<MainViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    MainScreenUi(uiState)
}

@Composable
fun MainScreenUi(uiState: BaseUiState) {
    val snackbarHostState = SnackbarHostState()
    Scaffold(
        snackbarHost = { SnackbarHost(remember { snackbarHostState }) },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                AnimatedVisibility(
                    visible = uiState is BaseUiState.LoadingState,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    CircularProgressIndicator()
                }

                AnimatedVisibility(
                    visible = uiState is BaseUiState.ContentState<*>,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    val content = (uiState as? BaseUiState.ContentState<*>)?.content
                    if (content is MainUi) {
                        CurrencyList(currencies = content.currencies)
                    }
                }

                if (uiState is BaseUiState.ErrorState) {
                    LaunchedEffect(key1 = uiState) {
                        snackbarHostState.showSnackbar(uiState.exception?.message.orEmpty())
                    }
                }
            }
        }
    )
}

@Composable
fun CurrencyList(currencies: List<CurrencyEntity>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(currencies) { item ->
            CurrencyItem(item)
        }
    }
}

@Composable
fun CurrencyItem(item: CurrencyEntity) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Column {
                Text(
                    text = item.name.orEmpty(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.symbol.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    BaseAppTheme(darkTheme = true) {
        MainScreenUi(
            BaseUiState.ContentState(
                MainUi(
                    listOf(
                        CurrencyEntity(
                            name = "name",
                            id = 1,
                            rank = 1,
                            symbol = "s",
                            slug = "s",
                            isActive = 1,
                            firstHistoricalData = "",
                            lastHistoricalData = "",
                            platform = ""
                        ),
                        CurrencyEntity(
                            name = "name2",
                            id = 1,
                            rank = 1,
                            symbol = "ss",
                            slug = "s",
                            isActive = 1,
                            firstHistoricalData = "",
                            lastHistoricalData = "",
                            platform = ""
                        )
                    )
                )
            )
        )
    }
}