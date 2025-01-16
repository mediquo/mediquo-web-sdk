<p align="center">
  <img height="128px" src="https://marqueting.s3.eu-central-1.amazonaws.com/assets/logo_rounded.png" title="logo mediQuo">
</p>

<h1 align="center">MediQuo Web SDK</h1>

Welcome to MediQuo SDK for web, the easiest way to integrate the MediQuo functionality into your app!

---

## Overview

Our Widget provides a seamless way to integrate our features into your app, allowing to embed the chat and video call functionalities in a simple and straightforward way. The SDK is available for Flutter applications and can be used in native applications as well by embedding a WebView.

## Web apps

Copy the snippet below and replace the placeholders with your own configuration:

Within the `<head>` tag:

```html
<script
  type="text/javascript"
  src="https://widget.mediquo.com/js/1.0.0/mediquo.js"
></script>
```

Then, `MediquoWidget` will be available in the global scope. Use it like this:

```html
<script>
  window.onload = () => {
    MediquoWidget.init({
      apiKey: "YOUR_API_KEY",
      accessToken: "YOUR_TOKEN",
      // Make sure you specify adapter as "web"
      adapter: "web",
      theme: {
        launcher: "base",
        position: "right",
        colors: {
          primary: "#a91e90",
          primaryContrast: "#ffffff",
          secondary: "#3c50ec",
          accent: "#42cece",
          messageTextOutgoing: "#201552",
          messageTextIncoming: "#201552",
          bubbleBackgroundOutgoing: "#eceff1",
          bubbleBackgroundIncoming: "#e7e3f1",
          alertText: "#201552",
          alertBackground: "#e7e3f1",
        },
      },
      immediateVideoCall: true,
      mute: true,
      // "production" -> for production usage
      environment: "sandbox",
    }).then(({ hasActiveSession, pendingConsultations }) => {
      console.log("Launched successfully", {
        hasActiveSession,
        pendingConsultations,
      });
    });
  };
</script>
```

For web apps no events nor permissions are sent as everything is handled directly in the browser.

For more information about the configuration options, refer to the [Widget documentation page](https://developer.mediquo.com/docs/sdk/widget/introduction/).

## Native apps

Open the following URL in a WebView:

`https://widget.mediquo.com/integration/native.html?api_key=YOUR_API_KEY&token=YOUR_TOKEN&platform=TARGET_PLATFORM&environment=ENVIRONMENT`

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

> [!IMPORTANT]  
> In case the user requests for permissions (like microphone or camera), it is possible to handle it on the WebView side. Refer to your platform documentation to understand how to handle request permissions on a WebView.

> [!TIP]
> Remember if you are whitelisting the valid domains in your app, allow the following wildcard domains:
>
> - `*.tokbox.com`
> - `*.opentok.com`
> - `*.mediquo.com`

## Flutter apps

We provide a flutter SDK that you can use to integrate the MediQuo widget into your app so you don't have to worry about the WebView implementation. Refer to the [Flutter SDK documentation](https://github.com/mediquo/mediquo-flutter-sdk) for more information.
