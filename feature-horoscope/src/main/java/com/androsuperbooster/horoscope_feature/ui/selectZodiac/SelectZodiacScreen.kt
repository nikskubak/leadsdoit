package com.androsuperbooster.horoscope_feature.ui.selectZodiac

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.androsuperbooster.horoscope_feature.R
import com.androsuperbooster.horoscope_feature.domain.model.ZodiacEntity
import com.androsuperbooster.horoscope_feature.ui.base.BaseScreen
import com.androsuperbooster.horoscope_feature.ui.base.BaseUiState
import com.androsuperbooster.horoscope_feature.ui.theme.BaseAppTheme
import com.androsuperbooster.horoscope_feature.ui.theme.Primary
import com.androsuperbooster.horoscope_feature.ui.theme.Secondary

@Composable
fun SelectZodiacScreen(onZodiacSelected: (ZodiacEntity) -> Unit) {
    val viewModel = hiltViewModel<SelectZodiacViewModel>()
    viewModel.onZodiacSelected = onZodiacSelected
    val uiState by viewModel.uiState.collectAsState()
    BaseScreen(uiState) {
        val content = (uiState as? BaseUiState.ContentState<*>)?.content
        if (content is SelectZodiacSignsUi) {
            ZodiacSignsScreenUi(content, { sign -> viewModel.selectSign(sign) })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZodiacSignsScreenUi(content: SelectZodiacSignsUi, onSignSelected: (ZodiacEntity) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(containerColor = MaterialTheme.colorScheme.surface, titleContentColor = MaterialTheme.colorScheme.onPrimary),
                title = { Text(stringResource(R.string.select_sign)) }, // Replace with your desired title
            )
        },
        content = { padding ->
            zodiacList(content, padding, onSignSelected)
        }
    )
}

@Composable
private fun zodiacList(
    content: SelectZodiacSignsUi,
    padding: PaddingValues,
    onItemClick: (ZodiacEntity) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.padding(padding),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(content.signs.size) { index ->
            ItemCard(content.signs[index], onItemClick)
        }
    }
}


@Composable
fun ItemCard(item: ZodiacEntity, onItemClick: (ZodiacEntity) -> Unit) {
    val gradientColors = listOf(Primary, Secondary) // Example gradient colors
    val interactionSource = remember { MutableInteractionSource() }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(),
                onClick = { onItemClick(item) }
            ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 2.dp,
            brush = Brush.horizontalGradient(colors = gradientColors)
        ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = item.animationRes ?: 0),
                contentDescription = null, // Add content description for accessibility
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.name.orEmpty(),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.birthPeriod.orEmpty(),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary,
                minLines = 2
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    BaseAppTheme(darkTheme = true) {

    }
}