# Changelog

All notable changes to **MC-AutomaticPackage** are documented here.

This project follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/)
and [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [Unreleased]

---

## [1.1.0] — 2026-09-11

### Added

- Multi-Loader Architecture: Full multi-module project structure separating `:common`, `:fabric`, and `:neoforge`.
- NeoForge Support: Native client support for Minecraft 1.21 / 1.21.1 on NeoForge.
- In-Game Resource Pack Selector GUI (`PackSelectorScreen`): Browse, real-time search, toggle, and target selection directly in Minecraft.
- In-Game Client Commands (`/autopack`): Subcommands `gui`, `toggle`, `status`, and `select` with autocompletion.
- Reload Debounce & Protection: Cooldown preventing game freezes from rapid toggle key spamming.
- Texture Stack Precedence: Toggled packs are placed at highest priority in the active pack list.
- ModMenu Integration: Native config screen entrypoint for Fabric ModMenu.
- Standard Command Surface (`Justfile`): Unified task runner abstraction layer.
- Automated Testing Suite: JUnit 5 unit tests for models, config, and state transitions.

### Changed

- Removed Hardcoded Pack Name: Eliminated the legacy hardcoded default `file/xray.zip`.
- Fixed Options Persistence: Succeeded user pack preservation in `options.txt` across client restarts.
- Display Name Formatting: Replaced raw file paths with formatted human-readable pack titles in messages.
- Mappings Standard: Migrated to official Mojang mappings across all modules.

---

## [1.0.0] — 2024-08-08

### Added

- Initial single-module release for Fabric (Minecraft 1.21.1).
- Basic keybind (default: V) to toggle a single pre-configured resource pack.
- JSON configuration at `.minecraft/config/automaticpackage.json`.
- Action-bar overlay feedback.
- 19 localized language files.

---

[Unreleased]: https://github.com/devil-doll-entertainment/mc-automatic-package/compare/v1.1.0...HEAD
[1.1.0]: https://github.com/devil-doll-entertainment/mc-automatic-package/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/devil-doll-entertainment/mc-automatic-package/releases/tag/v1.0.0
