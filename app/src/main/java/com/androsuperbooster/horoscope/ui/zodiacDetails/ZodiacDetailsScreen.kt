package com.androsuperbooster.horoscope.ui.zodiacDetails

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.androsuperbooster.horoscope.R
import com.androsuperbooster.horoscope.ui.base.BottomSheetDialog

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZodiacDetailsScreen(onBackPressed: () -> Unit, details : String = "") {
    val viewModel : ZodiacDetailsViewModel = hiltViewModel(
        creationCallback = { factory: ZodiacDetailsViewModel.ZodiacDetailsViewModelFactory ->
            factory.create(details = details)
        }
    )
    val details by remember { mutableStateOf(viewModel.getDetails()) }
    var isLoading by remember { mutableStateOf(true) }
    var webView by remember { mutableStateOf<WebView?>(null) }
    var showExitBottomSheet by remember { mutableStateOf(false) }

    BackHandler {
        if (webView?.canGoBack() == true) {
            webView?.goBack()
        } else {
            showExitBottomSheet = true
        }
    }

    Scaffold(
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            webViewClient = object : WebViewClient() {
                                override fun onPageStarted(
                                    view: WebView?,
                                    url: String?,
                                    favicon: Bitmap?
                                ) {
                                    isLoading = true
                                }

                                override fun onPageFinished(view: WebView?, url: String?) {
                                    isLoading = false
                                }
                            }
                            settings.setJavaScriptEnabled(true)
                            settings.domStorageEnabled = true
                            settings.allowContentAccess = true
                            settings.allowFileAccess = true
                            loadUrl(viewModel.getDetails())
                            webView = this
                        }
                    },
                    update = { it.loadUrl(details) },
                    modifier = Modifier.fillMaxSize()
                )

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }

            // Show bottom sheet when needed
            if (showExitBottomSheet) {
                BottomSheetDialog(
                    title = stringResource(R.string.are_you_sure),
                    onDismiss = { showExitBottomSheet = false },
                    onConfirm = {

                    },
                    onCancel = {
                        onBackPressed()
                    },
                    confirmText = stringResource(R.string.no),  // Optional - defaults to "Confirm"
                    cancelText = stringResource(R.string.yes)  // Optional - defaults to "Cancel"
                ) {
                    // Content of your bottom sheet
                }
            }
        }
    )
}
