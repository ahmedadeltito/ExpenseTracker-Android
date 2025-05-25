# 🪙 Expense Tracker - A Modern Android App

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.23-blue.svg?logo=kotlin)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.6.7-blue?logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVVM%2FUDF-red.svg)](https://developer.android.com/topic/architecture)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)

A feature-rich, offline-first expense tracking application built from the ground up to showcase a modern, scalable, and reactive Android architecture.

This project is a step-by-step guide and implementation of a complex Android application following best practices in software engineering, UI design, and architecture.

## ✨ About The Project

Expense Tracker is designed to be a robust tool for managing personal finances. It allows users to add, view, edit, and delete their daily expenses seamlessly. The primary goal of this project, however, is not just the features themselves, but to serve as a comprehensive example of building an app with a clean, decoupled, and testable architecture.

| Expense List Screen | Add/Edit Expense Screen | Expense Detail Screen |
| :---: |:---:|:---:|
| `[Screenshot of Expense List]` | `[Screenshot of Add Expense]` | `[Screenshot of Expense Detail]` |

## 🚀 Key Features

-   **CRUD Operations:** Full support for Creating, Reading, Updating, and Deleting expenses.
-   **Reactive UI:** The UI automatically updates in real-time when data changes, thanks to a reactive data layer. For example, editing an expense and navigating back to the detail screen will show the updated data instantly.
-   **Modern UI Toolkit:** Entirely built with Jetpack Compose for a declarative, modern, and efficient UI.
-   **Robust Architecture:** Designed for scalability, testability, and easy maintenance.
-   **In-Memory Data Source:** Currently uses an in-memory data source for rapid development and demonstration (Next step: Room DB persistence!).

## 🏛️ Architectural Overview

This project strictly follows **Clean Architecture** principles, combined with modern Android development patterns.

![Clean Architecture Diagram](https://user-images.githubusercontent.com/11275995/226123495-25d2c645-3677-44da-81e8-7180e53a793a.png)
*(Image credit: Fernando Cejas)*

### Layers

-   **`presentation`**: The UI layer, built with Jetpack Compose. It knows nothing about the data sources.
    -   **UI Pattern:** Unidirectional Data Flow (UDF) is implemented using the **MVVM** pattern.
        -   `UiState`: An immutable data class representing the complete state of a screen.
        -   `UiEvent`: Represents user actions or system events.
        -   `UiSideEffect`: For one-time events like navigation or showing a Snackbar.
    -   **Navigation:** Uses the **Coordinator Pattern** to decouple navigation logic from the UI. An `AppNavigator` class centralizes all navigation actions.

-   **`domain`**: The core business logic of the application. This layer is pure Kotlin and has no dependencies on the Android framework or any specific data source implementation.
    -   **Use Cases:** Encapsulate specific business rules (e.g., `AddExpenseUseCase`, `DeleteExpenseUseCase`).
    -   **Repository Interfaces:** Defines the contract for how the domain layer accesses data, following the Dependency Inversion Principle.
    -   **Entities:** Core business models (e.g., `Expense`).

-   **`data`**: The data layer, responsible for providing data to the domain layer.
    -   **Repository Implementations:** Concrete implementations of the repository interfaces defined in the domain layer.
    -   **Data Sources:** Provides data from a specific source (currently an in-memory cache, designed to be easily replaced by a Room database or a network API).

### Key Architectural Concepts Implemented

-   **Unidirectional Data Flow (UDF):** State flows down from the `ViewModel` to the `Composable` screens, and events flow up from the screens to the `ViewModel`.
-   **Clean Architecture:** Strict separation of concerns between UI, business logic, and data.
-   **Dependency Inversion Principle:** Achieved through repository interfaces and manual dependency injection.
-   **Manual Dependency Injection:** A simple `AppContainer` object acts as a service locator to provide singleton instances of repositories and use cases.
-   **Coordinator Pattern:** `AppNavigator` centralizes navigation logic, making the `ViewModels` and `Composables` unaware of the navigation implementation.
-   **Reactive Programming:** Heavily utilizes **Kotlin Coroutines and Flow** to handle asynchronous operations and provide real-time, reactive updates from the data layer to the UI.

## 🛠️ Technology Stack & Libraries

-   **Language:** [Kotlin](https://kotlinlang.org/)
-   **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
-   **Asynchronous Programming:** [Kotlin Coroutines & Flow](https://kotlinlang.org/docs/coroutines-guide.html)
-   **Architecture Components:**
    -   [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
    -   [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
    -   [SavedStateHandle](https://developer.android.com/topic/libraries/architecture/viewmodel-savedstate) for passing arguments and surviving process death.
-   **Dependency Injection:** Manual DI via a singleton `AppContainer`.

## 📂 Project Structure

└── app/src/main/java/com/yourname/expensetracker/
├── core/                # Base classes, UDF interfaces, navigation core
├── data/                # Data layer
│   ├── datasource/      # Data sources (e.g., in-memory cache)
│   ├── mapper/          # Mappers between data and domain models
│   └── repository/      # Repository implementations
├── di/                  # Manual Dependency Injection container
├── domain/              # Domain layer
│   ├── model/           # Domain entities
│   ├── repository/      # Repository interfaces
│   └── usecase/         # Business logic use cases
└── presentation/        # Presentation layer (UI)
├── features/        # Feature-specific screens (e.g., addexpense, expenselist)
│   ├── addexpense/
│   └── expenselist/
├── navigation/      # AppDestinations, AppNavigator, AppNavHost
└── ui/                # General UI components, Theme

## 🏃 How to Build and Run

1.  Clone the repository:
    ```bash
    git clone [https://github.com/your-github-username/your-repo-name.git](https://github.com/your-github-username/your-repo-name.git)
    ```
2.  Open the project in Android Studio (latest stable version recommended).
3.  Let Gradle sync the dependencies.
4.  Run the app on an emulator or a physical device.

## 🛣️ Future Work & Roadmap

This project is a living example and has plenty of room for growth. Here are some features planned for the future:

-   [ ] **Database Integration:** Replace the in-memory data source with a persistent local database.
-   [ ] **Category Management:** Allow users to create and manage custom expense categories.
-   [ ] **Data Visualization:** Add charts and summaries to visualize spending habits.
-   [ ] **Filtering & Sorting:** Implement filtering and sorting options on the expense list.
-   [ ] **UI/UX Polish:** Add animations, theming options, and improve overall aesthetics.
-   [ ] **Testing:** Write comprehensive unit tests for ViewModels and Use Cases, and UI tests for Compose views.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.