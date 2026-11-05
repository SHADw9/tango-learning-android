# Tango Learning – Android app
# Tango Learning (単) — App Overview

A single-file Japanese learning web app built for **Bengali speakers**. It runs entirely in the browser from `index.html` — no build step, no server, no account, and no network calls for the learning logic.

---

## What this app is

Tango Learning is a flashcard and quiz trainer for Japanese vocabulary, kanji, kana, and Minna no Nihongo lessons. It combines:

- **Spaced repetition (SRS)** to schedule reviews
- **XP, streaks, and a daily goal** for motivation
- **Four study modes** (cards, quiz, table, plus deck/tag filtering)
- **Bengali-first explanations**, with English also shown for vocabulary

Progress is saved locally in the browser via `localStorage` under the key `tango_v1`.

---

## Who it is for

| Audience | Why it fits |
|---|---|
| **Bengali speakers learning Japanese** | Vocabulary and kanji entries are glossed in Bangla, and the UI direction `বাংলা → 日本語` supports Bengali-to-Japanese recall. |
| **JLPT candidates (N5–N1)** | 8,385 vocabulary items and 2,382 kanji are tagged by JLPT level N5 through N1. |
| **Minna no Nihongo students** | 2,692 entries covering Lessons 1–50, filterable by lesson. |
| **Absolute beginners** | A generated kana deck covers hiragana and katakana, including voiced and combination characters. |
| **Self-study learners on mobile** | Responsive layout, safe-area support, touch-friendly controls, and optional Android text-to-speech integration. |
| **Offline / privacy-conscious users** | No login, no analytics, no cloud storage — everything stays in the browser. |

It is **not** a full dictionary, a grammar course, or a social/community app. There are no example sentences for every word and no sync between devices.

---

## Content / decks

| Deck | Label | Size | Organization |
|---|---|---|---|
| **Kana** | かな | Generated from character tables | Hiragana / Katakana × Basic / Voiced / Combo |
| **Vocabulary** | JLPT 8385 | 8,385 words | JLPT N5–N1 |
| **Kanji** | 漢字 2382 | 2,382 characters | JLPT N5–N1, with readings, Bangla meaning, stroke count |
| **Minna no Nihongo** | L1–L50 | 2,692 entries | Lessons 1–50 |

Each card can include: Japanese form, reading (furigana/romaji), Bangla meaning, English meaning, and a level/lesson tag.

---

## Study modes

### 1. 🃏 Cards (flashcards)
- Tap the card or press **Space** to flip.
- After flipping, rate the card:
  - **Again (1)** — resets progress and shows the card again soon
  - **Good (2)** — moves it forward one SRS box
  - **Easy (3)** — moves it forward two SRS boxes
- Cards animate with a 3D flip; wrong/again cards are reinserted into the queue a few positions later.
- A progress bar shows position within the session.

### 2. 🎯 Quiz (multiple choice)
- Shows a prompt and **four answer options** (one correct, three distractors).
- Correct answer: green, **+10 XP**, auto-advances after ~1.1 s.
- Wrong answer: red, shakes, **+2 XP**, and the card is re-queued.
- Keyboard shortcuts **1–4** select options; **Space** advances.

### 3. 📋 Table (browse / self-test)
- Searchable table of the current deck (Japanese, reading, Bangla, English/Info).
- **Self-test mode** can blur/hide Bangla, Japanese, or readings; tap a blurred cell to reveal it.
- Kana deck is shown as a grid of character tiles instead of a table.
- Pagination: 50 rows per page for tables, 500 tiles for kana.
- Each row has a speaker button.

### 4. Deck + tag filters (shared by all modes)
- Direction selector changes the question/answer sides.
- Session size: 10, 20, 30, or 50 cards.
- Tag chips filter by JLPT level, kana category, or Minna lesson; **All** toggles the whole set.
- Stats show **Due**, **New**, **Learning**, and **Mastered** counts plus a mastery bar.

---

## Answer directions

