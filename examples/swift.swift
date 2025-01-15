import SwiftUI
import WebKit

struct MediquoWebView: UIViewRepresentable {
    private let apiKey = "YOUR_API_KEY"
    private let token = "YOUR_TOKEN"
    private let environment = "sandbox"
    
    func makeUIView(context: Context) -> WKWebView {
        let webView = WKWebView()
        webView.navigationDelegate = context.coordinator
        
        // Add the JavaScript message handler
        webView.configuration.userContentController.add(context.coordinator, name: "nativeBridge")
        
        // Construct the URL with constants
        let urlString = "https://widget.dev.mediquo.com/integration/native.html?api_key=\(apiKey)&token=\(token)&platform=ios&environment=\(environment)"
        
        if let url = URL(string: urlString) {
            let request = URLRequest(url: url)
            webView.load(request)
        }
        
        return webView
    }
    
    func updateUIView(_ uiView: WKWebView, context: Context) {
        // No updates required for now
    }
    
    func makeCoordinator() -> Coordinator {
        Coordinator()
    }
    
    class Coordinator: NSObject, WKNavigationDelegate, WKScriptMessageHandler {
        func webView(_ webView: WKWebView, didStartProvisionalNavigation navigation: WKNavigation!) {
          // Loading callback
        }
        
        func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
          // Loaded callback
        }
        
        func webView(_ webView: WKWebView, didFail navigation: WKNavigation!, withError error: Error) {
            print("WebView failed with error: \(error.localizedDescription)")
        }
        
        // Handle JavaScript messages
        func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
            if message.name == "nativeBridge", let body = message.body as? [String: Any] {
                if let eventName = body["eventName"] as? String, let payload = body["payload"] {
                    print("Received event: \(eventName), with payload: \(payload)")
                }
            } else {
                print("Unknown message received: \(message.body)")
            }
        }
    }
}

struct ContentView: View {
    var body: some View {
        MediquoWebView()
            .edgesIgnoringSafeArea(.all)
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
