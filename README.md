# EleSlime

Een 2D platformer spel gebouwd met de [Yaeger](https://github.com/han-yaeger/yaeger) game engine. Navigeer door uitdagende levels als een elektrische slijm, versla vijanden en bereik het doel.

## Het spel starten

### Vereisten
- Java 21+
- Maven 3.6+

### Build en Run
```bash
mvn clean package
mvn javafx:run
```

## Besturing

| Actie | Toetsen |
|-------|---------|
| Links/Rechts bewegen | A/D of ← → |
| Omhoog/Omlaag bewegen | W/S of ↑ ↓ |
| Springen | SPACE |
| Bliksem schieten | Z |

## Hoe voeg je een level toe?

1. Maak een nieuw JSON bestand in `src/main/resources/levels/` (bijv. `lvl_4.json`)
2. Definieer het level:

```json
{
  "name": "Mijn Level",
  "tileSize": 100.0,
  "spawn": { },
  "tiles": [
  ],
  "pickups": [
  ],
  "mobs": [
  ],
  "obstacles": []
}
```

3. Voeg het toe aan de level-volgorde in `src/main/java/nl/han/jefmk/EleSlime.java`:

```java
private static final String[] LEVEL_ORDER = {
    "lvl_1",
    "lvl_2",
    "lvl_3",
    "lvl_end",
    "lvl_4"  // Jouw level
};
```

## Debug Modus

Om de leveleditor te gebruiken, pas aan in `EleSlime.java`:

```java
public static final boolean DEBUG = true;
```

Zet terug op `false` voor normaal spel.

## Technische Details

- **Engine**: Yaeger 2024.2025
- **Taal**: Java 21+
- **Build**: Maven
- **Render**: JavaFX

## Over dit project

Dit project is een **beroepsproduct** voor de opleiding **HBO-ICT SD** aan HAN Arnhem.

Het demonstreert kernvaardigheden in:
- Object-georiënteerd programmeren (Java)
- Game design en development
- Architectuur en code organisatie
