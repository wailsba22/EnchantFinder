# Enchant Filter

[![GitHub stars](https://img.shields.io/github/stars/wailsba22/EnchantFinder?style=flat-square&logo=github)](https://github.com/wailsba22/EnchantFinder/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/wailsba22/EnchantFinder?style=flat-square&logo=github)](https://github.com/wailsba22/EnchantFinder/network/members)
[![GitHub issues](https://img.shields.io/github/issues/wailsba22/EnchantFinder?style=flat-square&logo=github)](https://github.com/wailsba22/EnchantFinder/issues)
[![Latest release](https://img.shields.io/github/v/release/wailsba22/EnchantFinder?style=flat-square&logo=github)](https://github.com/wailsba22/EnchantFinder/releases/latest)
[![Downloads](https://img.shields.io/github/downloads/wailsba22/EnchantFinder/total?style=flat-square&logo=github)](https://github.com/wailsba22/EnchantFinder/releases)
[![Release build](https://img.shields.io/github/actions/workflow/status/wailsba22/EnchantFinder/release.yml?style=flat-square&label=release%20build)](https://github.com/wailsba22/EnchantFinder/actions/workflows/release.yml)

![Minecraft](https://img.shields.io/badge/Minecraft-26.1.2-5D8C3D?style=for-the-badge)
![Fabric](https://img.shields.io/badge/Fabric-client--side-DDBB77?style=for-the-badge)
![Java](https://img.shields.io/badge/Java-25%2B-orange?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)

**Enchant Filter** is a lightweight, client-side Fabric mod that makes enchanted-item
sorting fast and visual. Select enchantments and levels in an in-game panel, then
matching items are highlighted directly in inventory screens.

## Features

- **In-game filter panel** opened with `J` by default.
- **Multiple enchantment levels** for precise filtering.
- **Match Any** mode for broad searches.
- **Match All** mode when every selected enchantment must be present.
- **Exact Match** mode for matching the complete enchantment set and levels.
- **Green item highlighting** for matching stacks.
- Works with player inventories, chests, shulker boxes, anvils, and other
  container screens.
- Drag the panel, scroll through enchantments, and adjust visible rows with
  `Ctrl` + mouse wheel.
- Fully client-side: no server installation is required.

## Compatibility

| Component | Supported version |
| --- | --- |
| Minecraft | **26.1.2 only** |
| Mod loader | Fabric Loader 0.19.5 or newer |
| Fabric API | 0.155.3+26.1.2 or a compatible 26.1.2 build |
| Java | 25 or newer |

> **Important:** This release targets Minecraft **26.1.2** specifically. It is
> not a Minecraft 1.20.1 mod and should not be installed on 26.2 or another
> Minecraft version unless a compatible build is released.

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 26.1.2.
2. Install the matching [Fabric API](https://modrinth.com/mod/fabric-api) build.
3. Download the latest `enchantfilter-*.jar` from
   [Releases](https://github.com/wailsba22/EnchantFinder/releases).
4. Put the JAR in your Minecraft `mods` folder.
5. Start the Fabric profile and open an inventory.
6. Press `J` to open the filter panel.

## Usage

- Click an enchantment to select it and cycle through its levels.
- Right-click an enchantment to clear its selection.
- Click **MATCH ANY** / **MATCH ALL** to change how multiple selections work.
- Click **Exact: ON/OFF** to require an exact enchantment set and level match.
- Drag the panel by its title.
- Scroll while the pointer is over the panel to browse enchantments.
- Hold `Ctrl` while scrolling to change the number of visible rows.
- Change the key binding in Minecraft's **Options → Controls** menu.

## Building from source

### Requirements

- JDK 25 or newer
- Git

Clone the repository and build the distributable JAR:

```bash
git clone https://github.com/wailsba22/EnchantFinder.git
cd EnchantFinder
./gradlew build
```

On Windows PowerShell:

```powershell
git clone https://github.com/wailsba22/EnchantFinder.git
Set-Location EnchantFinder
.\gradlew.bat build
```

Build outputs are written to `build/libs/`:

- `enchantfilter-<version>.jar` — installable mod
- `enchantfilter-<version>-sources.jar` — source archive

To run a development client:

```bash
./gradlew runClient
```

## Project layout

```text
src/main/java/com/example/enchantfilter/
├── EnchantFilterClient.java       # Client entry point and input hooks
├── EnchantFilterPanel.java        # Panel rendering and interaction
├── EnchantFilterState.java        # Selected filters and panel state
├── EnchantFilterMatcher.java      # Item matching logic
└── mixin/HandledScreenMixin.java  # Matching-item highlighting
```

## Contributing

Bug reports and improvement ideas are welcome. When opening an issue, include
the Minecraft version, Fabric Loader version, Fabric API version, Java version,
and a log or reproducible description where possible.

## License

Enchant Filter is released under the [MIT License](LICENSE).
