# Block Hunt

A modern, lightweight **Block Hunt (Prop Hunt)** minigame for **PaperMC** servers.

Players are divided into **Hiders** and **Hunters**. Hiders disguise themselves as blocks and blend into the map, while Hunters must search the arena and eliminate every hidden player before time runs out.

> **Note:** This project was created as a portfolio piece to demonstrate my Java and PaperMC plugin development skills. While the core gameplay is fully functional through commands, some planned features and polish are still in development.

---

## Features

- Block disguises
- Solidification system
- Hunter vs Hider gameplay
- Spectator support
- Automatic lobby countdown
- Random hunter selection
- Multi-arena support
- Configurable arena spawns
- Join NPC support
- Win condition system
- Player statistics
- Automatic arena saving
- Lightweight and optimized

---

# Gameplay

## Hiders

- Choose a block disguise.
- Hide around the map.
- Stay still to solidify into a block.
- Survive until the timer expires.

## Hunters

- Randomly selected each game.
- Frozen during the hiding phase.
- Released when the countdown ends.
- Find and eliminate every hider.

---

# Commands

## Arena Commands

| Command | Description |
|----------|-------------|
| `/blockhunt create name` | Create an arena |
| `/blockhunt delete arena` | Delete an arena |
| `/blockhunt list` | List all arenas |
| `/blockhunt join arena` | Join an arena |
| `/blockhunt leave` | Leave the current arena |
| `/blockhunt start arena` | Force start a game |

## Arena Setup

| Command | Description |
|----------|-------------|
| `/blockhunt setlobby arena` | Set the lobby spawn |
| `/blockhunt sethiderspawn arena` | Set the hider spawn |
| `/blockhunt sethunterspawn arena` | Set the hunter spawn |

## NPC Commands

| Command | Description |
|----------|-------------|
| `/blockhunt spawnnpc` | Spawn a join NPC |

## Debug Commands

| Command | Description |
|----------|-------------|
| `/blockhunt disguise block` | Disguise as a block |
| `/blockhunt undisguise` | Remove your disguise |
| `/blockhunt solidify` | Force solidify |
| `/blockhunt unsolidify` | Cancel solidification |
| `/blockhunt role role` | Change your role |
| `/blockhunt stats` | View player statistics |

---

# Arena Setup

### 1. Create an arena

```bash
/blockhunt create Village
```

### 2. Set the lobby spawn

```bash
/blockhunt setlobby Village
```

### 3. Set the hider spawn

```bash
/blockhunt sethiderspawn Village
```

### 4. Set the hunter spawn

```bash
/blockhunt sethunterspawn Village
```

### 5. Spawn the join NPC

```bash
/blockhunt spawnnpc Village
```

### 6. Join the arena

```bash
/blockhunt join Village
```

---

# Game Flow

```text
Players Join
      │
      ▼
Lobby Countdown
      │
      ▼
Hunter Selected
      │
      ▼
Hiding Phase
      │
      ▼
Hunters Released
      │
      ▼
Seeking Phase
      │
      ▼
Hunters Win / Hiders Win
      │
      ▼
Arena Reset
```

---

# Project Structure

```text
me.involuting.blockhunt
├── command
├── game
│   ├── arena
│   ├── disguise
│   ├── npc
│   ├── player
│   ├── role
│   ├── session
│   ├── state
│   ├── taunts
│   ├── win
│   └── engine
├── listeners
├── menu
├── scoreboard
├── util
└── BlockHunt.java
```

---

# Requirements

- Java 21+
- Paper 1.21+
- Maven

---

# About This Project

This plugin was built as a portfolio project to demonstrate:

- Java application architecture
- Object-oriented design
- PaperMC plugin development
- Event-driven programming
- Game state management
- Clean code practices

The project is still under active development, and additional gameplay features and improvements are planned.

---

# Contributing

Contributions, suggestions, and pull requests are welcome.

If you discover a bug or have an idea for an improvement, feel free to open an issue.

---

# License

This project is licensed under the MIT License.
