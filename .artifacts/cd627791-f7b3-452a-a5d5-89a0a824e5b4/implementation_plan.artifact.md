# Implementation Plan - QRVault Android Application

Build a complete, fully functional Android application named **QRVault – Smart Contact Sharing with QR Code Generator** in Kotlin, matching the provided UI reference designs.

## UI Reference Alignment

The application UI will strictly follow the provided reference image layout:
1. **Splash Screen**: Full-bleed rich vibrant wave gradient background, large QR Vault logo with rounded corner brackets, bold title "QRVault", tagline *"Your Contacts. One Scan Away."*, page indicator dots.
2. **Home Dashboard (Light & Dark)**:
   - Header with greeting banner card featuring 3D phone artwork, "Welcome to QRVault 👋", subtitle "Manage and share your contacts smartly."
   - 3 Quick Action Cards: "Generate QR" (scan icon), "My Contacts" (people icon), "QR History" (clock icon).
   - Statistics Cards: "Total Contacts" & "QR Codes Generated".
   - "Recently Generated" section with item preview (QR thumbnail, Name, Phone, timestamp, overflow menu) and "View All ->".
   - Bottom Navigation with 5 tabs (Home, Contacts, Generate, History, Settings).
3. **Contacts Screen**:
   - Header with search bar ("Search contacts...").
   - List with avatar initial badges / profile pictures, contact details, and radio/check selection indicators.
   - Sticky bottom button: "Generate QR Code (Count)".
4. **QR Generator Screen**:
   - Contact detail header card (Avatar, Name, Phone, Email).
   - Large centered QR Card with stylish purple corner frame brackets and contact name below QR.
   - 4 Action Buttons: "Save to Gallery" (Download icon), "Share QR Code" (Share icon), "Regenerate QR" (Refresh icon), "Back to Contacts" (Back icon).
5. **Success Dialog**:
   - Custom Material Dialog with green checkmark icon, "QR Code Saved!", subtext, and "OK" button.
6. **QR History Screen**:
   - List displaying QR code thumbnails, contact name, phone number, formatted date/time, and context menu.
   - Action bar trash icon to clear history.
7. **Settings Screen**:
   - Items: About QRVault (Version 1.0.0), Theme Switcher (Light / Dark Mode), Clear QR History (with red icon), Privacy Policy, App Information.
   - Bottom branding card: "QRVault - Smart. Secure. Simple."

---

## User Review Required

> [!NOTE]
> The app will be written in **Kotlin**, target SDK 36 (min SDK 24), use Material 3, SQLite helper, ZXing core library for QR bitmap rendering, ContentResolver for reading contacts, MediaStore for gallery saving, and FileProvider for intent sharing.

## Proposed Changes

### Build & Dependencies
#### [MODIFY] [libs.versions.toml](file:///C:/Users/Mani/AndroidStudioProjects/QRVault/gradle/libs.versions.toml)
#### [MODIFY] [build.gradle.kts](file:///C:/Users/Mani/AndroidStudioProjects/QRVault/app/build.gradle.kts)
- Add ZXing core dependency (`com.google.zxing:core:3.5.3`).

---

### UI Resources & Styling
#### [MODIFY] [colors.xml](file:///C:/Users/Mani/AndroidStudioProjects/QRVault/app/src/main/res/values/colors.xml)
#### [MODIFY] [themes.xml](file:///C:/Users/Mani/AndroidStudioProjects/QRVault/app/src/main/res/values/themes.xml)
#### [NEW] [themes.xml (night)](file:///C:/Users/Mani/AndroidStudioProjects/QRVault/app/src/main/res/values-night/themes.xml)
#### [MODIFY] [strings.xml](file:///C:/Users/Mani/AndroidStudioProjects/QRVault/app/src/main/res/values/strings.xml)
#### [NEW] Drawables & Icons
- Gradient backgrounds (`bg_splash_gradient.xml`, `bg_banner_gradient.xml`, `bg_qr_frame.xml`, `btn_primary_gradient.xml`).
- Custom vector drawables for QR frame brackets, quick action badges, bottom nav icons, and success checkmark.

---

### Core & Data Layer
#### [MODIFY] [AndroidManifest.xml](file:///C:/Users/Mani/AndroidStudioProjects/QRVault/app/src/main/res/AndroidManifest.xml)
- `READ_CONTACTS` permission, FileProvider declaration, activity definitions (`SplashActivity`, `MainActivity`, `QRGeneratorActivity`, `QRViewerActivity`).
#### [NEW] [file_paths.xml](file:///C:/Users/Mani/AndroidStudioProjects/QRVault/app/src/main/res/xml/file_paths.xml)
#### [NEW] [DatabaseHelper.kt](file:///C:/Users/Mani/AndroidStudioProjects/QRVault/app/src/main/java/com/project/qrvault/database/DatabaseHelper.kt)
- SQLite helper managing `qrvault.db` (`qr_history` table).
#### [NEW] [ContactModel.kt](file:///C:/Users/Mani/AndroidStudioProjects/QRVault/app/src/main/java/com/project/qrvault/model/ContactModel.kt)
#### [NEW] [QRHistoryModel.kt](file:///C:/Users/Mani/AndroidStudioProjects/QRVault/app/src/main/java/com/project/qrvault/model/QRHistoryModel.kt)

