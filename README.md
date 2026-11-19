# MyCycleTEST

A Kotlin Multiplatform cycling application targeting Android, iOS, Web, Desktop (JVM), and Server.

## Project Structure

- **`/app/`** — Application modules for each platform
  - `iosApp` — iOS application entry point (also contains SwiftUI code)
  - `shared` — Shared Compose Multiplatform UI code
    - `commonMain` — Code common across all targets
    - Platform-specific folders (iosMain, jvmMain, etc.)
  - `androidApp` — Android application

- **`/core/`** — Shared code between all targets
  - `commonMain` — Core Kotlin code shared everywhere
  - Platform-specific folders available if needed

- **`/server/`** — Ktor server application

## Running the Apps

### Desktop (JVM)
- Hot reload: `./gradlew :app:desktopApp:hotRun --auto`
- Standard run: `./gradlew :app:desktopApp:run`

### Android
- Assemble debug: `./gradlew :app:androidApp:assembleDebug`

### Server
- Run Ktor server: `./gradlew :server:run`

### Web
- Wasm target (modern browsers): `./gradlew :app:webApp:wasmJsBrowserDevelopmentRun`
- JS target (older browsers): `./gradlew :app:webApp:jsBrowserDevelopmentRun`

### iOS
- Open `/app/iosApp` in Xcode and run from there

## Running Tests

- Android tests: `./gradlew :app:shared:testAndroidHostTest`
- Desktop tests: `./gradlew :app:shared:jvmTest`
- Server tests: `./gradlew :server:test`
- Web tests (Wasm): `./gradlew :app:shared:wasmJsTest`
- Web tests (JS): `./gradlew :app:shared:jsTest`
- iOS tests: `./gradlew :app:shared:iosSimulatorArm64Test`

## Learn More

- [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
- [Compose Multiplatform](https://kotlinlang.org/compose-multiplatform/)
- [Kotlin/Wasm](https://kotl.in/wasm/)

## Feedback

We appreciate your feedback on Compose/Web and Kotlin/Wasm in the public Slack channel **[#compose-web](https://slack-chats.kotlinlang.org/c/compose-web)**.

Report issues on [YouTrack](https://youtrack.jetbrains.com/newIssue?project=CMP).