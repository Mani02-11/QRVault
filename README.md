# 📱 QRVault – Smart Contact Sharing with QR Code Generator

## Implement an Android Application for Contact Sharing Using QR Code Generator

---

## 👨‍💻 Author

**Name:** Manikandan P  
**USN:** 25MCAR0186  
**Subject:** Mobile Application Development Lab  
**Project Name:** QRVault

---

## 📖 Experiment Description

This project demonstrates the development of an Android application for sharing contact information using QR codes with Kotlin and XML.

The application retrieves contact information from the Android device using **Content Provider** and allows users to select a contact to generate a QR code.

The selected contact details, including name, phone number, and email address, are converted into a standard **vCard 3.0 format** and encoded into a QR code using the ZXing library.

The generated QR code can be displayed, saved to the device gallery, and shared through applications such as WhatsApp, Gmail, Bluetooth, and other supported applications.

The application also maintains a history of generated QR codes using an SQLite database, allowing users to view, share, and delete previously generated QR codes.

QRVault provides a modern user interface with Light and Dark mode support, contact search, QR code management, and secure local data storage.

### Scenario

The application simulates a **Smart Digital Contact Sharing System**.

```text
              Launch QRVault Application
                         ↓
                  Display Splash Screen
                         ↓
                   Open Home Dashboard
                         ↓
                  Navigate to Contacts
                         ↓
                Request Contact Permission
                         ↓
                 Retrieve Device Contacts
                         ↓
                  Display Contact List
                         ↓
                  Search / Select Contact
                         ↓
                Retrieve Contact Details
                         ↓
                 Generate vCard 3.0 Data
                         ↓
                  Generate QR Code
                         ↓
                 Display Generated QR
                         ↓
              +----------+----------+
              ↓                     ↓
         Save to Gallery        Share QR Code
              ↓                     ↓
         Save PNG Image       Open Android Share Sheet
              ↓
       Store QR Details in SQLite
              ↓
          Open QR History
              ↓
       View / Share / Delete QR
```

---

## 🧠 Concept / Technology Used

### Android Content Provider and QR Code Generation

Android Content Provider provides a standard interface for accessing and managing application data. In this project, it is used to retrieve contact information from the device.

The application uses the following concepts:

* **Content Provider** – Retrieves contact information from the Android device.
* **ContentResolver** – Queries the device contact database.
* **ContactsContract** – Provides access to contact names, phone numbers, and email addresses.
* **Runtime Permission** – Requests permission to access device contacts.
* **RecyclerView / ListView** – Displays retrieved contacts in a structured list.
* **Search Functionality** – Filters contacts by name or phone number.
* **vCard 3.0** – Standard format used to represent contact information.
* **QR Code Generation** – Converts vCard information into a scannable QR code.
* **ZXing Library** – Generates QR code bitmap images.
* **SQLite Database** – Stores generated QR code history.
* **SQLiteOpenHelper** – Creates and manages the local database.
* **MediaStore API** – Saves QR code images to the device gallery.
* **Android Intent** – Shares generated QR code images with other applications.
* **FileProvider** – Provides secure file sharing through content URIs.
* **SharedPreferences** – Stores application theme preferences.
* **Kotlin Coroutines** – Performs time-consuming operations asynchronously.
* **XML** – Defines application screen layouts.
* **Material Design 3** – Provides modern UI components and styling.
* **Android Studio** – Used for application development, execution, and testing.

---

## 🚀 Features

* 📱 Attractive and modern QRVault application interface.
* 🎨 Premium UI with purple and blue gradient styling.
* 🏠 Home dashboard with contact and QR code statistics.
* 📇 Retrieves real contacts from the Android device.
* 🔍 Search contacts by name or phone number.
* 👤 Displays contact names, phone numbers, and email addresses.
* ☑️ Allows users to select contacts.
* 🔳 Generates QR codes using the ZXing library.
* 📄 Converts contact information into vCard 3.0 format.
* 💾 Saves generated QR codes as PNG images.
* 📤 Shares QR codes using Android Intent.
* 📜 Maintains QR generation history using SQLite.
* 🔄 Regenerates QR codes from stored history.
* 🗑️ Supports individual and complete history deletion.
* 🌙 Supports Light and Dark themes.
* ⚙️ Provides application settings and privacy information.
* ⚠️ Handles permission denial and application errors.
* 🎓 Displays student name and USN.

---

# 📂 Project Structure

```text
QRVault/
│
├── app/
│   │
│   ├── manifests/
│   │   └── AndroidManifest.xml
│   │
│   ├── java/
│   │   └── com/
│   │       └── project/
│   │           └── qrvault/
│   │               │
│   │               ├── MainActivity.kt
│   │               ├── SplashActivity.kt
│   │               ├── QRGeneratorActivity.kt
│   │               ├── QRViewerActivity.kt
│   │               │
│   │               ├── adapter/
│   │               │   ├── ContactAdapter.kt
│   │               │   └── QRHistoryAdapter.kt
│   │               │
│   │               ├── database/
│   │               │   └── DatabaseHelper.kt
│   │               │
│   │               ├── fragment/
│   │               │   ├── HomeFragment.kt
│   │               │   ├── ContactsFragment.kt
│   │               │   ├── QRHistoryFragment.kt
│   │               │   └── SettingsFragment.kt
│   │               │
│   │               ├── model/
│   │               │   ├── ContactModel.kt
│   │               │   └── QRHistoryModel.kt
│   │               │
│   │               └── utils/
│   │                   ├── ImageUtils.kt
│   │                   ├── PermissionUtils.kt
│   │                   └── QRCodeUtils.kt
│   │
│   ├── res/
│   │   ├── layout/
│   │   ├── drawable/
│   │   ├── mipmap/
│   │   └── values/
│   │
│   └── build.gradle.kts
│
├── gradle/
│
├── README.md
└── LICENSE
```

