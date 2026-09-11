# MC-AutomaticPackage

![Version](https://img.shields.io/badge/version-1.1.0-blue)
![License](https://img.shields.io/badge/License-MIT-green)
[![CI](https://github.com/devil-doll-entertainment/mc-automatic-package/workflows/CI%20Quality%20Gate/badge.svg)](https://github.com/devil-doll-entertainment/mc-automatic-package/actions)

<p align="center">
  <strong>Instant Toggle ✦ In-Game GUI Selector ✦ Fabric & NeoForge</strong><br>
  <em>Toggle and configure any Minecraft resource pack on the fly without restarting the game or navigating menus.</em>
</p>

<p align="center">
  <a href="#about">About</a> ✦
  <a href="#features">Features</a> ✦
  <a href="#installation">Installation</a> ✦
  <a href="#usage">Usage</a> ✦
  <a href="#architecture">Architecture</a> ✦
  <a href="#contributing">Contributing</a>
</p>

---

## About

**MC-AutomaticPackage** is a lightweight, multi-loader client utility mod for Minecraft Java Edition.

Switching resource packs in vanilla Minecraft disrupts gameplay by requiring users to pause the game, navigate nested options menus, reorder packs, and wait through lengthy reload screens. MC-AutomaticPackage removes that friction entirely.

Press a single keybind or use the in-game search screen to enable or disable your target resource pack immediately with zero manual configuration file editing.

### Philosophy

> *"Seamless texture switching belongs in the flow of the game, not buried in settings menus."*

This is a Devil Doll Entertainment project, part of the Sxnnyside Project's ecosystem.

## Features

- **Instant Toggle Keybind**: Switch your target pack on or off with a single keypress (Default: **V**).
- **In-Game GUI Selector**: Live-search, browse, toggle, and set your quick-toggle pack visually (Default: **G**).
- **Multi-Loader Support**: Native support for both **Fabric** and **NeoForge** on Minecraft 1.21.x.
- **Client Commands**: Manage packs directly from chat via `/autopack` (`gui`, `toggle`, `status`, `select`).
- **Reload Protection**: Anti-spam debounce logic prevents game freezes and crashes from rapid key presses.
- **Priority Stack Ordering**: Toggled packs are placed at the top of the active stack for immediate visual precedence.
- **ModMenu & Config Integration**: Integrated config screen factories for Fabric ModMenu and NeoForge mod lists.
- **19 Languages Supported**: Fully localized interface, messages, and keybindings.

## Installation

### Prerequisites

- Minecraft Java Edition (1.21.x / 1.21.1)
- Fabric Loader (>= 0.16.0) or NeoForge (>= 21.1.0)
- Java Runtime Environment (>= 21)

### From Source

```bash
git clone https://github.com/devil-doll-entertainment/mc-automatic-package.git
cd mc-automatic-package

# Build production JARs for all loaders
just build
```

The compiled mod JARs will be located in:
- Fabric: `fabric/build/libs/automaticpackage-fabric-1.1.0.jar`
- NeoForge: `neoforge/build/libs/automaticpackage-neoforge-1.1.0.jar`

## Usage

```bash
# Default Controls
Press V           # Quick-toggle configured resource pack
Press G           # Open in-game Resource Pack Selector GUI

# In-Game Commands
/autopack gui             # Open the selector interface
/autopack toggle          # Toggle the current pack
/autopack status          # Check configured pack and active state
/autopack select <pack>   # Set target pack with autocompletion
```

## Architecture

```
mc-automatic-package/
├── common/      # Core logic, GUI, commands, and loader-agnostic controllers
├── fabric/      # Fabric Loader entrypoint, Fabric API hooks, and ModMenu integration
└── neoforge/    # NeoForge Loader entrypoint, event bus listeners, and mod menu setup
```

For a detailed breakdown, see [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md).

## Contributing

Contributions are accepted. See [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

Before contributing, read the [Code of Conduct](CODE_OF_CONDUCT.md).

## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

---

<p align="center">
  <strong>MC-AutomaticPackage</strong> — A Devil Doll Entertainment Release<br>
  <em>&copy; 2024 Sxnnyside Project</em>
</p>
