package com.respire.mvi.ui.screens.privacyScreen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.respire.mvi.R
import android.os.Bundle
import androidx.compose.runtime.DisposableEffect

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PrivacyScreen(onBackPressed: () -> Unit, details : String = "") {
    val viewModel : PrivacyViewModel = hiltViewModel(
        creationCallback = { factory: PrivacyViewModel.PrivacyViewModelFactory ->
            factory.create(details = details)
        }
    )
    var isLoading by rememberSaveable { mutableStateOf(true) }
    var webView by remember { mutableStateOf<WebView?>(null) }
    var showExitBottomSheet by rememberSaveable { mutableStateOf(false) }

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
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.allowContentAccess = true
                            settings.allowFileAccess = true
                            webView = this
                            // Restore state if available, else load url
                            if (viewModel.webViewBundle != null) {
                                restoreState(viewModel.webViewBundle!!)
                            } else {
                                loadUrl(viewModel.getDetails())
                            }
                        }
                    },
                    update = { view ->
                        webView = view
                    },
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

    // Save state on dispose
    DisposableEffect(Unit) {
        onDispose {
            webView?.let { wv ->
                val bundle = Bundle()
                wv.saveState(bundle)
                viewModel.webViewBundle = bundle
            }
        }
    }
}
