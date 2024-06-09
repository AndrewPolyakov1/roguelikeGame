# software-design-cli

## Задача

Реализовать Roguelike игру в терминале.

## Библиотека

Для отрисовки консольной графики используем библиотеку [Lanterna](https://github.com/mabe02/lanterna).

Туториалы:

- [Туториал 1](https://github.com/mabe02/lanterna/blob/master/docs/tutorial/Tutorial01.md)
- [Туториал 2](https://github.com/mabe02/lanterna/blob/master/docs/tutorial/Tutorial02.md)
- [Туториал 3](https://github.com/mabe02/lanterna/blob/master/docs/tutorial/Tutorial03.md)
- [Туториал 4](https://github.com/mabe02/lanterna/blob/master/docs/tutorial/Tutorial04.md)

## Архитектура
![img.png](img.png)
- Interface Entity
    - Interface Player
    - Interface NPC
        - Class Peaceful
        - Class Aggressive
        - Class Boss
    - Interface Item
        - ???

- Interface Environment
    - Class OpenLevel (Procedural generated)
    - Class BossRoom (Loaded from file)
    - Class Dungeon (Loaded ?)

- Interface Window
    - Class Settings
    - Class Inventory
    - Class Map

- Class Interface (полоска здоровья, опыт и тд., то что отрисовывается постоянно)

## Описание

### Управление

Захватываем клавиши:

- Стрелки для управления (или `WASD`)
- `Enter` для атаки
- `Esc` для выхода
- `E` для отображения инвентаря
- `I` для отображения информации о персонаже