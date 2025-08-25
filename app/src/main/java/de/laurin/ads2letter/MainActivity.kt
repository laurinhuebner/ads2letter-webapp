package de.laurin.ads2letter

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView = findViewById<WebView>(R.id.webView)
        with(webView.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
            mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        }

        webView.webViewClient = WebViewClient() // hält Navigation in der App

        // >>> HIER deine LAN-Adresse <<<
        webView.loadUrl("http://192.168.1.21:5000/")
    }

    override fun onBackPressed() {
        val wv = findViewById<WebView>(R.id.webView)
        if (wv.canGoBack()) wv.goBack() else super.onBackPressed()
    }
}
