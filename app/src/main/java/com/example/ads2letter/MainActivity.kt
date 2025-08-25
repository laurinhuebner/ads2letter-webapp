package com.example.ads2letter

import android.app.Activity
import android.content.Intent
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var selectPdfButton: Button
    private lateinit var pdfPreview: ImageView
    private lateinit var resultText: TextView

    private val PICK_PDF_FILE = 1001

    // OkHttp mit Logging
    private val client: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectPdfButton = findViewById(R.id.selectPdfButton)
        pdfPreview = findViewById(R.id.pdfPreview)
        resultText = findViewById(R.id.resultText)

        selectPdfButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/pdf"
            }
            startActivityForResult(intent, PICK_PDF_FILE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_PDF_FILE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                renderPdf(uri)
                uploadPdf(uri)
            }
        }
    }

    private fun renderPdf(uri: Uri) {
        lifecycleScope.launch(Dispatchers.IO) {
            val file = copyToCache(uri)
            val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = PdfRenderer(fileDescriptor)
            val page = renderer.openPage(0)

            val bitmap = android.graphics.Bitmap.createBitmap(
                page.width, page.height,
                android.graphics.Bitmap.Config.ARGB_8888
            )
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            page.close()
            renderer.close()

            withContext(Dispatchers.Main) {
                pdfPreview.setImageBitmap(bitmap)
            }
        }
    }

    private fun uploadPdf(uri: Uri) {
        lifecycleScope.launch(Dispatchers.IO) {
            val file = copyToCache(uri)

package com.example.webviewapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
  @SuppressLint("SetJavaScriptEnabled")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    val webView = WebView(this)
    setContentView(webView)

    webView.settings.javaScriptEnabled = true
    webView.webViewClient = object : WebViewClient() {
      override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return false
      }
    }
    webView.loadUrl("http://192.168.1.21:5000")
  }
}
