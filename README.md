# cookiecutter-cmp-template

Cookiecutter template for a bleeding edge Compose Multiplatform app targeting **Android**, **iOS** and **Desktop (JVM)**.

> [!TIP]
> Looking for an Android-only Jetpack Compose version? Check out [cookiecutter-compose-template](https://github.com/kroegerama/cookiecutter-compose-template).

## Prerequisites

- Git
- Python 3
- [Cookiecutter](https://cookiecutter.readthedocs.io/en/stable/installation.html) template engine
- macOS with Xcode (only for building the iOS app)

## Usage

```sh
cookiecutter gh:kroegerama/cookiecutter-cmp-template
```

You will be prompted for:

| Prompt             | Default                    | Notes                                                    |
|--------------------|----------------------------|----------------------------------------------------------|
| App name           | `My App`                   | Display name of the app                                  |
| Application ID     | `com.example.myapp`        | Lowercase, 3+ dot-separated segments                     |
| Kotlin namespace   | _(same as application ID)_ | Lowercase, 2+ dot-separated segments, no Kotlin keywords |
| Minimum SDK        | `29`                       | Android only, must be between 21 and 36                  |
| Target folder name | _(derived from app name)_  | Folder where the project is generated                    |

## What's Included

**UI**

- Compose Multiplatform + Material3 Expressive
- Material3 Adaptive (adaptive layouts + navigation suite)
- Navigation3 with [Scene Decorators](https://developer.android.com/guide/navigation/navigation-3/recipes/navscenedecorator)
- [Coil](https://coil-kt.github.io/coil/) (image loading)

**Dependency Injection**

- [Metro](https://zacsweers.github.io/metro/)

**Networking**

- [kmpgen](https://github.com/kroegerama/openapi-kmp-gen) - OpenAPI Kotlin client codegen
- [Ktor](https://ktor.io/)
- [Chucker](https://github.com/ChuckerTeam/chucker) (debug HTTP inspector on Android, no-op in release)

**Utilities**

- [Arrow](https://arrow-kt.io/) (functional programming)
- [kmp-kaiteki](https://github.com/kroegerama/kmp-kaiteki) (helper classes for modern Kotlin multiplatform projects)
- DataStore (Preferences)
- kotlinx.serialization + kotlinx.collections.immutable
- [Kermit](https://kermit.touchlab.co/) (multiplatform logging)

**Project structure**

```
<project_slug>/
├── androidApp/   # Android application
├── desktopApp/   # Desktop (JVM) application
├── iosApp/       # iOS application (Xcode project)
├── shared/       # Shared Compose UI, navigation and app logic
└── network/      # API client module incl. generated API client (kmpgen)
```

## Example

The [`example/`](https://github.com/kroegerama/cookiecutter-cmp-template/tree/main/example) directory contains a pre-generated project showing what
the template produces.
