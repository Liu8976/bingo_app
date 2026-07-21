# AGENTS.md

## Project

Bingo is an Android app built with Kotlin and Jetpack Compose.

Run commands from the repository root. Do not assume the repository is checked out to a fixed drive or directory.

Important paths:

- App source: `app/src/main/java/com/bingo/app`
- Android resources: `app/src/main/res`
- Character and UI bitmap assets: `app/src/main/res/drawable-nodpi`
- Design references: `design_refs`

## Global Safety Rules

These rules apply to all work in this repository.

### Default execution strategy

When the user gives a task that can be completed from repository context, proceed directly instead of asking for routine technical choices.

Default to the smallest reversible change that matches the existing Android, Kotlin, and Jetpack Compose patterns in this project.

Before editing, quickly inspect the relevant source, resources, and Gradle files. Prefer existing app structure, naming, theme values, mock data patterns, composables, and resource usage over introducing new conventions.

For ordinary implementation details, Codex should decide from context:

- Use Kotlin and Jetpack Compose for UI work.
- Keep app code under `app/src/main/java/com/bingo/app`.
- Put reusable UI helpers in the existing `ui` package when that matches nearby code.
- Put screen-level UI in `ui/screen`.
- Put pure calculation or state-selection logic in `logic`.
- Put simple demo data in `mock`.
- Put app models in `model`.
- Use existing theme colors from `ui/theme` before adding new color constants.
- Use Android resources from `app/src/main/res` and keep resource names ASCII lowercase with underscores.
- Prefer `drawable-nodpi` for large bitmap character/UI assets already designed at fixed visual sizes.
- Keep changes local to the requested feature or bug.
- Avoid broad refactors, dependency changes, package moves, and mass formatting.
- If multiple normal implementation approaches are possible, choose the one that changes the fewest files and best matches surrounding code.

Fix root causes when practical, but do not expand scope into unrelated cleanup. If a localized workaround is safer than a larger redesign, use the localized fix and mention the tradeoff.

### Question boundary

Do not ask the user about routine implementation choices that can be inferred from this project.

Ask before continuing only when the decision has meaningful product, data, build, release, or destructive consequences.

Pause and ask the user before:

- Deleting, moving, or renaming files.
- Replacing user-provided assets or generated design references.
- Changing Gradle files, plugin versions, dependency versions, SDK versions, signing settings, package names, application IDs, CI, or release configuration.
- Adding a new library, framework, database, backend service, analytics SDK, ad SDK, login provider, payment integration, or network API.
- Changing app permissions, manifest exported components, deep links, or install/runtime behavior.
- Changing navigation structure, core product flow, data model meaning, scoring rules, fitness calculation behavior, or user-visible copy in a way that changes product intent.
- Wiping emulator data, uninstalling the app from a device, clearing app storage, or running commands with external side effects.
- Publishing, pushing, opening a pull request, uploading artifacts, or using external services.
- Making a broad refactor or project structure change.
- Proceeding when requirements conflict and code context cannot resolve the conflict.

If a question is needed, ask the smallest possible question and explain what is blocked.

### File deletion safety

Before deleting any folder, multiple files, generated assets, build output, cache folder, dependency folder, or version-controlled file, stop and ask the user for confirmation.

Do not delete user-created source files, assets, scenes, prefabs, project settings, `.meta` files, or configuration files unless the user explicitly asks.

Do not delete files or folders recursively.

Never use destructive recursive deletion commands, including but not limited to:

- `del /s`
- `rd /s`
- `rmdir /s`
- `Remove-Item -Recurse`
- `rm -rf`
- `git clean -fd`
- `git reset --hard`

If a file must be deleted, delete only one explicitly named file at a time.

Correct example:

```powershell
Remove-Item -LiteralPath "path\to\file.txt"
```

### Error handling

Do not repeat the same failed fix blindly.

Whenever the same error appears twice:

1. Stop repeating the same failed command or fix.
2. Re-read the full error message, stack trace, terminal output, and related source code.
3. Identify the exact file, line, command, package, configuration, or dependency involved.
4. Search the existing project first to understand how this problem is handled elsewhere.
5. Research official documentation or reliable web sources for 3-5 possible fixes.
6. Compare the fixes by compatibility, risk, implementation cost, and long-term maintainability.
7. Choose the most efficient and lowest-risk solution.
8. Apply the smallest necessary change.
9. Explain what changed, why it changed, and how to verify it.

Do not hide unresolved errors. If the issue cannot be fully verified locally, clearly state what was checked and what still needs user validation.

### Change scope

Prefer minimal, reversible changes.

Do not perform broad refactors, dependency upgrades, package migrations, project structure changes, mass formatting, or build configuration changes unless the user explicitly requests them.

Before changing build configuration, package versions, Android settings, CI files, or signing-related files, explain the risk and ask for confirmation.

### Verification and running

After making changes, do not automatically build, test, run the app, start the emulator, or install the APK.

Only build, test, run, start the emulator, or install the APK when the user explicitly asks for that verification step.

### Response requirements

After making changes, summarize:

- Files changed
- What changed
- Why it changed
- Risks or assumptions
- How to verify the result

## Android Workflow

Use a JDK compatible with the Gradle version in this project. Prefer the active `JAVA_HOME` if it points to JDK 17 or newer. If `JAVA_HOME` is not set, use the JDK bundled with the local Android Studio installation or another locally installed JDK 17+.

```powershell
.\gradlew.bat assembleDebug "-Dkotlin.compiler.execution.strategy=in-process"
```

The Kotlin daemon may report an `AccessDeniedException` under the current user's local Kotlin daemon directory. If Gradle falls back and the build finishes with `BUILD SUCCESSFUL`, treat it as a non-blocking local daemon permission issue.

To run on the emulator, read the Android SDK path from `local.properties` instead of hardcoding a machine-specific SDK directory:

```powershell
$sdkDir = (Get-Content -LiteralPath local.properties |
    Where-Object { $_ -like 'sdk.dir=*' } |
    Select-Object -First 1).Substring(8).Replace('\:', ':').Replace('\\', '\')
```

The development emulator is expected to be named `Pixel_10` on both company and home machines. Its adb serial is expected to be `emulator-5554`.

Start the emulator if it is not already running:

```powershell
& "$sdkDir\emulator\emulator.exe" -avd Pixel_10
```

Useful commands:

```powershell
$adb = "$sdkDir\platform-tools\adb.exe"
$serial = "emulator-5554"
& $adb devices -l
& $adb -s $serial install -r "app\build\outputs\apk\debug\app-debug.apk"
& $adb -s $serial shell am start -n com.bingo.app/.MainActivity
```

If adb reports `device offline` repeatedly, first try low-risk reconnect steps:

```powershell
& $adb kill-server
& $adb start-server
& $adb reconnect offline
```

Do not wipe emulator data unless the user explicitly approves.

## Resource Naming

Android resource references should use ASCII lowercase names with underscores.

If the user provides Chinese-named PNG assets, keep the original files if they are useful outside Android, but add Android-safe resource copies such as:

- `today_battle_title.png`
- `fat_health.png`
- `muscle_growth.png`
- `today_sport.png`
- `today_burn.png`

Avoid deleting the original user-provided assets unless explicitly requested.

## GitHub Notes

Remote repository:

```text
https://github.com/Liu8976/bingo_app.git
```

If normal `git push` over HTTPS is reset by the network and SSH has no configured key, use the existing GitHub credential only after confirming the normal push path failed. Prefer normal git commands whenever possible.

Do not commit local build/cache files. These should stay ignored:

- `.gradle/`
- `.kotlin/`
- `build/`
- `app/build/`
- `local.properties`
