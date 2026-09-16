# Assignment 1 — Builder Pattern

## Domain
Drone Mission configuration system. A `DroneMission` encapsulates flight parameters, safety systems, payload capacity, and hardware capabilities for commercial and industrial drone flights.

## Pattern
**Builder Pattern (GoF Creational Pattern)**

Separates the construction of a complex `DroneMission` product from its representation, allowing the same construction process to create various representations while ensuring immutability, step-by-step assembly, and robust validation.

## Technologies
- **Java 21** (OpenJDK 21)
- **JUnit 5** (JUnit Jupiter 5.13.4)
- **IntelliJ IDEA**

---

## Project Structure

```
assignment-1-builder/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── example/
│   │               └── builder/
│   │                   ├── DroneMission.java         # Product & nested Builder
│   │                   ├── DroneMissionPresets.java  # Preset configuration catalog
│   │                   ├── Main.java                 # Client demo application
│   │                   └── MissionType.java          # Mission type enumeration
│   │
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── builder/
│                       └── DroneMissionTest.java     # Automated unit test suite
│
├── docs/
│   ├── builder-uml.png                               # UML class diagram
│   ├── defense-notes.md                              # Defense questions & change simulations
│   └── generate_uml.py                               # Reproducible diagram generator
├── README.md
└── report.md
```

All source code and test files belong strictly to package `com.example.builder`.

---

## How to Run

Compile and execute `Main`:

```bash
# Compile main classes
javac -d out/production/assignment-1-builder/main $(find src/main/java -name "*.java")

# Run Main
java -cp out/production/assignment-1-builder/main com.example.builder.Main
```

Alternatively, open the project in **IntelliJ IDEA** and run `Main.main()`.

---

## How to Run Tests

Compile and execute the JUnit 5 test suite:

```bash
# Compile tests
CP="out/production/assignment-1-builder/main:$(find ~/.m2/repository -name "*.jar" | tr '\n' ':')"
javac -d out/production/assignment-1-builder/test -cp "$CP" $(find src/test/java -name "*.java")

# Run JUnit 5 Console Launcher
java -jar ~/.m2/repository/org/junit/platform/junit-platform-console-standalone/1.13.4/junit-platform-console-standalone-1.13.4.jar \
  --class-path out/production/assignment-1-builder/main:out/production/assignment-1-builder/test \
  --scan-class-path
```

In IntelliJ IDEA: right-click `DroneMissionTest` and select **Run 'DroneMissionTest'**.

---

## Builder Example

```java
DroneMission mission = new DroneMission.Builder(
        "Mountain Survey",
        MissionType.LONG_RANGE,
        "Almaty Mountains"
)
        .altitude(300)
        .speed(80)
        .batteryCapacity(6000)
        .enableGps()
        .enableCamera()
        .payloadKg(1.5)
        .durationMinutes(90)
        .build();
```

---

## Validation Rules

The builder prevents construction of invalid `DroneMission` instances before creation:

### Single-Field Invariants
1. `missionName` must not be null or blank.
2. `missionType` must not be null.
3. `destination` must not be null or blank.
4. `altitude` must be greater than 0 meters.
5. `speed` must be greater than 0 km/h.
6. `batteryCapacity` must be greater than 0 mAh.
7. `payloadKg` must not be negative.
8. `durationMinutes` must be greater than 0.

### Cross-Field Invariants
- **Rule 1**: `LONG_RANGE` mission requires GPS to be enabled (`gpsEnabled == true`).
- **Rule 2**: `LONG_RANGE` mission requires `batteryCapacity >= 5000` mAh.

---

## Presets

The [`DroneMissionPresets`](file:///home/morninginheaven/IdeaProjects/assignment-1-builder/src/main/java/com/example/builder/DroneMissionPresets.java) class supplies preconfigured, validated templates:

1. **`BASIC`** (`createBasicMission`):
   - Fast, low-altitude local transport (100 m altitude, 40 km/h speed, 4000 mAh, no GPS, no camera, 30 min duration).
2. **`SURVEY`** (`createSurveyMission`):
   - Medium-altitude mapping mission (150 m altitude, 50 km/h speed, 5000 mAh, GPS enabled, camera enabled, 1.0 kg sensor payload, 60 min duration).
3. **`LONG_RANGE`** (`createLongRangeMission`):
   - High-altitude, extended exploration (300 m altitude, 80 km/h speed, 6000 mAh, GPS enabled, camera enabled, 1.5 kg payload, 90 min duration).
