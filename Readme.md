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

##### 👨‍💻 Author
Chaos97-oss
https://github.com/Chaos97-oss
