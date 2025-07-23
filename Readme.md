# RideHailingApp 🚕

Mini ride-hailing application built with **Kotlin**, **MVVM**, **Room**, and **Hilt**.

---

## 📲 Features

✅ Simple UI with Google Maps integration  
✅ Input pickup & destination locations  
✅ Fare estimate before ride  
✅ Ride confirmation with driver details  
✅ Local ride history storage  
✅ Surge pricing & traffic multiplier

---

## 🏗 Tech Stack

- Kotlin
- MVVM Architecture
- Room (local database)
- Hilt (dependency injection)
- Coroutines
- Google Maps SDK
- Jetpack Navigation
- Unit & UI Tests (JUnit4, AndroidX Test, Room Testing)

---

## 🚀 Project Setup

### 📋 Prerequisites

- Android Studio Hedgehog or later
- Android Emulator or physical device running API level 21 or higher
- Google Maps API Key

---

### 🔧 How to Run

1️⃣ Clone the repository:
```bash
git clone https://github.com/Chaos97-oss/Ride-hailing-app.git 
````
2️⃣ Open the project in Android Studio.

3️⃣ Add your Google Maps API Key to local.properties
````
MAPS_API_KEY=your_api_key
````
🧪 Running Tests
Run all tests from the command line:

```bash
./gradlew test
./gradlew connectedAndroidTest
```
Or from Android Studio → Run Tests
#### 📚 API Endpoints (Mocked)
GET /api/fare-estimate
Returns a mocked fare estimate.

POST /api/request-ride
Returns a mocked ride confirmation with driver details.
🔒 Security & Industry Standards

This application was developed with security and best‑practice standards in mind. Below are some key principles and practices applied during development:

✅ Dependency Injection

We use Hilt for dependency injection to ensure modular, testable, and secure code without relying on unsafe global state.

✅ No Hard‑Coded Secrets

The Google Maps API key and other sensitive information are not hard‑coded into the codebase. Instead, they are stored in the local.properties file, preventing accidental leaks when pushing to version control.

✅ Safe Data Access

The Room database is accessed through properly defined DAOs (Data Access Objects), ensuring:
	•	Parameterized SQL queries to prevent SQL injection.
	•	Clear separation of concerns between data storage and business logic.

✅ MVVM Architecture

We implement the MVVM pattern, separating concerns between UI, ViewModel, and Repository layers, which promotes predictability and reduces the chance of insecure or inconsistent data flows.

✅ Thread Safety

All long‑running or blocking operations (like Room queries and simulated API calls) are performed using Kotlin Coroutines on background threads, keeping the main thread responsive.

✅ Least Privilege

The app requests only the minimal required permissions (location access for map functionality) in compliance with Android guidelines and privacy principles.

✅ Testing

We include unit tests and instrumented tests to ensure data integrity and correctness of logic, reducing the risk of unexpected bugs or inconsistent states in production.

⸻

📌 Future Enhancements
For production readiness, additional security measures such as data encryption (with EncryptedSharedPreferences or Encrypted Room) and obfuscation of release builds via ProGuard or R8 can be added.
##### 👨‍💻 Author
Chaos97-oss
https://github.com/Chaos97-oss
