# MC-AutomaticPackage

A lightweight **Fabric** mod for **Minecraft Java Edition 1.21+** that lets you toggle any resource pack on or off with a single keybind — no settings menu required.

---

## Features

- **Instant toggle** — enable or disable a resource pack with one key press.
- **Configurable keybind** — remap the toggle key in Minecraft's controls menu.
- **User-configurable target pack** — change the resource pack via a JSON config file; no recompilation needed.
- **Automatic resource reload** — the game reloads resources immediately after toggling.
- **Action-bar feedback** — on-screen confirmation when a pack is enabled, disabled, or not found.
- **19 languages supported** — fully localized keybind names and in-game messages.
- **Clean & extensible** — well-structured codebase ready for contributions.

## Supported Minecraft Versions

| Minecraft   | Status       |
|-------------|--------------|
| 1.21.x      | ✅ Supported |
| 1.22+       | ⚠️ May work — untested |

> The mod targets Minecraft 1.21+ and uses only stable Fabric APIs. Future compatibility depends on upstream API changes.

## Requirements

- **Minecraft Java Edition** 1.21 or later
- **Fabric Loader** 0.16.0 or later
- **Fabric API**

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/installer/).
2. Download the latest **Fabric API** from [Modrinth](https://modrinth.com/mod/fabric-api) or [CurseForge](https://www.curseforge.com/minecraft/mc-mods/fabric-api) and place it in your `mods` folder.
3. Download the **MC-AutomaticPackage** JAR from [Releases](https://github.com/HoujouSxnnyside/MC-AutomaticPackage/releases).
4. Place the JAR in `.minecraft/mods/`.
5. Launch Minecraft with the Fabric profile.

## Usage

1. Place your desired resource pack (e.g., `xray.zip`) in `.minecraft/resourcepacks/`.
2. Edit the configuration file to specify the resource pack ID (see [Configuration](#configuration)).
3. Launch Minecraft.
4. Press the toggle key (default: **V**) to enable or disable the resource pack.
5. You can remap the keybind in **Options → Controls → MC-AutomaticPackage**.

## Configuration

The configuration file is created automatically on first launch at:

```
.minecraft/config/automaticpackage.json
```

### Default configuration

```json
{
  "resourcePackId": "file/xray.zip"
}
```

### Fields

| Field            | Type   | Description |
|------------------|--------|-------------|
| `resourcePackId` | String | The internal ID of the resource pack to toggle. |

### Finding the resource pack ID

Resource packs in the `resourcepacks` folder follow this ID format:

| File in `resourcepacks/`   | Resource Pack ID        |
|-----------------------------|-------------------------|
| `xray.zip`                  | `file/xray.zip`         |
| `my_textures.zip`           | `file/my_textures.zip`  |
| `custom_pack` (folder)      | `file/custom_pack`      |

### Changing the target pack

1. Close Minecraft (or the config will be overwritten).
2. Open `.minecraft/config/automaticpackage.json` in a text editor.
3. Set `resourcePackId` to the correct ID for your resource pack.
4. Save the file and relaunch Minecraft.

## Building from Source

```bash
# Clone the repository
git clone https://github.com/HoujouSxnnyside/MC-AutomaticPackage.git
cd MC-AutomaticPackage

# Generate the Gradle wrapper (if not included)
gradle wrapper --gradle-version 8.10.2

# Build the mod
./gradlew build
```

The compiled JAR will be in `build/libs/`.

> **Note:** Verify the exact dependency versions (Yarn mappings, Fabric API, etc.) from [Fabric's development page](https://fabricmc.net/develop/) if building for a specific Minecraft version.

## Localization

The mod is localized in the following languages:

`en_us` `es_es` `es_mx` `fr_fr` `de_de` `pt_br` `ru_ru` `ja_jp` `zh_cn` `zh_tw` `ko_kr` `it_it` `pl_pl` `tr_tr` `nl_nl` `sv_se` `cs_cz` `uk_ua` `ar_sa`

Keybind names, category labels, and in-game messages are translated. Documentation is provided in English only.

## Disclaimer

This mod is a **convenience utility** for toggling resource packs. Some resource packs (e.g., XRay) may violate the rules of multiplayer servers. **Always respect the rules of the server you are playing on.** The authors of this mod are not responsible for any consequences arising from its use, including but not limited to bans or disciplinary actions imposed by server administrators.

**Use responsibly and at your own risk.**

## License

This project is licensed under the [MIT License](LICENSE).

## Contact

For support or inquiries: **support.sxnnyside@sxnnysideproject.com**

GitHub: [https://github.com/HoujouSxnnyside/MC-AutomaticPackage](https://github.com/HoujouSxnnyside/MC-AutomaticPackage)
