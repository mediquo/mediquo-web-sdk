import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.json.JSONObject

class MediquoWebViewActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private val apiKey = "YOUR_API_KEY"
    private val token = "YOUR_TOKEN"
    private val environment = "sandbox"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mediquo_webview)

        webView = findViewById(R.id.webView)
        setupWebView()
    }

    private fun setupWebView() {
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
        }

        // Set WebViewClient to handle navigation within WebView
        webView.webViewClient = WebViewClient()

        // Set WebChromeClient to handle permissions
        webView.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest?) {
                request?.resources?.forEach { resource ->
                    when (resource) {
                        PermissionRequest.RESOURCE_VIDEO_CAPTURE -> {
                            handlePermissionRequest(
                                Manifest.permission.CAMERA,
                                request
                            )
                        }
                        PermissionRequest.RESOURCE_AUDIO_CAPTURE -> {
                            handlePermissionRequest(
                                Manifest.permission.RECORD_AUDIO,
                                request
                            )
                        }
                        else -> {
                            request?.deny()
                        }
                    }
                }
            }
        }

        // Add the JavaScript interface for event communication
        webView.addJavascriptInterface(NativeBridge(), "NativeBridge")

        // Construct the URL with constants
        val url =
            "https://widget.dev.mediquo.com/integration/native.html?api_key=$apiKey&token=$token&platform=android&environment=$environment"
        webView.loadUrl(url)
    }

    private fun handlePermissionRequest(permission: String, request: PermissionRequest) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            request.grant(arrayOf(permission))
        } else {
            val permissionLauncher =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                    if (isGranted) {
                        request.grant(arrayOf(permission))
                    } else {
                        request.deny()
                    }
                }
            permissionLauncher.launch(permission)
        }
    }

    // JavaScript interface for handling events
    inner class NativeBridge {
        @JavascriptInterface
        fun postMessage(eventJson: String) {
            try {
                val event = JSONObject(eventJson)
                val eventName = event.getString("eventName")
                val payload = event.getJSONObject("payload")
                
                // Handle the event
                handleEvent(eventName, payload)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun handleEvent(eventName: String, payload: JSONObject) {
        when (eventName) {
            "mediquo_native_close" -> {
                // Handle close action
                Toast.makeText(this, "User closed the view", Toast.LENGTH_SHORT).show()
                finish() // Close the activity if applicable
            }
            "mediquo_native_download" -> {
                // Handle download action
                val fileUrl = payload.optString("url", "Unknown")
                Toast.makeText(this, "Download requested: $fileUrl", Toast.LENGTH_SHORT).show()
                // Add your download logic here
            }
            else -> {
                Toast.makeText(this, "Unhandled event: $eventName", Toast.LENGTH_SHORT).show()
            }
        }
    }
}