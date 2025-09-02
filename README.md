# xero-invoices-demo

This is a demo Android project that displays invoices in a scrollable list.

The minimum supported Android SDK is 26 (Oreo), covering approximately 97.4% of devices. This version offers improved support for `LocalDateTime`, `DateTimeFormatter`, and related APIs.

The project adheres to SOLID principles. Each class is responsible for its core function and implements a defined `interface`. The structure separates API definitions (`interface`) from feature implementations (`impl`), enabling easy dependency injection and override during unit testing.

MVVM architecture is used: data persists in the ViewModel via `StateFlow`, Compose components observe state changes and recompose accordingly, and UI actions are dispatched to the ViewModel.

While MVI (Model-View-Intent) is popular in modern Android Compose projects, MVVM remains widely adopted and is straightforward for most developers.

## App Preview

The app reads from three files to render different invoice states:

- **Valid JSON array:** Displays invoices in a list. Invoice line items are hidden by default ([see screenshots](#collapse-state)) and can be expanded by tapping the invoice header ([see screenshots](#expand-state)). Tapping toggles between expanded and collapsed states.
- **Empty JSON array:** Shows an empty invoice screen ([see screenshots](#empty-state)). Tapping the `Reload` button triggers a fetch operation, displaying a loading screen.
- **Invalid JSON:** Shows an error screen ([see screenshots](#error-state)). Tapping the `Retry` button attempts to fetch invoices again, updating the UI if successful. A loading screen is shown during the fetch.

### Loading State

| Light Theme | Dark Theme |
| :---------: | :--------: |
| ![Loading light](/assets/Loading-Light.png) | ![Loading dark](/assets/Loading-Dark.png) |

### Empty State

| Light Theme | Dark Theme |
| :---------: | :--------: |
| ![Empty light](/assets/Empty-Light.png) | ![Empty dark](/assets/Empty-Dark.png) |

### Error State

| Light Theme | Dark Theme |
| :---------: | :--------: |
| ![Error light](/assets/Error-Light.png) | ![Error dark](/assets/Error-Dark.png) |

### Collapse State

| Light Theme | Dark Theme |
| :---------: | :--------: |
| ![Collapse light](/assets/Collapse-Light.png) | ![Collapse dark](/assets/Collapse-Dark.png) |

### Expand State

| Light Theme | Dark Theme |
| :---------: | :--------: |
| ![Expand light](/assets/Expand-Light.png) | ![Expand dark](/assets/Expand-Dark.png) |

## Setup

This project runs in Android Studio (`Narwhal Feature Drop | 2025.1.2 Patch 2`). Clone the repository, open it in Android Studio, wait for Gradle sync, and run the app on an emulator or Android device using the `Run 'App'` button.

## Structure

The project uses an `interface`/`impl` design. The ViewModel invokes UseCases, which access the Repository to fetch data via HttpClient. Data transformations occur at each layer, ensuring explicit handling and easy adaptation to format changes.
This design enables flexibility and easy expansion. For example, the current `HttpClient` reads data from the app bundle, but it can be swapped for a real HTTP client to fetch data from a remote source. Since each layer transforms data explicitly, adding a transformer to adapt modified network responses to the existing UI format requires minimal effort.

If caching is needed, a new `CachedInvoiceRepository` can inherit from `InvoiceRepository` and replace `InvoiceRepositoryImpl` via dependency injection and feature gating. This repository can read from a local data source, return cached data if valid, or fetch and store remote data as needed. These changes can be made without modifying other layers of the codebase.

## Assumptions

- On launch, the app displays a loading screen using shimmer effects for a modern look.
- If empty data is received, an Empty screen appears. The "Reload" button triggers a simulated network request, showing the loading screen.
- If an error occurs, an Error screen is shown. The "Retry" button allows another fetch attempt, with a loading screen for feedback.
- When invoices are loaded, line items are hidden by default and can be expanded by tapping the invoice header.
- Currency values are shown in local currency without localization.
- Dates and times are displayed in the device's local time zone.

## Libraries

- **DaggerHilt:** Dependency injection
- **turbine:** Coroutine flow testing
- **mockk:** Interface mocking for unit tests
- **kotlinx-serialization-json:** JSON parsing

## Test Coverage

Unit tests cover core business logic, including `InvoiceRepository`, `InvoicesViewModel`, and use cases (`ExpandInvoiceUseCase`, `FetchInvoicesUseCase`, `CollapseInvoiceUseCase`).

## Future Work Suggestions

1. Add UI test coverage (e.g., [Robolectric](https://robolectric.org/#kotlin) for Compose layout testing).
2. Enhance UX:
    - Search invoices
    - Sort by date (ascending/descending)
    - Sort by total cost (ascending/descending)
    - Locale settings for currency display (with mocked exchange rates)
3. Support landscape mode and different device sizes (foldables, tablets)
4. Implement local data caching to reduce remote fetches
5. Add debounce logic for expanding/collapsing invoice sections to optimize performance
6. Ensure unique IDs for invoices and line items