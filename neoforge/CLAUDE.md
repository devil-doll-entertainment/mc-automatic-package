# Module: :neoforge

## Overview
- **Path:** `neoforge/`
- **Role:** NeoForge loader adapter. Registers `@Mod("automaticpackage")` lifecycle, Mod Event Bus key mappings, and Game Event Bus client ticks.
- **Dependencies:** `:common`, `net.neoforged:neoforge`.

## Key Implementations
- `AutomaticPackageNeoForge`: NeoForge `@Mod` entrypoint and config screen factory registration.
- `NeoForgeClientEvents`: Handles `RegisterKeyMappingsEvent`, `ClientTickEvent.Post`, and `RegisterClientCommandsEvent`.
- `NeoForgePlatformHelper`: Implements `PlatformHelper` using `FMLPaths.CONFIGDIR`.
- `META-INF/neoforge.mods.toml`: NeoForge mod manifest.

## Submodule Commands
```bash
./gradlew :neoforge:compileJava
./gradlew :neoforge:assemble
./gradlew :neoforge:runClient
```
