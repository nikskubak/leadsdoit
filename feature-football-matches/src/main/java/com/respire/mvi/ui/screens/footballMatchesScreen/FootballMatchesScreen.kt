package com.respire.mvi.ui.screens.footballMatchesScreen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.ripple
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import android.app.DatePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.respire.mvi.R
import com.respire.mvi.domain.model.FixtureEntity
import com.respire.mvi.ui.screens.footballMatchesScreen.state.FilterType
import com.respire.mvi.ui.screens.footballMatchesScreen.state.FootballMatchesEffect
import com.respire.mvi.ui.screens.footballMatchesScreen.state.FootballMatchesEvent
import com.respire.mvi.ui.screens.footballMatchesScreen.state.FootballMatchesState
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

@Composable
fun FootballMatchesScreen(
    onItemSelected: (id: Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<FootballMatchesViewModel>()
    val state by viewModel.uiState.collectAsState()
    ObserveEffects(viewModel, onItemSelected)
    FootballMatchesUI(state, viewModel::onEvent, modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FootballMatchesUI(
    state: FootballMatchesState,
    onEvent: (event: FootballMatchesEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val refreshState = rememberPullToRefreshState()
    val context = LocalContext.current
    val selectedDate = state.selectedDate
    val readableDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val formattedDate = readableDateFormat.format(selectedDate)
    val onDateSelected = rememberUpdatedState<(Date) -> Unit> { date ->
        onEvent(FootballMatchesEvent.OnDateSelected(date))
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    ),
                title = { Text(formattedDate) },
                actions = {
                    IconButton(onClick = {
                        val selected = Calendar.getInstance().apply { time = selectedDate }
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                onDateSelected.value(Calendar.getInstance().apply {
                                    set(Calendar.YEAR, year)
                                    set(Calendar.MONTH, month)
                                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                                }.time)
                            },
                            selected.get(Calendar.YEAR),
                            selected.get(Calendar.MONTH),
                            selected.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Select date")
                    }
                }
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
                if (state.isLoading && !state.isRefreshing) {
                    CircularProgressIndicator()
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // Filter Chips
                            FilterChips(
                                selectedFilter = state.selectedFilter,
                                onFilterChanged = { filterType ->
                                    onEvent(FootballMatchesEvent.FilterChangedEvent(filterType))
                                }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            AnimatedVisibility(state.matches.isNotEmpty()) {
                                PullToRefreshBox(
                                    modifier = Modifier.fillMaxSize(),
                                    state = refreshState,
                                    isRefreshing = state.isRefreshing,
                                    onRefresh = {
                                        onEvent(FootballMatchesEvent.OnRefreshedListEvent)
                                    },
                                ) {
                                    // Matches List
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize(),
                                        // Adds 8.dp of space vertically between each item
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        // Optional: if you also want padding around the whole list
                                        contentPadding = PaddingValues(horizontal = 16.dp)
                                    ) {
                                        items(state.matches.size) { index ->
                                            MatchItem(state.matches[index], onEvent)
                                        }
                                    }
                                }
                            }

                            AnimatedVisibility(
                                state.matches.isEmpty(),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = stringResource(R.string.empty_matches),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun FilterChips(
    selectedFilter: FilterType,
    onFilterChanged: (FilterType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterType.entries.forEach { filterType ->
            FilterChip(
                onClick = {
                    onFilterChanged(filterType)
                },
                label = {
                    Text(
                        text = stringResource(filterType.nameRes),
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                selected = selectedFilter == filterType,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MatchItem(fixtureEntity: FixtureEntity, onEvent: (event: FootballMatchesEvent) -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple()
            ) {
                onEvent(FootballMatchesEvent.ClickOnItemEvent(fixtureEntity.fixture.id))
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(
            width = 1.dp,
            brush = Brush.horizontalGradient(
                colors = if (fixtureEntity.fixture.status.isLive) {
                    listOf(Color(0xFF4CAF50), Color(0xFF8BC34A)) // Green gradient for live matches
                } else {
                    listOf(
                        MaterialTheme.colorScheme.outline,
                        MaterialTheme.colorScheme.outlineVariant
                    )
                }
            )
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // League and Status Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = fixtureEntity.league.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )

                // Live indicator or match status
                if (fixtureEntity.fixture.status.isLive) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF4CAF50))
                        )
                        Text(
                            text = stringResource(R.string.live_caps),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF4CAF50),
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Text(
                        text = fixtureEntity.fixture.status.short,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Teams and Score Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home Team
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    GlideImage(
                        model = fixtureEntity.teams.home.logo,
                        contentDescription = "Home team logo",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    ) {
                        it.fitCenter()
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = fixtureEntity.teams.home.name,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2
                    )
                }

                // Score Section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    if (fixtureEntity.fixture.status.hasStarted) {
                        if (fixtureEntity.fixture.status.isLive) {
                            // Live match - show current score and time
                            Text(
                                text = "${fixtureEntity.goals.home ?: 0} - ${fixtureEntity.goals.away ?: 0}",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = fixtureEntity.fixture.status.short,
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.Bold
                            )
                            fixtureEntity.fixture.status.elapsed?.let { elapsed ->
                                Text(
                                    text = "${elapsed}'",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            // Ended match - show final score and date
                            Text(
                                text = "${fixtureEntity.goals.home ?: 0} - ${fixtureEntity.goals.away ?: 0}",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = formatMatchDate(fixtureEntity.fixture.date),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        // Match hasn't started yet - show match time
                        Text(
                            text = fixtureEntity.fixture.time,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = formatMatchDate(fixtureEntity.fixture.date),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Away Team
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    GlideImage(
                        model = fixtureEntity.teams.away.logo,
                        contentDescription = "Away team logo",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    ) {
                        it.fitCenter()
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = fixtureEntity.teams.away.name,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Venue information (if available)
            fixtureEntity.fixture.venue?.name?.let { venueName ->
                Text(
                    text = venueName,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun ObserveEffects(viewModel: FootballMatchesViewModel, onItemSelected: (id: Int) -> Unit) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect {
            when (it) {
                is FootballMatchesEffect.SelectItemEvent -> {
                    onItemSelected(it.matchId)
                }

                is FootballMatchesEffect.ErrorEffect -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

private fun formatMatchDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        date?.let { outputFormat.format(it) } ?: dateString
    } catch (e: Exception) {
        dateString
    }
}

@Preview(showBackground = true)
@Composable
fun QuizListUIPreview() {
    FootballMatchesUI(FootballMatchesState(), {}, Modifier)
}

