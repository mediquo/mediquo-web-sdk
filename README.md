<p align="center">
  <img height="128px" src="https://marqueting.s3.eu-central-1.amazonaws.com/assets/logo_rounded.png" title="logo mediQuo">
</p>

<h1 align="center">MediQuo Web SDK</h1>

Welcome to MediQuo SDK for web, the easiest way to integrate the MediQuo functionality into your app!

---

## Overview

The MediQuo Web SDK provides a seamless way to integrate our features into your app through a WebView. This SDK supports [list the supported platforms or technologies, e.g., React, Angular, vanilla JavaScript].

## Setting up

We currently provide an SDK for Flutter based applications. In case of native applications, you can get it working by using the WebView API supported on your platform. To get started, follow the instructions accordingly.

## Flutter applications

1. Install the package:

```bash
flutter pub add mediquo_flutter_sdk
```

2. Import and use the `MediquoWidget` widget in your app:

```dart
import 'package:mediquo_flutter_sdk/mediquo_flutter_sdk.dart';

// ...

return const MediquoWidget(
  apiKey: "YOUR_API_KEY",
  token: "YOUR_TOKEN",

  // 'sandbox' or 'production'
  environment: MediquoWidgetEnvironment.sandbox,

  // callback when the user downloads a file, you will receive the file url
  onDownload: onDownloadListener,

  // callback when the user clicks a content link, you will receive the url
  onLoadUrl: onLoadUrl,

  // callback when the user requests microphone permissions
  onMicrophonePermission: onMicrophonePermission,

  // callback when the user requests camera permissions
  onCameraPermission: onCameraPermission,

  // theme for the widget, you can customize the colors
  theme:
      const MediquoWidgetTheme(containerColor: Colors.purple),
);
```

## Non-Flutter applications

Open the following URL in a WebView:

`https://widget.dev.mediquo.com/integration/native.html?api_key=YOUR_API_KEY&token=YOUR_TOKEN&platform=TARGET_PLATFORM&environment=ENVIRONMENT`

Replace the following placeholders:

| Placeholder       | Description                                                                              |
| ----------------- | ---------------------------------------------------------------------------------------- |
| `YOUR_API_KEY`    | Your MediQuo API key.                                                                    |
| `YOUR_TOKEN`      | Your MediQuo token.                                                                      |
| `TARGET_PLATFORM` | The platform where you are integrating the SDK. Possible values: `ios`, `android`.       |
| `ENVIRONMENT`     | The environment where you want to run the SDK. Possible values: `sandbox`, `production`. |

### Events

Events will be sent through a native bridge to the WebView according to the platform.

For iOS apps, the WebView implements the `window.webkit` interface and the events are sent like this:

```javascript
window.webkit.messageHandlers.nativeBridge.postMessage({
  eventName,
  payload,
});
```

For Android apps, the WebView exports the `window.NativeBridge` object and the events are sent like this:

```javascript
window.NativeBridge.postMessage(
  JSON.stringify({
    eventName,
    payload,
  })
);
```

| Event Name                | Description                            |
| ------------------------- | -------------------------------------- |
| `mediquo_native_close`    | A close action was made by the user    |
| `mediquo_native_download` | A download action was made by the user |

### Permissions

In case the user requests for permissions (like microphone or camera), it is possible to handle it on the WebView side. Refer to your platform documentation to understand how to handle request permissions on a WebView.