| Direction | Shown on front → answer on back |
|---|---|
| 日本語 → বাংলা | Japanese → Bangla |
| বাংলা → 日本語 | Bangla → Japanese |
| かな → 漢字 | Kana reading → Kanji |
| 漢字 → かな | Kanji → Kana reading |
| かな → Romaji | Kana → Romaji (Kana deck only) |
| Romaji → かな | Romaji → Kana (Kana deck only) |

Kana/kanji directions automatically skip items that have no distinct reading.

---

## Spaced repetition

The SRS uses six boxes with fixed intervals:

| Box | Interval |
|---|---|
| 0 | 1 minute |
| 1 | 1 day |
| 2 | 3 days |
| 3 | 7 days |
| 4 | 14 days |
| 5 | 30 days |

- A new card starts at box 0.
- **Again** resets the card to box 0.
- **Good** increases the box by 1; **Easy** by 2 (capped at box 5).
- A card counts as **Mastered** at box 4 or higher.
- Reviews are scheduled per **deck + direction** pair, so a word can be strong in one direction and weak in another.

Session building prioritizes overdue cards first, then fills remaining slots with new cards. If nothing is due, the app offers **Practice anyway**.

---

## Motivation and progress

- **XP**: Again = 2, Good = 10, Easy = 15, quiz correct = 10, quiz wrong = 2.
- **Daily goal**: 20 reviews per day, shown as a top progress bar.
- **Streak**: increments on the first review of a new day if the previous day was also studied.
- **Session summary**: number of reviews, accuracy percentage, and XP earned, with a "Next session" or "Practice anyway" action.
- All progress persists in `localStorage` (`tango_v1`).

---

## Appearance and accessibility

- **Light/dark mode** toggle, with automatic system preference detection.
- **Six color palettes**: Indigo (default), Ocean, Sakura, Forest, Sunset, Slate.
- Responsive layout from phone to desktop; safe-area insets for notched devices.
- `prefers-reduced-motion` disables animations and transitions.
- ARIA labels on icon buttons, select elements, and card actions.
- Touch-friendly 38–44 px targets; horizontally scrollable tab/chip rows.
- Japanese-specific font stack (`Noto Sans JP`, `Hiragino Sans`, `Yu Gothic`) and Bengali-friendly `Hind Siliguri`.

---

## Speech

- Uses the browser's **Web Speech API** (`speechSynthesis`, `ja-JP`, rate 0.85).
- If the app is wrapped in an Android WebView exposing `AndroidTTS`, it calls `AndroidTTS.speak()` instead.
- A small bridge (`AndroidTTS.setBars`) also syncs the Android status/navigation bar color to the current theme.
- If speech is unavailable, a toast reports "Speech not supported".

---

## Keyboard shortcuts

| Key | Action |
|---|---|
| **Space / Enter** | Flip card (Cards) or go to next question (Quiz) |
| **1 / 2 / 3** | Rate Again / Good / Easy after flipping |
| **1–4** | Choose an answer in Quiz |
| **S** | Speak the current Japanese text |

Shortcuts are disabled while typing in the search field or while in Table mode.

---

## Technical notes

- **One file**: all HTML, CSS, JavaScript, and data live in `index.html` (~800 KB of embedded JSON).
- **No dependencies** beyond Google Fonts (Hind Siliguri + Noto Sans JP), so fonts need internet on first load; the app logic itself is offline-capable.
- **No backend, no accounts, no tracking** — progress is device/browser-specific.
- **State** is stored in one object: `P` (progress, SRS, XP, streak, preferences) saved to `tango_v1`.
- **Rendering** is plain DOM string templating with delegated click/input/change/keydown handlers.
- **Data format** (`D` object in the script):
  - `D.w` — `[japanese, reading, bangla, english, jlptLevel]`
  - `D.k` — `[kanji, bangla, readings, jlptLevel, strokeCount]`
  - `D.m` — `[japanese, reading, bangla, english, lessonNumber]`

### Known limitations

- Progress does not sync across browsers or devices, and clearing site data erases it.
- No export/import or backup feature.
- No grammar explanations, audio files, or example sentences for every entry.
- Speech quality depends entirely on the device/browser voice engine.
- Table search and self-test blur are convenience features, not a replacement for full dictionary lookup.

---




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
