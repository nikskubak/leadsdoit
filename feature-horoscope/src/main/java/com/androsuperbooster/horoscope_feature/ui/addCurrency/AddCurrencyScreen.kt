package com.androsuperbooster.horoscope_feature.ui.addCurrency

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.androsuperbooster.horoscope_feature.ui.theme.BaseAppTheme

@Composable
fun AddCurrencyScreen() {
    Text(
        text = "Hello!"
    )
}

@Preview(showBackground = true)
@Composable
fun AddCurrencyScreenPreview() {
    BaseAppTheme {
        AddCurrencyScreen()
    }
}