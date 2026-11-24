# Trebuchet Universal

An independent, FOSS Android home launcher for Android 11 and newer.

This is a clean-room, public-API implementation. It does not redistribute the
LineageOS system APK supplied as a reference, and it deliberately omits the
privileged Quickstep/recents integration that only works when a launcher is
installed as a signed system component.

## Build

Set `ANDROID_HOME` to an Android SDK containing platform 36, then run:

```powershell
gradle :app:assembleDebug
```

The resulting APK is `app/build/outputs/apk/debug/app-debug.apk`. Install it,
press Home, and select **Trebuchet Universal** as the default launcher.

## Compatibility

- `minSdk 30` (Android 11)
- target SDK 35
- uses only public Android APIs
- app drawer with launchable installed applications

Licensed under Apache-2.0.
LineageOS Trebuchet Launcher port for Android 9+