---

# 🎨 User Interface

The application contains the following screens:

* **Splash Screen** – Displays QRVault logo and application branding.
* **Home Dashboard** – Displays welcome message, contact statistics, QR statistics, and quick actions.
* **Contacts Screen** – Displays device contacts with search and selection functionality.
* **QR Generator Screen** – Displays selected contact details and generated QR code.
* **QR History Screen** – Displays previously generated QR codes.
* **QR Viewer Screen** – Displays individual QR code details.
* **Settings Screen** – Provides theme selection, history management, and application information.

### Navigation Flow

```text
                SPLASH SCREEN
                      |
                      ↓
                HOME DASHBOARD
                      |
          +-----------+-----------+
          |           |           |
          ↓           ↓           ↓
       CONTACTS     HISTORY    SETTINGS
          |
          ↓
     SELECT CONTACT
          |
          ↓
     QR GENERATOR
          |
      +---+---+
      |       |
      ↓       ↓
     SAVE   SHARE
      |
      ↓
   GALLERY

HISTORY → VIEW QR → SHARE / DELETE
```

---

# 🔑 Permissions Used

The application requires the following permissions in `AndroidManifest.xml`:

### 1. READ_CONTACTS

```xml
<uses-permission android:name="android.permission.READ_CONTACTS" />
```

Allows the application to retrieve contact names, phone numbers, and email addresses from the Android device.

This permission must be requested at runtime before accessing contacts.

### 2. Internet Permission

Not required for the core QRVault functionality because contact retrieval, QR generation, and history management are performed locally.

### Storage Handling

The application uses Android **MediaStore API** to save QR code images.

It follows Android Scoped Storage requirements and avoids requesting unnecessary storage permissions.

### FileProvider

FileProvider is used to share generated QR code images securely with other applications through content URIs.

---

# 🗄️ Database Design

**Database Name:** `qrvault.db`

**Table Name:** `qr_history`

| Column | Data Type | Description |
|---|---|---|
| id | INTEGER | Primary key with auto increment |
| contact_name | TEXT | Contact name |
| phone_number | TEXT | Contact phone number |
| email | TEXT | Contact email address |
| qr_data | TEXT | Generated vCard information |
| created_at | TEXT | QR generation date and time |

### Database Operations

* INSERT – Stores generated QR code details.
* SELECT – Retrieves QR history records.
* SELECT WHERE – Retrieves a specific QR record.
* DELETE WHERE – Deletes an individual history record.
* DELETE FROM – Clears all history records.
* COUNT – Calculates the total number of generated QR codes.

---

# 🧪 Test Cases

The following test cases are used to verify the functionality of the QRVault application.

## Test Case 1 – Application Launch and Home Dashboard

### Objective

To verify that the QRVault application launches successfully and displays the home dashboard.

### Input

1. Launch the QRVault application.
2. Observe the splash screen.
3. Wait for the home dashboard to appear.
4. Verify the navigation options and dashboard cards.

### Expected Result

The application should launch successfully and display the QRVault home dashboard with welcome message, statistics, quick actions, and bottom navigation.

### Actual Result

The application launches successfully and displays the home dashboard.

### Status

✅ **Passed**

### Screenshot – Home Dashboard

<!-- Add your actual home screen screenshot here -->

---

## 🧪 Test Case 2 – Contact Permission and Retrieval

### Objective

To verify that the application requests contact permission and retrieves contacts from the Android device.

### Input

1. Open the QRVault application.
2. Navigate to the Contacts screen.
3. Grant READ_CONTACTS permission.
4. Observe the retrieved contact list.

### Expected Result

The application should successfully retrieve and display contact names, phone numbers, and available email addresses.

### Actual Result

The application successfully retrieves and displays device contacts.

### Status

✅ **Passed**

### Screenshot – Contact List

<!-- Add your actual contacts screenshot here -->

---

## 🧪 Test Case 3 – Contact Search and Selection

### Objective

To verify that users can search and select a contact from the contact list.

### Input

1. Navigate to the Contacts screen.
2. Enter a contact name in the search bar.
3. Observe the filtered results.
4. Select a contact.

### Expected Result

The application should filter matching contacts and display the selected contact's information.

### Actual Result

The application successfully filters and displays the selected contact details.

### Status

✅ **Passed**

### Screenshot – Contact Search and Selection

<!-- Add your actual screenshot here -->

---

## 🧪 Test Case 4 – QR Code Generation

### Objective

To verify that the application converts selected contact information into a QR code using the ZXing library.

### Input

1. Select a contact from the Contacts screen.
2. Navigate to the QR Generator screen.
3. Tap the Generate QR Code button.
4. Observe the generated QR code.

### Expected Result

The application should generate a valid QR code containing the selected contact's information in vCard 3.0 format.

### Actual Result

The application successfully generates and displays the contact QR code.

### Status

✅ **Passed**

### Screenshot – Generated QR Code

<!-- Add your actual QR generator screenshot here -->

---

## 🧪 Test Case 5 – QR Code Scanning and Validation

### Objective

To verify that the generated QR code contains valid contact information and can be recognized by a compatible QR scanner.

### Input

1. Generate a QR code for a selected contact.
2. Open another smartphone's QR scanner.
3. Scan the generated QR code.
4. Verify the decoded contact information.

### Expected Result

The QR scanner should recognize the vCard data and display the contact information, allowing it to be saved as a new contact.

### Actual Result

The generated QR code is successfully recognized by a compatible QR scanner.

### Status

✅ **Passed**

### Screenshot – QR Code Scanning

<!-- Add your actual scanning screenshot here -->


