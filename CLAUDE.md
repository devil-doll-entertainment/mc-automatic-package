# CLAUDE.md

## Repository Overview

- **Project:** MC-AutomaticPackage
- **Organization:** Devil Doll Entertainment
- **Repository:** https://github.com/devil-doll-entertainment/mc-automatic-package
- **Maintainer:** @HoujouSxnnyside
- **Description:** Lightweight client mod for Minecraft Java Edition 1.21+ (Fabric & NeoForge) enabling in-game resource pack searching, quick-toggling via keybinds, and command control.
- **Topology:** Non-Monolithic (Gradle Multi-Module Workspace)
- **Toolchain:** Java 21, Gradle 9.3.1 (via Gradle Wrapper `./gradlew`), Just task runner


---

## Module Map

| Module | Path | Purpose |
|---|---|---|
| `:common` | `common/` | Core logic, `ResourcePackController`, `ModConfig`, `PackSelectorScreen`, `AutoPackCommand`, and language assets. Loader-agnostic. |
| `:fabric` | `fabric/` | Fabric entrypoint (`AutomaticPackageFabric`), Fabric API keybindings, tick events, client commands, and `FabricPlatformHelper`. |
| `:neoforge` | `neoforge/` | NeoForge entrypoint (`AutomaticPackageNeoForge`), Mod Event Bus key mappings, Game Event Bus tick/commands, and `NeoForgePlatformHelper`. |

---

## Standard Command Surface (`just`)

All development actions are managed via `just`:

```bash
just install       # Verify toolchain and wrapper dependencies
just dev           # Launch Fabric development client
just dev-neoforge  # Launch NeoForge development client
just build         # Build production JARs for all loaders
just test          # Run automated JUnit 5 test suite
just typecheck     # Verify compiler correctness across all modules
just lint          # Check formatting and static analysis (Spotless)
just format        # Automatically format all code (Spotless/Palantir)
just check         # Full quality gate (format -> lint -> typecheck -> test -> build)
just clean         # Clean build outputs and caches
```

---

## Architecture & Design Patterns

1. **Platform Abstraction (`PlatformHelper`):**
   - Platform-dependent operations (e.g., config directory, mod loaded queries) are defined in `com.sxnnyside.autopack.platform.PlatformHelper`.
   - Implementations are loaded dynamically at runtime using Java's standard `ServiceLoader` via `Services.PLATFORM`.
   - Never import loader-specific classes (`FabricLoader`, `FMLPaths`, etc.) inside the `:common` module.

2. **Mappings Standard:**
   - Both Fabric and NeoForge use **Mojang official mappings** (`loom.officialMojangMappings()`).
   - Standard Mojang/Minecraft class names apply across `:common` (e.g., `Minecraft`, `PackRepository`, `Screen`, `KeyMapping`, `Component`).

3. **No Hardcoded Paths or Pack Names:**
   - No assumptions about resource pack filenames (e.g., never hardcode `xray.zip`).
   - The user selects packs dynamically in the GUI (`PackSelectorScreen`) or via `/autopack select <id>`.

4. **Formatting & Code Style:**
   - Enforced automatically via Spotless with Palantir Java Format.
   - Run `just format` prior to committing.

---

## Quality Gate Checklist

Before opening a pull request or creating a release:
1. Run `just check` — must pass cleanly with zero errors.
2. Verify that both JARs exist:
   - `fabric/build/libs/automaticpackage-fabric-<version>.jar`
   - `neoforge/build/libs/automaticpackage-neoforge-<version>.jar`
3. Ensure Conventional Commits (`feat:`, `fix:`, `docs:`, `chore:`, etc.) are used for commit messages.
