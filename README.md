# CervixOnTop Android

Proyecto Android para empaquetar CervixOnTop con Node.js Mobile + Mineflayer.

## Compilación desde GitHub

El workflow `.github/workflows/build-apk.yml` descarga automáticamente el runtime Node.js Mobile ARM64, instala las dependencias de Mineflayer y genera `app-debug.apk`.

1. Sube este proyecto a un repositorio de GitHub.
2. Abre **Actions**.
3. Ejecuta **Build CervixOnTop APK** con **Run workflow**.
4. Cuando termine, abre la ejecución y descarga el artifact **CervixOnTop-debug**.

El APK está orientado a `arm64-v8a`.
