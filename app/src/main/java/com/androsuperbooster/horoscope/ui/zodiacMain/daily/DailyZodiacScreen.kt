package com.androsuperbooster.horoscope.ui.zodiacMain.daily

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.androsuperbooster.horoscope.R
import com.androsuperbooster.horoscope.domain.model.ZodiacEntity
import com.androsuperbooster.horoscope.ui.base.BaseScreen
import com.androsuperbooster.horoscope.ui.base.BaseUiState
import com.androsuperbooster.horoscope.ui.theme.BaseAppTheme
import com.google.firebase.inappmessaging.FirebaseInAppMessaging

@Composable
fun DailyScreen(onZodiacDetailsClick: (String) -> Unit) {
    val viewModel = hiltViewModel<DailyZodiacViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    BaseScreen(uiState) {
        val content = (uiState as? BaseUiState.ContentState<*>)?.content
        if (content is DailyZodiacSignsUi) {
            DailyScreenUI(content, onZodiacDetailsClick)
        }
    }
}

@Composable
fun DailyScreenUI(uiState: DailyZodiacSignsUi, onZodiacDetailsClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Image
                Image(
                    painter = painterResource(id = uiState.zodiacEntity.animationRes ?: 0),
                    contentDescription = "Daily Image",
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )

                // Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 24.dp)
                ) {
                    // Title
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = uiState.zodiacEntity.name.orEmpty(),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Scrollable horoscope text
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState()),
                            text = uiState.zodiacEntity.todayHoroscope,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action button
                    AnimatedVisibility(
                        visible = uiState.zodiacEntity.action != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(28.dp))
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.secondary
                                        )
                                    )
                                )
                        ) {
                            Button(
                                onClick = {
                                    onZodiacDetailsClick(uiState.zodiacEntity.action?.details.orEmpty())
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .border(
                                        width = 2.dp,
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.primary,
                                                MaterialTheme.colorScheme.secondary
                                            )
                                        ),
                                        shape = RoundedCornerShape(28.dp)
                                    ),
                                shape = RoundedCornerShape(28.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent
                                )
                            ) {
                                Text(
                                    text = uiState.zodiacEntity.action?.action.orEmpty(),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DailyScreenPreview() {
    BaseAppTheme(darkTheme = false, dynamicColor = false) {
        DailyScreenUI(
            DailyZodiacSignsUi(
                ZodiacEntity(
                    "1",
                    "name",
                    "1-1",
                    emptyList(),
                    R.drawable.ic_oven,
                    "Bugün evren senin yanında! Uzun zamandır ertelediğin bir şeyi denemek için mükemmel bir zaman. Şans tam yanında – sadece elini uzat. Cesur ol, çünkü şans cesurları sever."
                )
            ),
            {}
        )
    }
}
