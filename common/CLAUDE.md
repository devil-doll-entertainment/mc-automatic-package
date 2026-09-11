# Module: :common

## Overview
- **Path:** `common/`
- **Role:** Core mod logic, GUI presentation, client commands, configuration storage, and resource pack state controller.
- **Dependencies:** Minecraft (via Mojang Official Mappings). Loader-agnostic.

## Architectural Constraints
- **Strict Loader Isolation:** Never import `net.fabricmc.*` or `net.neoforged.*` classes here.
- **Service Loader Pattern:** Platform-dependent operations must be accessed strictly through `com.sxnnyside.autopack.platform.PlatformHelper` via `Services.PLATFORM`.
- **Dynamic Identification:** Never hardcode pack paths or pack file IDs. All pack IDs are acquired dynamically via Minecraft's `PackRepository`.

## Submodule Commands
```bash
./gradlew :common:compileJava
./gradlew :common:test
./gradlew :common:spotlessCheck
./gradlew :common:spotlessApply
```
