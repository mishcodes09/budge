# 💸 Budge— Your Personal Expense Tracker

![Budge Banner](https://github.com/user-attachments/assets/fb43b97d-04bc-4893-b62f-2e8974c43812)


## 📱 Purpose of the App

**Budge** is a mobile application designed to empower users to manage their finances more effectively by tracking expenses, monitoring savings goals, and promoting financial awareness. Through a user-friendly interface and engaging gamification features, the app helps users stay within their spending limits by providing visual insights and feedback. Developed as part of the final assessment for the Mobile App Development module, Budge integrates feedback from the prototype phase to deliver an improved and polished user experience.

## 🎨 Design Considerations

| Feature Area | Consideration |
|--------------|----------------|
| **UI/UX** | Responsive and user-friendly interface with a clean, calm design focused on clarity and ease of use |
| **User Feedback** | All prototype feedback was implemented — e.g., improved spacing, clearer graph visuals, simpler navigation |
| **Performance** | Optimized for real device usage (not emulator), ensuring smooth animations and fast database operations |
| **Accessibility** | Icons and color palettes tested for visibility and readability, especially around graphs and budget indicators |

### Screenshots

| Dashboard | Graph View | Add Expense |
|----------|------------|-------------|
| ![dashboard](https://github.com/user-attachments/assets/a7e03468-1f95-44b7-8f58-1453f2373d7c) | ![Graph](https://github.com/user-attachments/assets/8bc26829-249c-4c26-909a-dccfa959ce79) | ![Add](https://github.com/user-attachments/assets/75cc3c56-0fed-4eb6-87b8-286a1998f031) |

## 🧠 Core Features

- 🔥 **Firebase Integration**
  - Realtime Database to store and sync user data
  - Authentication with secure sign-in
- 📊 **Spending Graph**
  - Visualize spending per category over a user-selected time range
  - Interactive bar chart using MPAndroidChart
- 🎯 **Goal Tracking**
  - Set min/max limits per category
  - Visual indicators show how well the user is doing compared to their goals

## 🚀 Additional Features

### ✅ Feature 1: Receipt Upload
Users can upload an image of their receipt when logging a transaction, which is stored in Firebase Storage and linked to the expense.

### ✅ Feature 2: Monthly Breakdown Summary
At the end of each month, users receive a summary screen with:
- Total spending per category
- Comparison to previous months
- A star badge if they stay within budget in all categories


## 🛠️ Technologies Used

| Technology | Purpose |
|------------|---------|
| **Kotlin** | Main language for Android app development |
| **Firebase Realtime DB** | Backend database to store user data |
| **Firebase Storage** | Store receipt image uploads |
| **MPAndroidChart** | Render interactive bar and line graphs |
| **GitHub Actions** | Automate builds, run tests, and deploy previews |
| **Android Studio** | Main development environment |


## 🔧 GitHub and GitHub Actions

### Version Control

This project was developed using Git and hosted on GitHub. Major features were developed in dedicated branches and merged via pull requests, enabling clean version control and teamwork readiness.

### GitHub Actions Integration

GitHub Actions automates the following:
- ✅ Code linting and formatting checks on push
- ✅ Unit test execution for critical data components
- ✅ Build checks to ensure the app compiles on all commits

Example Workflow File: `.github/workflows/android.yml`
```yaml
name: Android CI

on:
  push:
    branches:
      - main
  pull_request:

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    - name: Set up JDK
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
    - name: Build Project
      run: ./gradlew build

## 🔚 Final Notes

- ✅ App tested and recorded on a real Android device (**Samsung Galaxy A32**)
- ✅ Includes final assets and app icon
- ✅ Fully integrated with **Firebase** (Realtime Database + Storage)
- ✅ All lecturer feedback implemented from the prototype phase
- ✅ README includes documented custom features

### 📹 Video Demo
[🎥 Watch Demo](https://example.com/final_demo_video) <!-- Replace with your actual demo link -->

---

## 👤 Developed By

- **Student Name:** John Doe  
- **Module:** INSY6212 / IPMA6212  
- **Institution:** [Your University Name]  
- **Lecturer:** [Lecturer’s Name]  

---

## 📂 Repo Structure
📦 app/
 ┣ 📂 src/
 ┃ ┣ 📂 main/
 ┃ ┃ ┣ 📂 java/
 ┃ ┃ ┣ 📂 res/
 ┃ ┃ ┣ 📜 AndroidManifest.xml
 ┣ 📂 images/
 ┃ ┣ dashboard.png
 ┃ ┣ graph.png
 ┃ ┣ add_expense.png
 ┃ ┣ banner.png
 ┣ 📜 README.md
 ┣ 📜 .gitignore
 ┣ 📜 build.gradle


---

## 📬 Feedback & Contact

Feel free to raise an issue or suggest improvements using the **[Issues](../../issues)** tab.  
For direct contact: 📧 **johndoe@example.com**
