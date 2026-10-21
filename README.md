# QR Generator 171

A lightweight browser-based QR Code generator.

## Features
- Generate a QR Code from text or a URL
- Preview the QR Code in the page
- Download as PNG at 200, 300, 500, or 800 pixels
- Choose QR error-correction level
- Copy source text and clear the form
- Responsive dark navy / cyan interface

## Run locally
Open `index.html` in a modern browser with an internet connection. The QR library is loaded from cdnjs.

## Publish with GitHub Pages
1. Open the repository **Settings**.
2. Select **Pages** in the sidebar.
3. Under **Build and deployment**, select **Deploy from a branch**.
4. Select branch `main` and folder `/ (root)`, then save.
5. Wait for GitHub Pages to publish the site.

## Test checklist
- Generate a QR for a short word.
- Generate a QR for an HTTPS URL.
- Try Indonesian characters and punctuation.
- Change image size and correction level.
- Download PNG and scan it with a phone camera.
- Confirm empty input shows a clear message.

Note: a successful GitHub commit or Pages deployment does not by itself prove QR generation and camera scanning work on every device.

## Android APK
An Android WebView wrapper is included for **QR Generator 171**. It shows a 2-second branded splash screen, then opens the GitHub Pages web app. To build the APK, open **Actions** and select **Build QR Generator 171 Android APK**. The workflow uploads a debug APK artifact when the build succeeds.

Important: the Android wrapper loads the published web app over the internet, so an internet connection is required. A successful workflow build confirms APK compilation, not full on-device functional testing.
