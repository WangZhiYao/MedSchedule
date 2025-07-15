# MedSchedule

**MedSchedule** is a modern Android application designed to help users easily manage their personal
medications and track their medication history. The project is built entirely with Kotlin and
Jetpack Compose, strictly following the MVI (Model-View-Intent) architecture pattern to ensure
high-quality, maintainable, and scalable code.

> **Note:** This application is currently under active development, and some features may not be
> fully implemented.

## Features

The project aims to provide a complete and seamless medication management experience. The core
features currently implemented include:

### Medicine Cabinet (feature:medication)

* **Medication List:** Displays all medications in a clean card format, showing name, notes, and
  stock information.
* **Add Medication:** Supports adding new medications, including name, initial stock, dose unit, and
  notes.
* **View Details:** Tapping a medication opens a detail screen with more information and its
  associated medication history.
* **Edit & Delete:** Allows for modifying and deleting existing medication details.
* **Stock Management:** Easily add to the stock of any medication.

### Medication Records (feature:medication-record)

* **History Log:** Shows all medication records in reverse chronological order, using different
  colors to indicate the status of each record.
* **Manual Entry:** Supports manually creating new medication records, including selecting the date,
  time, medications taken, and dosage.
* **View Details:** Check the details of a single medication record, including a list of all
  medications taken and any notes.
* **Delete Record:** Medication records can be deleted if they are no longer needed.

### Medication Plan (feature:medication-plan) - In-progress

* Set up scheduled medication reminders.
* Automatically generate medication records based on the plan.

## Tech Stack & Architecture

This project is built using the latest in Android development technology and best practices to
create a robust and maintainable application.

* Language: Kotlin
  * Fully utilizing Coroutines and Flow for asynchronous programming.

* UI: Jetpack Compose
  * Building a declarative and responsive UI with the Material 3 design system.
  * Supports both dark and light themes.

* Architecture: MVI (Model-View-Intent)
  * State: A single UiState data class serves as the single source of truth for the UI.
  * ViewModel: Acts as the center for business logic, receiving Events and updating State.
  * SideEffect: SharedFlow is used for one-time events like Toasts and navigation.

* Dependency Injection: Hilt
  * Manages dependencies throughout the application, improving modularity and testability.

* Asynchronous Processing: Kotlin Coroutines + Flow
  * All long-running tasks (like database access) are performed in coroutines to keep the UI thread
    unblocked.

* Data Persistence: Room
  * Serves as the local database for storing core data like medications and records.

* Pagination: Paging 3
  * Efficiently loads and displays large lists of data, such as medication history.

* Navigation: Navigation-Compose
  * Implements type-safe navigation between Composables.

## Project Structure (Modular)

The project uses a clean, multi-module architecture to achieve separation of concerns and promote
high cohesion and low coupling.

```
MedSchedule/
├── app/                   # Main application module, integrates all features
|
├── core/                  # Core logic modules
|   ├── common/            # Common utilities (logging, coroutine dispatchers, etc.)
|   ├── data/              # Data layer (Repository implementations, Room DB, Mappers)
|   └── domain/            # Domain layer (UseCases, domain models, Repository interfaces)
|
├── feature/               # Feature modules
|   ├── home/              # Home screen feature
|   ├── medication/        # Medicine cabinet feature
|   ├── medication-plan/   # Medication plan feature
|   └── medication-record/ # Medication record feature
|
└── shared/                # Shared modules
    ├── designsystem/      # Design system (Theme, Colors, Icons, Typography)
    └── ui/                # Shared UI components (BaseViewModel, common Composables, etc.)
```

* `app:` The entry point of the application, responsible for assembling all feature modules and
  handling top-level navigation.
* `feature:` Each feature module is an independent, self-contained functional unit (e.g.,
  medication, medication-record).
* `core:` Contains the business core of the application.
  * `domain:` Defines the business rules and data models, independent of any specific
    implementation.
  * `data:` Implements the interfaces defined in the domain layer and provides concrete data
    sources (local database or network).
* `shared:` Provides resources and code shared across modules.
  * `designsystem:` Unifies the visual style of the application.
  * `ui:` Contains reusable Composable components and a base ViewModel.

## How to Build

1. Clone the repository:
    ```bash
    git clone https://github.com/wangzhiyao/MedSchedule.git
    ```
2. Open the project in `Android Studio Narwhal | 2025.1.1` or a newer version.
3. Wait for Gradle to sync and build the project.
4. Run the app module on an emulator or a physical device.