# EventBazaar - Shop. Book. Celebrate.

A modern Android eCommerce + Event Booking application built with Kotlin, Jetpack Compose, MVVM architecture, and Firebase.

## Features

### User Panel
- **Products**: Browse and buy birthday/event-related products (decorations, cakes, gifts)
- **Booking**: Book event venues/halls with date, time, and hall selection
- **Orders**: View purchase history and booking history
- **Settings**: Manage profile (change name, password), logout

### Admin Panel
- **Users List**: View all registered users
- **Add Product**: Add products with image upload (via Cloudinary)
- **Add Booking Event**: Add halls with pricing and availability

### Authentication
- Email/password login and registration via Firebase Auth
- Role-based access (user/admin)

## Tech Stack
- **Language**: Kotlin
- **UI**: Jetpack Compose + Material Design 3
- **Architecture**: MVVM (Model-View-ViewModel)
- **Backend**: Firebase Authentication + Cloud Firestore
- **Image Storage**: Cloudinary (unsigned upload)
- **Image Loading**: Coil
- **Navigation**: Jetpack Navigation Compose

## Project Structure
```
com.example.eventshop/
├── model/          # Data classes (User, Product, Booking, Hall, Order, CartItem)
├── repository/     # Firebase & Cloudinary data access layer
├── viewmodel/      # Business logic (AuthVM, ProductVM, BookingVM, OrderVM, UserVM, AdminVM)
├── view/
│   ├── auth/       # Login & Register screens
│   ├── user/       # User panel screens (Products, Booking, Orders, Settings)
│   └── admin/      # Admin panel screens (Users, Add Product, Add Hall)
├── navigation/     # Nav graph, bottom nav items, screen routes
├── utils/          # Constants, Resource sealed class, CloudinaryUploader
└── ui/theme/       # Material3 color scheme, typography
```

## Setup Instructions

### 1. Firebase Setup
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project
3. Add an Android app with package name `com.example.eventshop`
4. Download `google-services.json` and place it in `app/`
5. Enable **Authentication** → Email/Password sign-in method
6. Enable **Cloud Firestore** and create a database

### 2. Cloudinary Setup
1. Create a free account at [Cloudinary](https://cloudinary.com/)
2. Go to Settings → Upload → Add upload preset (set to **Unsigned**)
3. Open `app/src/main/java/com/example/eventshop/utils/Constants.kt`
4. Replace `YOUR_CLOUD_NAME` with your Cloudinary cloud name
5. Replace `YOUR_UPLOAD_PRESET` with your unsigned upload preset name

### 3. Admin User Setup
1. Register a new user through the app
2. Go to Firebase Console → Firestore → `users` collection
3. Find the user document and change the `role` field from `"user"` to `"admin"`
4. Log out and log back in — you'll be redirected to the Admin panel

## Build & Run
1. Open the project in Android Studio
2. Ensure `google-services.json` is in the `app/` directory
3. Sync Gradle
4. Run on an emulator or physical device (min SDK 24)
