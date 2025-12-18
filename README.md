# FairShare

## Project Overview

FairShare is a mobile Android application designed to help groups of friends, roommates, or colleagues track shared expenses and settle debts easily. The app simplifies expense tracking, reduces confusion, and ensures everyone pays their fair share.

### Goals

- **Simplify Expense Tracking**: Make it effortless for groups to track shared expenses
- **Automate Calculations**: Automatically calculate balances and suggest optimal "Settle Up" payments
- **Reduce Confusion**: Provide clear visibility into who owes what to whom
- **Cloud Sync**: Enable secure, real-time synchronization across devices

### Main Features

- **Group Management**: Create and manage groups for shared expenses
- **Expense Tracking**: Add expenses with description and amount
- **Balance Calculation**: View live balances and suggested "Settle Up" payments
- **Secure Authentication**: Secure login & cloud sync
- **Real-time Updates**: Automatic balance calculations as expenses are added
- **Modern UI**: Built with Jetpack Compose for a beautiful, responsive user experience

## Team Members

- **Marten Ojasaar** - Lead Developer, Researcher ([@Marten221](https://github.com/Marten221))
- **Robin Henrik Neem** - Project Leader / Manager, Editor, Presenter ([@RobinHenrik](https://github.com/RobinHenrik))

## Installation & Setup

### Prerequisites

- **Android Studio**: Latest version (recommended: Hedgehog or newer)
- **JDK**: Java Development Kit 11 or higher
- **Android SDK**: Minimum SDK 24 (Android 7.0), Target SDK 36
- **Git**: For version control

### Dependencies

The project uses the following key dependencies:

- **Kotlin**: 2.2.10
- **Jetpack Compose**: Latest BOM (2025.08.01)
- **Retrofit**: 3.0.0 (for API communication)
- **Coroutines**: 1.10.2 (for asynchronous operations)
- **Navigation Compose**: 2.9.5
- **Material 3**: 1.3.2

### Build Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/Marten221/FairShare.git
   cd FairShare
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the `FairShare` directory and open it

3. **Sync Gradle**
   - Android Studio should automatically sync Gradle dependencies
   - If not, click "Sync Now" or go to `File > Sync Project with Gradle Files`

4. **Build the project**
   - Click `Build > Make Project` or press `Ctrl+F9` (Windows/Linux) / `Cmd+F9` (Mac)
   - Wait for the build to complete

5. **Run the app**
   - Connect an Android device or start an emulator (API 24 or higher)
   - Click the "Run" button or press `Shift+F10` (Windows/Linux) / `Ctrl+R` (Mac)
   - Select your target device and wait for the app to install and launch

### Additional Setup Notes

- The app requires internet connectivity to communicate with the [FairShare API](https://github.com/Marten221/FairShareAPI)
- Ensure your device/emulator has internet access enabled

## Usage Guide

### Getting Started

1. Launch the app
2. Sign up or sign in with your email
3. Create a new group or join an existing one
4. Start adding expenses!

### Key Features Usage

- **Creating Groups**: Once you are logged in you will be taken to the your groups page. On the bottom right there is a plus icon for creating new groups.
- **Joining Groups**: On the bottom left there is a join icon for joining groups. You need to enter the ID of the group you want to join.
- **Adding Expenses**: When you click on a group that you are a part of you will be taken to the group detail screen. Below the group ID and your balance you will see an 'add expense' button. Click it, just type in the description and amount for the item that you paid for and click add.
- **Viewing Balances**: You can see your balance at the top of the group detail screen. You can see if the group as a whole owes you or if you owe the group.
- **Settling Up**: If you click on your balance you can see how to settle up with the fewest amount of transactions. You can see who you need to send money to and who has to send money to you.

### Demo

![Demo GIF](demo.gif)

## Project Structure

The project follows a clean architecture pattern with clear separation of concerns:

```
FairShare/
├── app/
│   ├── build.gradle.kts          # App-level build configuration
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/fairshare/
│   │   │   │   ├── MainActivity.kt          # Main entry point
│   │   │   │   ├── AppNav.kt               # Navigation setup
│   │   │   │   ├── data/                   # Data layer
│   │   │   │   │   ├── remote/             # API interfaces and network models
│   │   │   │   │   │   ├── AuthApi.kt
│   │   │   │   │   │   ├── BalanceApi.kt
│   │   │   │   │   │   ├── ExpenseApi.kt
│   │   │   │   │   │   ├── GroupsApi.kt
│   │   │   │   │   │   ├── models/         # API response models
│   │   │   │   │   │   └── NetworkModule.kt
│   │   │   │   │   └── repository/         # Repository implementations
│   │   │   │   │       ├── AuthRepositoryImpl.kt
│   │   │   │   │       ├── BalanceRepositoryImpl.kt
│   │   │   │   │       ├── ExpenseRepositoryImpl.kt
│   │   │   │   │       └── GroupsRepositoryImpl.kt
│   │   │   │   ├── domain/                 # Domain layer (business logic)
│   │   │   │   │   ├── model/              # Domain models
│   │   │   │   │   │   ├── Balance.kt
│   │   │   │   │   │   ├── Expense.kt
│   │   │   │   │   │   └── Group.kt
│   │   │   │   │   └── repository/         # Repository interfaces
│   │   │   │   │       ├── AuthRepository.kt
│   │   │   │   │       ├── BalanceRepository.kt
│   │   │   │   │       ├── ExpenseRepository.kt
│   │   │   │   │       └── GroupsRepository.kt
│   │   │   │   └── ui/                     # UI layer
│   │   │   │       ├── screens/            # Composable screens
│   │   │   │       │   ├── GroupDetailScreen.kt
│   │   │   │       │   ├── GroupsListScreen.kt
│   │   │   │       │   └── HomePageScreen.kt
│   │   │   │       ├── theme/              # Theme configuration
│   │   │   │       │   ├── Color.kt
│   │   │   │       │   ├── Theme.kt
│   │   │   │       │   └── Type.kt
│   │   │   │       └── viewmodels/         # ViewModels for state management
│   │   │   │           ├── AuthViewModel.kt
│   │   │   │           ├── GroupDetailViewModel.kt
│   │   │   │           └── GroupsListViewModel.kt
│   │   │   ├── res/                        # Resources (images, layouts, etc.)
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                           # Unit tests
│   │   └── androidTest/                    # Instrumented tests
│   └── proguard-rules.pro
├── build.gradle.kts                        # Project-level build configuration
├── settings.gradle.kts                     # Project settings
├── gradle/
│   ├── libs.versions.toml                 # Dependency version catalog
│   └── wrapper/                            # Gradle wrapper files
├── docs/                                   # Documentation
│   ├── paper-prototype/                    # Design prototypes
│   ├── step3_report.md
│   └── step4_report.md
└── README.md                               # This file
```

### Architecture Overview

The project follows **Clean Architecture** principles with three main layers:

1. **UI Layer** (`ui/`): 
   - Jetpack Compose screens and components
   - ViewModels for state management
   - Theme configuration

2. **Domain Layer** (`domain/`):
   - Business logic and domain models
   - Repository interfaces (contracts)

3. **Data Layer** (`data/`):
   - Repository implementations
   - API interfaces (Retrofit)
   - Network models and data mapping

### Key Technologies

- **Jetpack Compose**: Modern declarative UI framework
- **MVVM Architecture**: ViewModels manage UI state
- **Retrofit**: Type-safe HTTP client for API communication
- **Room Database**: Local data persistence (if implemented)
- **Coroutines & Flow**: Asynchronous programming
- **Navigation Compose**: Type-safe navigation between screens
- **Material 3**: Modern Material Design components

## External API

This project uses a custom backend API:
- **FairShare API**: [https://github.com/Marten221/FairShareAPI](https://github.com/Marten221/FairShareAPI)


