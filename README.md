# Student Organizer App

Android app built with Jetpack Compose and Navigation Compose for managing courses, assignments, student profile, and app preferences.

## Screens
- **Dashboard** (`dashboard`) – Student greeting, quick stats, horizontal course list, urgent deadlines, and `+` FAB.
- **Assignments** (`assignments`) – Filterable assignment list (`All`, `Pending`, `Completed`) with completion checkboxes and priority badges.
- **Profile** (`profile`) – Student profile card, academic stats (GPA, completed credits, semester), and quick actions.
- **Settings** (`settings`) – Dark mode toggle, push notification preference, and default start screen selection.
- **Add Assignment** (`add_assignment`) – Validated form for creating a new assignment (title, course dropdown, due date picker, priority, notes, reminder).

## Demo
See [demo.mp4](./demo.mp4) for a walkthrough of all 5 screens.

## Build & Run
```bash
./gradlew assembleDebug
```
Or open the project in Android Studio and run on an emulator or Android device.
