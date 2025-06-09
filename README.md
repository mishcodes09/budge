# 💸 Budge— Your Personal Expense Tracker

![Budge Banner](https://github.com/user-attachments/assets/fb43b97d-04bc-4893-b62f-2e8974c43812)


## 📱 Purpose of the App

**Budge** is a mobile application designed to empower users to manage their finances more effectively by tracking expenses, monitoring savings goals, and promoting financial awareness. Through a user-friendly interface and engaging gamification features, the app helps users stay within their spending limits by providing visual insights and feedback. Developed as part of the final assessment for the Mobile App Development module, Budge integrates feedback from the prototype phase to deliver an improved and polished user experience.

## 🎨 Design Considerations

| Feature Area | Consideration |
|--------------|----------------|
| **UI/UX** | Responsive and user-friendly interface with a clean, calm design focused on clarity and ease of use |
| **User Feedback** | All prototype feedback was implemented — including a more complete and polished UI, the addition of income tracking functionality, improved spacing, clearer graph visuals, and simplified navigation. |
| **Performance** | Optimized for real device usage (not emulator), ensuring smooth animations and fast database operations |
| **Accessibility** | Icons and color palettes tested for visibility and readability, especially around graphs and budget indicators |

### Screenshots

| Dashboard | Graph View | Add Expense |
|----------|------------|-------------|
| ![dashboard](https://github.com/user-attachments/assets/67cbaf37-594f-4c3a-9e56-b72951a7deee) | ![Graph](https://github.com/user-attachments/assets/9fcb7ad7-251d-49be-84bc-57e0508765a4) | ![Add](https://github.com/user-attachments/assets/1020994c-b98c-451d-9848-be4941ca1f78) |

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

### ✅ Feature 1: Income Tracking Functionality  
Users can now add, view, and manage **income entries** alongside expenses. This allows for a more complete view of their financial status and makes the app suitable for both budgeting and saving purposes.

### ✅ Feature 2: Combined Expense & Income Graph  
An interactive graph displays **both expenses and income per category** over a user-defined time period. This dual-visual representation helps users quickly understand their financial health and make informed decisions.

## 📲 How to Use Budge

Using **Budge** is simple and intuitive. Follow these steps to get started:

### 1. 🔐 Sign Up / Log In
- Launch the app and sign up with your email and password.
- If you already have an account, simply log in.

### 2. ➕ Add Income or Expenses
- Tap the **“+”** button on the dashboard.
- Choose whether to add an **Income** or an **Expense**.
- Fill in the details:
  - **Category**
  - **Description**
  - **Amount**
  - **Date & Time**
  - (Optional) Upload a receipt

### 3. 📊 View Your Finances
- Go to the **Graph** section to view a breakdown of your **income and expenses per category**.
- Use the filters to select a custom date range.

### 4. 🎯 Set and Track Goals
- Define your **minimum and maximum spending goals** per category.
- The app will show how well you are doing through visuals and feedback.

### 5. 📅 Monthly Summary
- At the end of each month, you’ll receive a **summary** showing total spending, income, and how it compares to previous months.

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
```

## 🔚 Final Notes

- ✅ App tested and recorded on a real Android device (**Samsung Galaxy A32**)
- ✅ Includes final assets and app icon
- ✅ Fully integrated with **Firebase** (Realtime Database + Storage)
- ✅ All lecturer feedback implemented from the prototype phase
- ✅ README includes documented custom features

## 👤 Developed By

- **Student Name:** Mihle, Sihle, Nazreen, Micah
- **Module:** PROG7313
- **Institution:** IIE Varsity College
- **Lecturer:** Reece Wanvig
---


