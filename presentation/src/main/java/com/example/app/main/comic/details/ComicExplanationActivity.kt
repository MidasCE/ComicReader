package com.example.app.main.comic.details

import android.net.http.SslError
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.example.app.R

class ComicExplanationActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_explain)

        webView = findViewById(R.id.webView)

        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(
                view: WebView,
                handler: SslErrorHandler,
                error: SslError
            ) {
                handler.proceed()
            }
        }
        webView.webChromeClient = WebChromeClient()
        webView.settings.setSupportMultipleWindows(true)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        val url = intent.getStringExtra("url");
        url?.let {
            webView.loadUrl(it)
        }
    }
}
