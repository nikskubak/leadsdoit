package com.respire.baseapp.ui.addCurrency

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.respire.baseapp.ui.theme.BaseAppTheme

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