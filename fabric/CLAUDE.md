# Module: :fabric

## Overview
- **Path:** `fabric/`
- **Role:** Fabric loader adapter. Registers Fabric API client entrypoint, keybinding hooks, tick events, and ModMenu configuration screen.
- **Dependencies:** `:common`, `fabric-loader`, `fabric-api`.

## Key Implementations
- `AutomaticPackageFabric`: Implements `ClientModInitializer`.
- `FabricPlatformHelper`: Implements `PlatformHelper` using `FabricLoader.getInstance()`.
- `fabric.mod.json`: Fabric mod manifest with client entrypoint declarations.

## Submodule Commands
```bash
./gradlew :fabric:compileJava
./gradlew :fabric:assemble
./gradlew :fabric:runClient
```
