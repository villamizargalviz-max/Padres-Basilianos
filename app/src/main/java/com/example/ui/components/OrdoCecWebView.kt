package com.example.ui.components

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.webkit.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.model.AppLanguage
import com.example.ui.i18n.AppStrings

const val CEC_ORDO_URL = "https://web-ordo-colombiano.cec.org.co/liturgia-horas"
const val CEC_ORDO_INICIO_URL = "https://web-ordo-colombiano.cec.org.co/inicio"

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun OrdoCecWebView(
    modifier: Modifier = Modifier,
    initialUrl: String = CEC_ORDO_URL,
    displayTitle: String = "Ordo Colombiano Oficial • Conferencia Episcopal de Colombia",
    appLanguage: AppLanguage = AppLanguage.ES,
    onSwitchToNativeMode: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val strings = remember(appLanguage) { AppStrings.get(appLanguage) }
    var currentUrl by remember(initialUrl) { mutableStateOf(initialUrl) }
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var loadingProgress by remember { mutableStateOf(0) }
    var hasError by remember { mutableStateOf(false) }
    var reloadTrigger by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("ordo_cec_webview_container")
    ) {
        // Embedded Top Controls Bar (Completely inside the app)
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f),
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "🇨🇴", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = displayTitle,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1
                            )
                            Text(
                                text = currentUrl.removePrefix("https://"),
                                fontSize = 9.5.sp,
                                color = MaterialTheme.colorScheme.primary,
                                maxLines = 1
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Switch to Native Mode if available
                        if (onSwitchToNativeMode != null) {
                            FilledTonalButton(
                                onClick = onSwitchToNativeMode,
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text(
                                    text = strings.liturgia.tabNativeOffline,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Back in history inside the web view
                        IconButton(
                            onClick = {
                                if (webViewInstance?.canGoBack() == true) {
                                    webViewInstance?.goBack()
                                }
                            },
                            enabled = webViewInstance?.canGoBack() == true,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Página anterior",
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        // Reload page
                        IconButton(
                            onClick = {
                                hasError = false
                                isLoading = true
                                reloadTrigger++
                                webViewInstance?.reload()
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = strings.liturgia.reloadWeb,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        // Open in external browser
                        IconButton(
                            onClick = {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentUrl))
                                    context.startActivity(intent)
                                } catch (_: Exception) {}
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.OpenInBrowser,
                                contentDescription = strings.liturgia.openExternalBrowser,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                // Sub-Bar: Quick Switcher between CEC Inicio and Liturgia de las Horas
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val inicioLabel = when (appLanguage) {
                        AppLanguage.EN -> "CEC Ordo Home"
                        AppLanguage.FR -> "Accueil Ordo CEC"
                        AppLanguage.ES -> "Ordo Inicio (CEC)"
                    }
                    val liturgiaLabel = when (appLanguage) {
                        AppLanguage.EN -> "Liturgy of the Hours"
                        AppLanguage.FR -> "Liturgie des Heures"
                        AppLanguage.ES -> "Liturgia de las Horas"
                    }
                    FilterChip(
                        selected = currentUrl == CEC_ORDO_INICIO_URL,
                        onClick = {
                            currentUrl = CEC_ORDO_INICIO_URL
                            hasError = false
                            isLoading = true
                            webViewInstance?.loadUrl(CEC_ORDO_INICIO_URL)
                        },
                        label = { Text(inicioLabel, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        modifier = Modifier.height(28.dp)
                    )
                    FilterChip(
                        selected = currentUrl == CEC_ORDO_URL,
                        onClick = {
                            currentUrl = CEC_ORDO_URL
                            hasError = false
                            isLoading = true
                            webViewInstance?.loadUrl(CEC_ORDO_URL)
                        },
                        label = { Text(liturgiaLabel, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        modifier = Modifier.height(28.dp)
                    )
                }
            }
        }

        // Integrated Loading Progress indicator
        if (isLoading) {
            LinearProgressIndicator(
                progress = { loadingProgress / 100f },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (hasError) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudSync,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = displayTitle,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = strings.liturgia.errorNotice,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (onSwitchToNativeMode != null) {
                                Button(
                                    onClick = onSwitchToNativeMode,
                                    modifier = Modifier.fillMaxWidth(0.9f)
                                ) {
                                    Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(strings.liturgia.switchToNativeBtn, fontWeight = FontWeight.Bold)
                                }
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                FilledTonalButton(
                                    onClick = {
                                        hasError = false
                                        isLoading = true
                                        reloadTrigger++
                                    }
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(strings.common.retry)
                                }

                                OutlinedButton(
                                    onClick = {
                                        try {
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentUrl))
                                            context.startActivity(intent)
                                        } catch (_: Exception) {}
                                    }
                                ) {
                                    Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(strings.liturgia.openExternalBrowser)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            key(reloadTrigger) {
                AndroidView(
                    factory = { ctx ->
                        WebView(ctx).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )

                            // Use SOFTWARE layer for emulators/headless virtualization to prevent MESA rendernode crashes
                            try {
                                setLayerType(android.view.View.LAYER_TYPE_SOFTWARE, null)
                            } catch (_: Exception) {}

                            // Optimized settings for stability
                            settings.apply {
                                javaScriptEnabled = true
                                domStorageEnabled = true
                                databaseEnabled = false
                                useWideViewPort = true
                                loadWithOverviewMode = true
                                builtInZoomControls = false
                                displayZoomControls = false
                                setSupportZoom(false)
                                mediaPlaybackRequiresUserGesture = true
                                cacheMode = WebSettings.LOAD_DEFAULT
                                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                            }

                            webViewClient = object : WebViewClient() {
                                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                    super.onPageStarted(view, url, favicon)
                                    isLoading = true
                                }

                                override fun onPageFinished(view: WebView?, url: String?) {
                                    super.onPageFinished(view, url)
                                    isLoading = false
                                    if (url != null) {
                                        currentUrl = url
                                    }
                                }

                                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                                    val destinationUrl = request?.url?.toString() ?: ""
                                    if (destinationUrl.isNotBlank()) {
                                        if (destinationUrl.startsWith("http://") || destinationUrl.startsWith("https://")) {
                                            view?.loadUrl(destinationUrl)
                                        } else {
                                            try {
                                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(destinationUrl))
                                                ctx.startActivity(intent)
                                            } catch (_: Exception) {}
                                        }
                                    }
                                    return true
                                }

                                override fun onReceivedError(
                                    view: WebView?,
                                    request: WebResourceRequest?,
                                    error: WebResourceError?
                                ) {
                                    super.onReceivedError(view, request, error)
                                    if (request?.isForMainFrame == true) {
                                        Log.w("OrdoCecWebView", "Main frame load error: ${error?.description}")
                                        hasError = true
                                        isLoading = false
                                    }
                                }

                                override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {
                                    Log.w("OrdoCecWebView", "Chromium render process terminated. Handled gracefully.")
                                    try {
                                        (view?.parent as? ViewGroup)?.removeView(view)
                                        view?.destroy()
                                    } catch (_: Exception) {}
                                    webViewInstance = null
                                    hasError = true
                                    isLoading = false
                                    onSwitchToNativeMode?.invoke()
                                    return true
                                }
                            }

                            webChromeClient = object : WebChromeClient() {
                                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                    loadingProgress = newProgress
                                    if (newProgress >= 100) {
                                        isLoading = false
                                    }
                                }
                            }

                            loadUrl(currentUrl)
                            webViewInstance = this
                        }
                    },
                    update = { webView ->
                        webViewInstance = webView
                    },
                    onRelease = { webView ->
                        try {
                            webView.stopLoading()
                            webView.onPause()
                            webView.removeAllViews()
                            webView.destroy()
                        } catch (_: Exception) {}
                        webViewInstance = null
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("ordo_cec_webview")
                )
            }
        }
    }
}