---

### Utilities & Adapters
#### [NEW] [QRCodeUtils.kt](file:///C:/Users/Mani/AndroidStudioProjects/QRVault/app/src/main/java/com/project/qrvault/utils/QRCodeUtils.kt)
- ZXing bitmap encoding & vCard 3.0 formatting.
#### [NEW] [ImageUtils.kt](file:///C:/Users/Mani/AndroidStudioProjects/QRVault/app/src/main/java/com/project/qrvault/utils/ImageUtils.kt)
- Scoped storage MediaStore gallery saving & FileProvider URI sharing.
#### [NEW] [PermissionUtils.kt](file:///C:/Users/Mani/AndroidStudioProjects/QRVault/app/src/main/java/com/project/qrvault/utils/PermissionUtils.kt)
- Runtime permission request flow.
#### [NEW] [ContactAdapter.kt](file:///C:/Users/Mani/AndroidStudioProjects/QRVault/app/src/main/java/com/project/qrvault/adapter/ContactAdapter.kt)
- List adapter with search filtering, contact initial badges, radio selection state.
#### [NEW] [QRHistoryAdapter.kt](file:///C:/Users/Mani/AndroidStudioProjects/QRVault/app/src/main/java/com/project/qrvault/adapter/QRHistoryAdapter.kt)
- History list adapter with item thumbnail rendering, share/delete popup menu.

---

### Activities & Fragments
#### [NEW] [SplashActivity.kt](file:///C:/Users/Mani/AndroidStudioProjects/QRVault/app/src/main/java/com/project/qrvault/SplashActivity.kt) & `activity_splash.xml`
- Full-screen splash with gradient, QR logo, and tagline.
#### [MODIFY] [MainActivity.kt](file:///C:/Users/Mani/AndroidStudioProjects/QRVault/app/src/main/java/com/project/qrvault/MainActivity.kt) & `activity_main.xml`
- Main host with BottomNavigationView (Home, Contacts, Generate, History, Settings).
#### [NEW] [HomeFragment.kt](file:///C:/Users/Mani/AndroidStudioProjects/QRVault/app/src/main/java/com/project/qrvault/fragment/HomeFragment.kt) & `fragment_home.xml`
- Dashboard with welcome banner, statistics cards, 3 quick action buttons, recent QRs list.
#### [NEW] [ContactsFragment.kt](file:///C:/Users/Mani/AndroidStudioProjects/QRVault/app/src/main/java/com/project/qrvault/fragment/ContactsFragment.kt) & `fragment_contacts.xml`
- Search bar, contact list from ContentResolver, permission prompt, bottom "Generate QR Code (1)" button.
#### [NEW] [QRGeneratorActivity.kt](file:///C:/Users/Mani/AndroidStudioProjects/QRVault/app/src/main/java/com/project/qrvault/QRGeneratorActivity.kt) & `activity_qr_generator.xml`
- Selected contact summary card, framed QR matrix view, 4 action buttons (Save to Gallery, Share QR Code, Regenerate QR, Back to Contacts), success dialog modal.
#### [NEW] [QRHistoryFragment.kt](file:///C:/Users/Mani/AndroidStudioProjects/QRVault/app/src/main/java/com/project/qrvault/fragment/QRHistoryFragment.kt) & `fragment_qr_history.xml`
- History list with item thumbnails, top bar clear all action, context popup menu for delete/share.
#### [NEW] [QRViewerActivity.kt](file:///C:/Users/Mani/AndroidStudioProjects/QRVault/app/src/main/java/com/project/qrvault/QRViewerActivity.kt) & `activity_qr_viewer.xml`
- Dedicated viewer activity for viewing, saving, or sharing existing history QR codes.
#### [NEW] [SettingsFragment.kt](file:///C:/Users/Mani/AndroidStudioProjects/QRVault/app/src/main/java/com/project/qrvault/fragment/SettingsFragment.kt) & `fragment_settings.xml`
- About, Version, Theme switcher (Light/Dark mode using `AppCompatDelegate`), Clear History, Privacy Policy, App Information, and bottom branding box.

## Verification Plan

### Automated Build Verification
- Run `./gradlew assembleDebug` via `gradle_build` to verify zero build errors.

### Manual Verification
- Deploy to Android device/emulator.
- Verify Splash transition -> Home Dashboard.
- Verify Home Dashboard banner, stats, quick action navigation.
- Grant `READ_CONTACTS` permission & verify contact listing and search.
- Select contact & click "Generate QR Code (1)" -> verify `QRGeneratorActivity`.
- Test "Save to Gallery" -> check custom success dialog display and gallery image.
- Test "Share QR Code" -> test Android Intent share sheet.
- Check SQLite database insertion in QR History tab.
- Test QR History viewing, sharing, individual item deletion, and clear history dialog.
- Test Light / Dark Mode toggle in Settings.
