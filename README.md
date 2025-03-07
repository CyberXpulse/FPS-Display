# Totem Swap Mod

![Totem of Undying](https://static.wikia.nocookie.net/minecraft_gamepedia/images/3/3a/Totem_of_Undying_JE3_BE2.png/revision/latest?cb=20191229235406)

Totem Swap Mod is a Minecraft Fabric mod that allows players to quickly swap a Totem of Undying to their offhand slot with a simple key press. This mod is designed to be efficient and user-friendly, ensuring that players can react quickly in dangerous situations without having to manually search their inventory.

## Features

- **Keybinding for Totem Swap**: Press the `T` key to automatically search your inventory for a Totem of Undying and place it in your offhand slot.
- **Optimized Inventory Management**: The mod sends the necessary packets to the server to register the inventory changes, ensuring smooth and quick swaps without causing auto-clicker detection issues.
- **Lightweight and Efficient**: Designed to run efficiently in the background without affecting game performance.

## Installation

1. **Download and Install Fabric Loader**:
   - Visit the [Fabric website](https://fabricmc.net/use/) and download the Fabric Loader installer.
   - Run the installer, select your Minecraft version, and click "Install".

2. **Download Fabric API**:
   - Download the [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api) and place it in your Minecraft `mods` folder.

3. **Download Totem Swap Mod**:
   - Download the latest release of Totem Swap Mod from the [Releases](https://github.com/CyberXpulse/totem-swap-mod/releases) page.
   - Place the downloaded JAR file in your Minecraft `mods` folder.

4. **Launch Minecraft**:
   - Open the Minecraft launcher, select the Fabric profile, and click "Play".

## Usage

1. **Start Minecraft**:
   - Launch Minecraft with the Fabric profile.

2. **In-Game Keybinding**:
   - By default, the totem swap keybinding is set to `T`. You can change this keybinding in the Minecraft controls settings.

3. **Swap Totem to Offhand**:
   - Press the `T` key to automatically move a Totem of Undying from your inventory to your offhand slot. The mod will search your entire inventory, including the hotbar, for a Totem of Undying.

## Technical Details

### Mod Structure

- **Mod Initialization**:
  - The mod initializes both on the client and server sides to ensure proper functionality.

- **Keybinding Registration**:
  - The `T` key is registered as the default keybinding for swapping the totem. This can be customized in the controls settings.

- **Inventory Management**:
  - The mod searches the player's inventory for a Totem of Undying and uses the `ClickSlotC2SPacket` to perform the swap. This ensures that the server is aware of the inventory changes and helps prevent auto-clicker detection issues.

## Contributing

We welcome contributions to enhance the Totem Swap Mod. To contribute:

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Commit your changes and push them to your fork.
4. Create a pull request to the main repository.

Please ensure your code follows the project's coding standards and includes appropriate tests.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

## Support

If you encounter any issues or have questions, please open an issue on the [GitHub repository](https://github.com/CyberXpulse/totem-swap-mod/issues) or contact us via the support page.

## Acknowledgments

- [FabricMC](https://fabricmc.net/) for providing the modding framework.
- [Yarn](https://github.com/FabricMC/yarn) for the Minecraft mappings.

Enjoy the Totem Swap Mod and stay safe in your Minecraft adventures!

```
