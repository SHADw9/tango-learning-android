# Tango Learning – Android app

Japanese ⇄ Bangla flashcards (kana, JLPT vocabulary, kanji, Minna no Nihongo) packaged as an Android app.
The whole web app lives in `app/src/main/assets/index.html`; a thin Java `WebView` shell runs it
and connects the 🔊 button to Android's **native text-to-speech**.

## Get the APK (no tools needed on your computer)
1. Create a new empty repository on GitHub.
2. Upload this project (keep the folder structure, including the hidden `.github` folder):
   ```
   git init && git add . && git commit -m "Tango Learning"
   git branch -M main
   git remote add origin https://github.com/<you>/<repo>.git
   git push -u origin main
   ```
3. Open the **Actions** tab – the *Build & Release APK* workflow starts automatically (about 3–5 min).
4. When it finishes, open the **Releases** page of your repo and download `Tango-Learning-v1.0.N.apk`.
5. On your phone: open the downloaded file and allow "Install unknown apps" for your browser when asked.

Every push to `main` builds a new version and publishes a new release (`v1.0.<run number>`).
You can also run it manually from Actions → *Run workflow*.

## Updating the content
Edit `app/src/main/assets/index.html` (or replace it with a new export of the page) and push.

## Signing key
The project includes `keystore/tango.jks` (password `tangolearning`) so every release is signed with the
same key and installs as an **update** over the previous one. This is fine for personal use.
If you make the repo public or want your own key, create one and add these repository secrets
(Settings → Secrets and variables → Actions); the workflow then uses it automatically:

| Secret | Value |
|---|---|
| `KEYSTORE_BASE64` | `base64 -w0 my.jks` output |
| `KEYSTORE_PASSWORD` | keystore password |
| `KEY_ALIAS` | key alias |
| `KEY_PASSWORD` | key password |

Switching keys later requires uninstalling the old app first (your progress is stored on the device).

## Japanese voice
The app uses the phone's text-to-speech engine. If you hear nothing, open
**Settings → System → Languages → Text-to-speech output** and install the *Japanese* voice data
(the app also offers to open the installer the first time).

## Requirements
Android 8.0 (API 26) or newer. Internet is only used for the optional Google Fonts; everything else works offline.
