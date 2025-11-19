# Step 4 Report

## Testing strategy
The project uses a combination of unit tests and UI tests to verify core functionality.a

- Unit test(JUnit + coroutines): We test small, data focused logic like mapping backend models to domain models and formatting timestamps into readable dates. These tests run without Android or real network calls, using fake APIs to catch regressions in data handling.
- UI test(Espresso): We testd key flows such as opening the “Add expense” dialog, filling it in, and submitting it. The tests check that the right UI elements appear and that user actions lead to the expected state changes.
## Build process for the APK
The build process for the Fairshare app was very straight forward. The app is built and signed using Android Studio.

First step was creating the keystore, to sign the APK. This was easily done in Android studio

After creating the keystore, I was able to generate a signed APK through android studio. The signing was done using the keystore created in the previous step.

## Known bugs and limitations
- There is a small time window between the splash screen and home screen loading, where a white screen can be seen.
- Currently the expenses are divided between all of the group members equally.