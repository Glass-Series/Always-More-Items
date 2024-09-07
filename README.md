# Always More Items

A very rough backport of 1.8.9 [JEI](https://github.com/mezz/JustEnoughItems) to b1.7.3.  
Yes, this includes most of the API backend, though we don't have a fluids API in StAPI yet, so fluids are entirely unsupported for now.

Features over HMI-fabric (for users):
- A much less laggy UI.
- Proper support for StationAPI tooltips.
- Proper support for hiding items that you don't want to see. (WIP)
- Syncing recipes from server to client, meaning servers running custom recipes can nicely do so without additional client mods. (Requires AMI on server)
- **Overhauled tooltip rendering.**
    - Tooltips are the last thing rendered, meaning you can queue a tooltip at any point, and it should render, regardless of the current GUI (if any at all).
    - Tooltips are screen edge aware, unlike vanilla tooltips.
    - They can render modern formatting codes (italics, bold, etc).
    - They can render itemstacks, dividers, and even images.
    - Customisation and extension API coming soon:tm:.
- **A rarities system.**
    - This does nothing without a mod that adds them to items.
    - Can have fancy formatting, header colours, and icons.

Drawbacks compared to HMI-fabric:
- You need StationAPI regardless of if you want it or not. (Will be worked on, but you will lose everything aside from recipe-related features)

Features over every other inventory editor:
- Multiplayer NBT and metadata giving. (Requires AMI on server)
- Multiplayer healing. (Requires AMI on server)
- Multiplayer day/night.
- Recipe viewing.

Features over HMI-fabric (for devs):
- A sane API that allows you to almost entirely reuse your existing container and rendering code, removing the need for specialized containers and UIs.
- An entirely redone tooltip rendering system which supports modern colour and formatting codes, and also supports rarities (WIP on latter.)
- A ton of render helpers that make creating and editing GUIS *far* less painful.
- An item blacklist API, which can be updated during runtime for progression-locked items, if wanted.

TODO:
- Add tabs (see creative tabs)
- Implement hiding items through the item menu.
- Add the ability to format tooltips.
- Make this work without StationAPI.
  - This will be on a vastly reduced featureset.

## Setup (dev)

[See the StationAPI wiki.](https://github.com/ModificationStation/StationAPI/wiki)

## Common Issues (dev)

See [StAPI Example Mod](https://github.com/calmilamsy/stationapi-example-mod).

## License

See [LICENSE](LICENSE).
