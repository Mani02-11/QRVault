# QRVault – Smart Contact Sharing with QR Code Generator

**QRVault** is a modern, high-performance Android application designed for seamless contact sharing using dynamic QR codes. Built with Kotlin and Material Design 3, QRVault turns device contacts into standard vCard 3.0 QR codes that can be instantly scanned, saved to the gallery, or shared across applications.

---

## ✨ Features

- 📱 **Modern & Premium UI/UX**: Designed following contemporary digital identity aesthetics with vibrant gradients, clean typography, rounded card elevations, and full Light & Dark mode support.
- ⚡ **Home Dashboard**: Features a welcome header, live contact & QR code statistics, quick action shortcuts, and a preview of recently generated QR codes.
- 📇 **Real-Time Contact Access**: Asynchronously fetches phone contacts via Android `ContentResolver` with real-time instant search filtering.
- 🔳 **vCard 3.0 QR Engine**: Encodes selected contact details into standard vCard format (`BEGIN:VCARD...`) using high-resolution ZXing barcode matrix generation.
- 💾 **Gallery Saving**: Saves generated QR codes as PNG images in the device gallery using Android `MediaStore` with Scoped Storage compliance (`Pictures/QRVault`).
- 📤 **Intent Sharing**: Uses `FileProvider` (`androidx.core.content.FileProvider`) to safely share QR code images across WhatsApp, Gmail, Bluetooth, and installed apps.
- 📜 **SQLite History Tracking**: Automatically stores generated QR code records in a local SQLite database (`qrvault.db`) with date/time stamps, context menus, and single/batch deletion options.
- ⚙️ **Settings & Customization**: Includes Light/Dark theme switching, clear history management with safety dialogs, app version, and privacy information.

---

## 🛠️ Technology Stack

| Layer | Technology / Library |
| :--- | :--- |
| **Language** | Kotlin 1.9+ |
| **UI Framework** | XML Layouts + Material Design 3 Components (`com.google.android.material`) |
| **QR Code Library** | ZXing Core (`com.google.zxing:core:3.5.3`) |
| **Database** | SQLite (`SQLiteOpenHelper`) |
| **Async Tasks** | Kotlin Coroutines (`Dispatchers.IO`) |
| **Storage & Sharing** | MediaStore API & Android FileProvider |
| **Min / Target SDK** | Min SDK 24 (Android 7.0) / Compile SDK 37 |

---

## 📂 Project Structure

```
QRVault/
├── app/
│   ├── src/main/
│   │   ├── java/com/project/qrvault/
│   │   │   ├── MainActivity.kt               # Main Host Activity with BottomNavigationView
│   │   │   ├── SplashActivity.kt             # Brand Splash Screen
│   │   │   ├── QRGeneratorActivity.kt        # QR Display & Action Screen
│   │   │   ├── QRViewerActivity.kt           # History Item Detail Screen
│   │   │   ├── adapter/
│   │   │   │   ├── ContactAdapter.kt         # Contact list adapter with search filter
│   │   │   │   └── QRHistoryAdapter.kt        # History list adapter with context menu
│   │   │   ├── database/
│   │   │   │   └── DatabaseHelper.kt         # SQLite OpenHelper (qrvault.db)
│   │   │   ├── fragment/
│   │   │   │   ├── HomeFragment.kt           # Dashboard, stats & quick actions
│   │   │   │   ├── ContactsFragment.kt       # Device contacts & selection
│   │   │   │   ├── QRHistoryFragment.kt      # Saved history records list
│   │   │   │   └── SettingsFragment.kt       # Theme & app settings
│   │   │   ├── model/
│   │   │   │   ├── ContactModel.kt           # Contact data model
│   │   │   │   └── QRHistoryModel.kt         # History data model
│   │   │   └── utils/
│   │   │       ├── ImageUtils.kt             # MediaStore gallery saving & intent sharing
│   │   │       ├── PermissionUtils.kt        # READ_CONTACTS permission helper
│   │   │       └── QRCodeUtils.kt            # ZXing QR matrix bitmap generator
│   │   └── res/                             # Layouts, drawables, themes & strings
│   └── build.gradle.kts                      # Module build script
├── gradle/                                   # Gradle wrapper & versions catalog
└── README.md
```

---

## 🚀 Building & Running

### Prerequisites
- **Android Studio**: Android Studio Ladybug or newer.
- **JDK**: Java 17 or higher.
- **Device / Emulator**: Android API 24 (Android 7.0) or higher.

### Steps to Build
1. Clone or open the project in **Android Studio**.
2. Sync the project with Gradle files (`File -> Sync Project with Gradle Files`).
3. Connect an Android physical device via USB / Wi-Fi debugging or launch an AVD Emulator.
4. Run the project by selecting `app` and clicking the **Run** button (`Shift + F10`).

---

## 🔒 Privacy & Permissions

- **READ_CONTACTS**: Requested at runtime solely to allow users to pick a contact for generating a QR code.
- **Local Data Processing**: Contact data and QR generation are processed 100% locally on the device. No contact information is ever transmitted to external servers.

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).
