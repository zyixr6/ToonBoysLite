# ToonBoysLite

**ToonBoysLite** is a 2D side-scrolling battle game developed using [libGDX](https://libgdx.com/), set up with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff) for a modular and maintainable project structure. The game features animated characters, interactive battle mechanics, stage-based progression, and custom audio-visual assets.

---

## Game Overview

ToonBoysLite lets players summon friendly units to combat waves of enemies across multiple themed stages. Players manage mana resources to strategically deploy warriors, knights, and samurai, while facing off against various foes like cats, demons, and Po.

---

## Assets and Resources

### Environment

| Asset                  | Source |
|------------------------|--------|
| Main Menu Background   | [Freepik](https://img.freepik.com/free-vector/nuclear-war-explosion-mushroom-rising-up-city_107791-14443.jpg) |
| Game Background        | [Vecteezy](https://static.vecteezy.com/system/resources/previews/022/394/347/non_2x/blue-sky-with-clouds-anime-style-background-with-shining-sun-and-white-fluffy-clouds-sunny-day-sky-scene-cartoon-illustration-vector.jpg) |
| Win Background         | [Pixabay](https://cdn.pixabay.com/photo/2014/04/03/09/58/castle-309460_1280.png) |
| Platform Tiles         | [Kenney](https://www.kenney.nl/assets/scribble-platformer) |
| Friendly Castle        | [Pixabay](https://cdn.pixabay.com/photo/2014/04/03/09/58/castle-309460_1280.png) |
| Enemy Castle           | [Vexels](https://images.vexels.com/media/users/3/130886/isolated/preview/8cd79b2163037609a061458fb7fea770-haunted-castle-cartoon-2.png) |

### Friendly Units

| Unit     | Source |
|----------|--------|
| Warrior  | [Itch.io](https://xzany.itch.io/kobold-warrior-2d-pixel-art) |
| Knight   | [Itch.io](https://xzany.itch.io/free-knight-2d-pixel-art) |
| Samurai  | [Itch.io](https://xzany.itch.io/samurai-2d-pixel-art) |

### Enemy Units

| Unit     | Source |
|----------|--------|
| Cat      | [Itch.io](https://xzany.itch.io/cat-2d-pixel-art) |
| Demon    | [Itch.io](https://xzany.itch.io/flying-demon-2d-pixel-art) |
| Po (Running) | [Wallpapers.com](https://wallpapers.com/images/hd/kung-fu-panda-po-action-pose-9um4ai85e67d0rg0.jpg) |
| Po (Fighting) | [Fandom](https://static.wikia.nocookie.net/deadliestfiction/images/e/e0/Po_Profile.png/revision/latest?cb=20200506020200) |

### UI and Text

- Buttons: [ClickMinded Button Generator](https://www.clickminded.com/button-generator/)
- Text Graphics: [CoolText](https://cooltext.com/)

---

## Audio

### Music

| Context         | Source |
|-----------------|--------|
| Main Menu       | [YouTube](https://www.youtube.com/watch?v=IQ-24ZTlKAI) |
| Win Screen      | [YouTube](https://www.youtube.com/watch?v=I9Yh7gFtFek) |
| Stage One       | [YouTube](https://www.youtube.com/watch?v=NBpu3Fhvkdc) |
| Stage Two       | [YouTube](https://www.youtube.com/watch?v=9gBTKiVqprE) |
| Final Stage     | [YouTube](https://www.youtube.com/watch?v=K_9j1k3jH-Q) |
| Battle Horn     | [YouTube](https://www.youtube.com/watch?v=uHHMV8hukBI) |
| Thunder Quote   | [YouTube](https://www.youtube.com/watch?v=q_lFHHSt-uI) |

### Sound Effects

| Effect         | Source |
|----------------|--------|
| Sword Attack   | [YouTube](https://www.youtube.com/watch?v=wURp8_kO2m8) |
| Cat Attack     | [YouTube](https://www.youtube.com/watch?v=kY4UObU5UfA) |
| Demon Attack   | [YouTube](https://www.youtube.com/watch?v=ifClLcPcpYg) |
| Skadoosh (Po)  | [YouTube](https://www.youtube.com/watch?v=7ZezcW127mg) |

---

## Project Structure

This project uses a multi-platform Gradle setup:

- `core`: Shared application logic.
- `lwjgl3`: Desktop-specific launcher and configuration (uses LWJGL3).

---

## Gradle Usage

To build or run the game, you can use the included Gradle wrapper:

### Common Tasks

| Task                     | Description |
|--------------------------|-------------|
| `./gradlew build`        | Builds all sources and archives. |
| `./gradlew lwjgl3:run`   | Runs the game using the desktop backend. |
| `./gradlew clean`        | Removes all build outputs. |
| `./gradlew test`         | Runs unit tests (if any). |

### IDE Support

| Task                | Description |
|---------------------|-------------|
| `./gradlew idea`    | Generate IntelliJ project files. |
| `./gradlew eclipse` | Generate Eclipse project files. |
| `./gradlew cleanIdea` / `cleanEclipse` | Remove project metadata for the respective IDE. |

---

## Notes

- All visual and audio assets are used for educational or non-commercial purposes.
- Some assets are sourced from third parties with their own usage terms.

---
