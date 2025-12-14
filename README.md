# PrivateBank Manager

JavaFX application that models a private bank with multiple accounts, interest-bearing payments, and transfers that are persisted as JSON files. The UI lets you browse accounts, inspect transactions, and create or delete entries while sharing the same domain model as the back end.

## Features
- PrivateBank domain model with payments, incoming/outgoing transfers, and validation through custom exceptions.
- JSON persistence under `data_json_app/` with automatic loading and saving per account.
- JavaFX UI that lists accounts, shows transaction details, and provides dialogs for adding new entries.
- Sorting and filtering options (ascending, descending, income-only, expense-only) for every account view.
- Maven-based build with JUnit 5 tests for the banking logic.

## Tech Stack
- Java 17
- JavaFX 22 (controls, FXML)
- Google Gson 2.11 for JSON (de-)serialization
- Maven (compiler, surefire, javafx-maven-plugin)

## Project Layout
```
src/
  main/java/
    bank/            # Domain layer (accounts, transactions, persistence)
    ui/              # JavaFX application, controllers, dialogs
  main/resources/    # FXML files for the scenes
  test/java/         # Unit tests
 data_json_app/      # Runtime account data
 data_json_test/     # Sample JSON for tests
```

## Getting Started
1. **Install prerequisites**
   - JDK 17+
   - Maven 3.9+
   - JavaFX runtime (pulled automatically via Maven dependencies)
2. **Clone and build**
   ```bash
   mvn clean verify
   ```

### Run the JavaFX UI
```bash
mvn javafx:run
```
The launcher (`ui.FxApplication`) boots a `PrivateBank` instance pointing to `data_json_app/` and opens the main view.

### Run Tests
```bash
mvn test
```
Unit tests live under `src/test/java` (e.g., `PaymentTest`, `TransferTest`).

## Working with Data
- `data_json_app/` stores one JSON file per account (`Konto_<Name>.json`).
- `PrivateBank` loads existing files at startup and keeps them in sync after any change.
- `data_json_test/` contains example payloads you can use during development.

## UI Usage Tips
- Double-click an account or use the context menu to open it.
- Use the combo box in the account view to sort or filter transactions.
- Click **+ Transaction** to open the dialog; choose between payments or transfers. Sender/recipient fields activate automatically for transfers.
- Right-click a transaction to remove it after confirmation.

## Troubleshooting
- If account files are missing, ensure the `data_json_app/` directory exists (the application creates it on demand).
- JavaFX runtime errors usually stem from missing OpenGL support on headless systems; prefer running locally with a desktop session.

Feel free to adapt the interest rates, data directory, or UI texts inside `ui.FxApplication` for your environment.
