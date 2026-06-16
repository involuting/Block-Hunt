# Block Hunt

A modern, lightweight **Block Hunt / Prop Hunt** minigame for **PaperMC** servers.

Players are divided into **Hiders** and **Hunters**. Hiders disguise themselves as blocks and blend into the map, while Hunters search the arena and eliminate every hidden player before time runs out.

This is an plugin for my portfolio that works through commands and is an example of my work so it is not fully completed but works with commands

##  Features

-  Block disguises
-  Solidification system
-  Spectator support
-  Hunter vs Hider gameplay
-  Automatic lobby countdowns
-  Random hunter selection
-  Multi-arena support
-  Configurable arena spawns
-  Join NPC support
-  Win condition system
-  Player statistics
-  Lightweight & optimized
-  Automatic arena saving

---

##  Gameplay

###  Hiders
- Choose a block disguise.
- Hide around the map.
- Stay still to solidify.
- Survive until time runs out.

###  Hunters
- Randomly selected each game.
- Frozen during the hiding phase.
- Released when the timer ends.
- Find and eliminate all hiders.



##  Commands

### Arena Commands

| Command | Description |
|----------|-------------|
| `/blockhunt create name` | Create an arena |
| `/blockhunt delete arena` | Delete an arena |
| `/blockhunt list` | List all arenas |
| `/blockhunt join arena` | Join an arena |
| `/blockhunt leave` | Leave an arena |
| `/blockhunt start arena` | Force start a game |

### Arena Setup

| Command | Description |
|----------|-------------|
| `/blockhunt setlobby arena` | Set lobby spawn |
| `/blockhunt sethiderspawn arena` | Set hider spawn |
| `/blockhunt sethunterspawn arena` | Set hunter spawn |

### NPC Commands

| Command | Description |
|----------|-------------|
| `/blockhunt spawnnpc arena` | Spawn a join NPC |

### Debug Commands

| Command | Description |
|----------|-------------|
| `/blockhunt disguise block` | Disguise as a block |
| `/blockhunt undisguise` | Remove disguise |
| `/blockhunt solidify` | Force solidify |
| `/blockhunt unsolidify` | Force unsolidify |
| `/blockhunt role role` | Set role |
| `/blockhunt stats` | View stats |



##  Arena Setup

### 1. Create Arena

```bash
/blockhunt create Village
```

### 2. Set Lobby Spawn

```bash
/blockhunt setlobby Village
```

### 3. Set Hider Spawn

```bash
/blockhunt sethiderspawn Village
```

### 4. Set Hunter Spawn

```bash
/blockhunt sethunterspawn Village
```

### 5. Spawn NPC

```bash
/blockhunt spawnnpc Village
```

### 6. Join Arena

```bash
/blockhunt join Village
```



##  Game Flow

```
Players Join
      ↓
Lobby Countdown
      ↓
Hunter Selected
      ↓
Hiding Phase
      ↓
Hunters Released
      ↓
Seeking Phase
      ↓
Hunters Win / Hiders Win
      ↓
Arena Reset
```



## Project Structure

```text
me.involuting.blockhunt
├── command
├── config
├── game
│   ├── arena
│   ├── disguise
│   ├── manager
│   ├── npc
│   ├── player
│   ├── role
│   ├── state
│   ├── task
│   └── win
├── listeners
├── menu
├── util
└── BlockHunt.java

## Requirements

- Java 21+
- Paper 1.21+
- Maven


##  Contributing

Contributions, suggestions, and pull requests are welcome.

If you find a bug, open an issue and include as much information as possible.



##  License

This project is licensed under the MIT License.

