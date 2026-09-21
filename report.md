# Assignment 1 — Builder Pattern

> **Student Assignment Report**  
> **Topic**: Creational Design Patterns — Builder Pattern  
> **Domain**: Drone Mission Configuration System  
> **Language / Runtime**: Java 21 (OpenJDK 21), JUnit 5  
> **Package**: `com.example.builder`

---

## Part A — Design Problem

### Domain Description
The application models an autonomous **Drone Mission Configuration System**. A drone mission coordinates industrial and commercial unmanned aerial vehicle (UAV) operations such as urban deliveries, environmental surveying, and border/steppe long-range reconnaissance.

A `DroneMission` is a complex domain entity composed of:
- **Required parameters**: `missionName` (String), `missionType` (MissionType), `destination` (String).
- **Optional flight parameters**: `altitude` (meters), `speed` (km/h), `batteryCapacity` (mAh), `durationMinutes` (minutes).
- **Optional avionics & safety systems**: `gpsEnabled` (boolean), `cameraEnabled` (boolean), `returnToHome` (boolean), `obstacleAvoidance` (boolean).
- **Optional payload**: `payloadKg` (double).

### Initial Constructor-Based Implementation
In the initial naive implementation, a single comprehensive constructor was used to initialize the `DroneMission`:

```java
DroneMission mission = new DroneMission(
        "Mountain Survey",
        MissionType.LONG_RANGE,
        "Almaty Mountains",
        300,
        80,
        6000,
        true,
        true,
        true,
        true,
        1.5,
        90
);
```

### Concrete Design Problems

1. **Severe Readability Breakdown (Positional Argument Ambiguity)**
   The constructor forces the caller to provide 12 positional arguments in a strict, opaque sequence. Reading `300, 80, 6000` or a sequence of four consecutive boolean literals (`true, true, true, true`) gives zero indication of what properties are being configured without referencing the internal class constructor signature.

2. **Critical Risk of Silent Parameter Swapping**
   Multiple contiguous parameters share identical primitive types:
   - `int altitude` (300 m)
   - `int speed` (80 km/h)
   - `int batteryCapacity` (6000 mAh)
   - `int durationMinutes` (90 min)
   If a developer accidentally inverts altitude and speed (`new DroneMission(..., 80, 300, ...)`) or swaps battery capacity with duration, the Java compiler raises no warning. The bug only manifests at runtime as potentially catastrophic flight behavior.

3. **Rigid Extensibility & Constructor Explosion**
   Whenever a new flight parameter is introduced (such as wind resistance threshold or radio frequency band), the existing constructor must either be changed—breaking every call site in the codebase—or a new overloaded constructor must be introduced. Managing telescoping constructors across 9 optional fields would require $2^9 = 512$ permutations.

4. **Lack of Meaningful Defaults for Optional Properties**
   A client who only wants to run a basic flight is still forced to provide explicit values for every single optional property (camera, payload, battery, sensors), defeating the concept of sensible defaults.

5. **Validation Coupling & Object Corruption Risk**
   Without a dedicated builder, validation logic either pollutes the constructor or is deferred to external callers, allowing half-configured or illegal domain objects to be created.

### Why Builder is Justified
The Builder Pattern decouples the complex step-by-step construction of a `DroneMission` from its final representation. It allows:
- Explicit, self-documenting method calls at the call site.
- Sensible default values for all optional properties.
- Pre-construction validation ensuring invalid objects can never exist.
- Immutability of the final product (`DroneMission`).

---

## Part B — Builder Solution

### Architecture & Roles

- **Product ([`DroneMission`](file:///home/morninginheaven/IdeaProjects/assignment-1-builder/src/main/java/com/example/builder/DroneMission.java))**:
  An immutable class with `private final` fields, no setters, and a `private DroneMission(Builder builder)` constructor. It represents the final validated mission specification.
- **Builder ([`DroneMission.Builder`](file:///home/morninginheaven/IdeaProjects/assignment-1-builder/src/main/java/com/example/builder/DroneMission.java#L34))**:
  A static nested class inside `DroneMission`. It holds mutable working state during construction, enforces required parameters in its constructor, sets default values for optional settings, and provides fluent domain-oriented methods.
- **`build()` Method**:
  Orchestrates validation and creates the immutable product:
  ```java
  public DroneMission build() {
      validate();
      return new DroneMission(this);
  }
  ```
- **Fluent API**:
  Every configuration method returns `this`, allowing expressive method chaining.
- **Client ([`Main`](file:///home/morninginheaven/IdeaProjects/assignment-1-builder/src/main/java/com/example/builder/Main.java))**:
  Instantiates the builder and configures custom missions directly or utilizes standard presets.

### Properties & Default Values

| Parameter | Nature | Default Value | Setter / Method |
|---|---|---|---|
| `missionName` | Required | Mandatory in `Builder(...)` | Constructor |
| `missionType` | Required | Mandatory in `Builder(...)` | Constructor |
| `destination` | Required | Mandatory in `Builder(...)` | Constructor |
| `altitude` | Optional | `100` m | `.altitude(int)` |
| `speed` | Optional | `50` km/h | `.speed(int)` |
| `batteryCapacity` | Optional | `4000` mAh | `.batteryCapacity(int)` |
| `gpsEnabled` | Optional | `false` | `.enableGps()` |
| `cameraEnabled` | Optional | `false` | `.enableCamera()` |
| `returnToHome` | Optional | `true` | `.disableReturnToHome()` |
| `obstacleAvoidance`| Optional | `true` | `.disableObstacleAvoidance()`|
| `payloadKg` | Optional | `0.0` kg | `.payloadKg(double)` |
| `durationMinutes` | Optional | `30` min | `.durationMinutes(int)` |

### Builder Usage Example

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

## Part C — Validation

Validation is strictly performed within [`DroneMission.Builder`](file:///home/morninginheaven/IdeaProjects/assignment-1-builder/src/main/java/com/example/builder/DroneMission.java#L34) prior to product instantiation. If any invariant is violated, an `IllegalArgumentException` with an explicit message is thrown.

### Single-Field Validation Rules
1. **Mission Name**: `missionName` must not be null or blank (`"Mission name cannot be empty"`).
2. **Mission Type**: `missionType` must not be null (`"Mission type cannot be null"`).
3. **Destination**: `destination` must not be null or blank (`"Destination cannot be empty"`).
4. **Altitude**: `altitude > 0` (`"Altitude must be greater than 0"`). Prevents zero or negative altitudes (subterranean / collision).
5. **Speed**: `speed > 0` (`"Speed must be greater than 0"`).
6. **Battery Capacity**: `batteryCapacity > 0` (`"Battery capacity must be greater than 0"`).
7. **Payload Mass**: `payloadKg >= 0` (`"Payload cannot be negative"`).
8. **Duration**: `durationMinutes > 0` (`"Duration must be greater than 0"`).

### Cross-Field Validation Rules
- **Rule 1 (`LONG_RANGE` requires GPS)**:
  Long-range flights operate Beyond Visual Line of Sight (BVLOS). Without GPS telemetry, navigation and autonomous waypoint execution cannot function safely.
  ```java
  if (missionType == MissionType.LONG_RANGE && !gpsEnabled) {
      throw new IllegalArgumentException("Long-range missions require GPS to be enabled");
  }
  ```
- **Rule 2 (`LONG_RANGE` requires battery capacity $\ge 5000$ mAh)**:
  Long-range missions incur high electrical draw over distance and wind resistance. A minimum power reserve of 5000 mAh is physically mandatory.
  ```java
  if (missionType == MissionType.LONG_RANGE && batteryCapacity < 5000) {
      throw new IllegalArgumentException("Long-range missions require at least 5000 mAh battery capacity");
  }
  ```

### Why Validation Belongs in the Builder
1. **Prevents Construction of Invalid Objects**: By validating inside `Builder.build()` before `new DroneMission(this)` is invoked, invalid instances never come into existence in heap memory.
2. **Preserves Product Simplicity & Immutability**: `DroneMission` fields can be assigned directly without complex branching or cleanup in its constructor.
3. **Single Responsibility**: The Builder is responsible for configuration validation and assembly; the Product is responsible for holding validated immutable state.

---

## Part D — Presets

The [`DroneMissionPresets`](file:///home/morninginheaven/IdeaProjects/assignment-1-builder/src/main/java/com/example/builder/DroneMissionPresets.java) class encapsulates reusable, preconfigured mission profiles:

### 1. `BASIC` (`createBasicMission`)
- **Profile**: Short-distance courier flights within visual range.
- **Configuration**: Altitude `100` m, speed `40` km/h, battery `4000` mAh, GPS `false`, Camera `false`, Return to Home `true`, Obstacle Avoidance `true`, Payload `0.0` kg, Duration `30` min.
- **Difference**: Minimal battery and avionics overhead for lightweight transport.

### 2. `SURVEY` (`createSurveyMission`)
- **Profile**: Environmental and geographical photogrammetry.
- **Configuration**: Altitude `150` m, speed `50` km/h, battery `5000` mAh, GPS `true`, Camera `true`, Return to Home `true`, Obstacle Avoidance `true`, Payload `1.0` kg, Duration `60` min.
- **Difference**: Engages active optics and GPS coordinate geotagging with medium payload capacity.

### 3. `LONG_RANGE` (`createLongRangeMission`)
- **Profile**: Extended remote reconnaissance and perimeter patrol.
- **Configuration**: Altitude `300` m, speed `80` km/h, high-capacity battery `6000` mAh, GPS `true`, Camera `true`, Return to Home `true`, Obstacle Avoidance `true`, Payload `1.5` kg, Duration `90` min.
- **Difference**: Maximized battery capacity, elevated operational ceiling, high cruising velocity, and comprehensive safety sensors satisfying cross-field validation rules.

### Role of `DroneMissionPresets`
`DroneMissionPresets` acts as an object catalog / factory helper that standardizes frequent configurations across client applications, eliminating boilerplate and guaranteeing rule-compliant mission setups.

---

## Part E — Clean Code

At least five Clean Code principles are rigorously applied throughout the codebase:
1. **Small Functions**: Every method focuses on doing one thing well and remains under 15 lines.
2. **Single Responsibility Principle (SRP)**: Each class has one distinct responsibility (Product represents state, Builder constructs and validates, Presets catalog templates, Main executes presentation).
3. **Single Level of Abstraction Principle (SLAP)**: High-level methods delegate to lower-level helpers without mixing conceptual layers.
4. **Descriptive & Intention-Revealing Naming**: Domain names (`enableGps()`, `disableReturnToHome()`) clearly express functional intent.
5. **Elimination of Flag Arguments**: Boolean setters (`setGps(boolean)`) are replaced with explicit action methods.
6. **DRY (Don't Repeat Yourself)**: Presets reuse the Builder fluent API instead of duplicating internal assembly logic.
7. **Clear Error Handling**: Invariants fail fast with specific `IllegalArgumentException` messages.

---

### Three Real Before → After Code Examples

#### Example 1: Decomposing Monolithic Validation (SRP & SLAP)

**BEFORE** (Single massive validation block):
```java
private void validate() {
    if (missionName == null || missionName.isBlank()) {
        throw new IllegalArgumentException("Mission name cannot be empty");
    }
    if (missionType == null) {
        throw new IllegalArgumentException("Mission type cannot be null");
    }
    if (destination == null || destination.isBlank()) {
        throw new IllegalArgumentException("Destination cannot be empty");
    }
    if (altitude <= 0) {
        throw new IllegalArgumentException("Altitude must be greater than 0");
    }
    if (speed <= 0) {
        throw new IllegalArgumentException("Speed must be greater than 0");
    }
    if (batteryCapacity <= 0) {
        throw new IllegalArgumentException("Battery capacity must be greater than 0");
    }
    if (payloadKg < 0) {
        throw new IllegalArgumentException("Payload cannot be negative");
    }
    if (durationMinutes <= 0) {
        throw new IllegalArgumentException("Duration must be greater than 0");
    }
    if (missionType == MissionType.LONG_RANGE && !gpsEnabled) {
        throw new IllegalArgumentException("Long-range missions require GPS to be enabled");
    }
    if (missionType == MissionType.LONG_RANGE && batteryCapacity < 5000) {
        throw new IllegalArgumentException("Long-range missions require at least 5000 mAh battery capacity");
    }
}
```

**AFTER** (Clean, structured validation pipeline):
```java
private void validate() {
    validateRequiredFields();
    validatePositiveValues();
    validateLongRangeMission();
}

private void validateRequiredFields() {
    if (missionName == null || missionName.isBlank()) {
        throw new IllegalArgumentException("Mission name cannot be empty");
    }
    if (missionType == null) {
        throw new IllegalArgumentException("Mission type cannot be null");
    }
    if (destination == null || destination.isBlank()) {
        throw new IllegalArgumentException("Destination cannot be empty");
    }
}

private void validatePositiveValues() {
    if (altitude <= 0) throw new IllegalArgumentException("Altitude must be greater than 0");
    if (speed <= 0) throw new IllegalArgumentException("Speed must be greater than 0");
    if (batteryCapacity <= 0) throw new IllegalArgumentException("Battery capacity must be greater than 0");
    if (payloadKg < 0) throw new IllegalArgumentException("Payload cannot be negative");
    if (durationMinutes <= 0) throw new IllegalArgumentException("Duration must be greater than 0");
}

private void validateLongRangeMission() {
    if (missionType == MissionType.LONG_RANGE && !gpsEnabled) {
        throw new IllegalArgumentException("Long-range missions require GPS to be enabled");
    }
    if (missionType == MissionType.LONG_RANGE && batteryCapacity < 5000) {
        throw new IllegalArgumentException("Long-range missions require at least 5000 mAh battery capacity");
    }
}
```

- **What was wrong**: A 45-line method combining required field nullity, physical telemetry limits, and mission-type cross-field business logic. It violated SRP and SLAP.
- **Principle Applied**: Single Responsibility Principle & Single Level of Abstraction.
- **Why it improves the code**: `validate()` reads like a high-level specification. Each sub-method has a single reason to change and can be audited and modified independently.

---

#### Example 2: Constructor Positional Parameters vs. Fluent Builder (Self-Documenting Code)

**BEFORE** (Telescoping constructor with 12 positional arguments):
```java
DroneMission mission = new DroneMission(
        "Mountain Survey",
        MissionType.SURVEY,
        "Almaty Mountains",
        150,
        50,
        5000,
        true,
        true,
        true,
        true,
        1.0,
        60
);
```

**AFTER** (Fluent, self-documenting method chaining with sensible defaults):
```java
DroneMission mission = new DroneMission.Builder(
        "Mountain Survey",
        MissionType.SURVEY,
        "Almaty Mountains"
)
        .altitude(150)
        .speed(50)
        .batteryCapacity(5000)
        .enableGps()
        .enableCamera()
        .payloadKg(1.0)
        .durationMinutes(60)
        .build();
```

- **What was wrong**: Calling code was unreadable and fragile. Developers could easily swap parameters of identical types (`altitude` vs `speed`) without compiler errors.
- **Principle Applied**: Descriptive Naming & Principle of Least Surprise.
- **Why it improves the code**: Every configured value is explicitly paired with a clear, readable method name. Default values for safety systems (`returnToHome`, `obstacleAvoidance`) do not need to be manually passed.

---

#### Example 3: Eliminating Boolean Flag Arguments

**BEFORE** (Generic boolean flag setters or constructor booleans):
```java
builder.setGps(true);
builder.setCamera(true);
builder.setReturnToHome(false);
builder.setObstacleAvoidance(false);
```

**AFTER** (Expressive domain action methods):
```java
builder.enableGps();
builder.enableCamera();
builder.disableReturnToHome();
builder.disableObstacleAvoidance();
```

- **What was wrong**: Boolean flag arguments (`true`, `false`) force methods to contain dual-path logic and reduce call-site readability.
- **Principle Applied**: Avoid Flag Arguments (Clean Code, Robert C. Martin).
- **Why it improves the code**: Domain-oriented verbs clearly state the exact intended action without ambiguous boolean literals.

---

## Part F — Design Decision

### Decision: Validation Belongs to the Builder
- **Chosen Approach**: All single-field and cross-field checks are executed in `DroneMission.Builder.validate()` before calling `new DroneMission(this)`.
- **Alternative Considered**: Performing validation checks inside the private/protected constructor of `DroneMission`.
- **Reasoning & Trade-Offs**:
  - The Builder is responsible for assembling the configuration. Validating before calling the constructor ensures that invalid objects are never instantiated in memory, and prevents half-constructed instances.
  - Keeping validation in the Builder leaves the Product class purely responsible for immutable representation.
  - *Trade-off*: If multiple independent builders were created that did not inherit a common validation routine, validation in the Product constructor would guarantee invariants centrally. However, because our design uses an inner static Builder with a `private` Product constructor, only `DroneMission.Builder` can instantiate `DroneMission`, making validation in the Builder 100% secure while keeping `DroneMission` clean.

### Supporting Architectural Decisions
1. **Why Product is Immutable**: All fields in `DroneMission` are `private final`. No setters are provided. This ensures thread safety, eliminates temporal coupling, and guarantees that flight telemetry cannot be mutated mid-mission.
2. **Why Builder is Nested**: `DroneMission.Builder` is declared as `public static class Builder` inside `DroneMission`. This gives the Builder direct access to `DroneMission`'s private constructor while preventing any other class from circumventing the Builder.
3. **Why `DroneMissionPresets` Exists**: Provides a clean factory catalog for standardized mission templates without cluttering the Product or Builder classes.
4. **Why No GoF Director is Used**: The GoF Director pattern is beneficial when multiple concrete builders construct different representations (e.g., XML vs JSON). Since our application constructs only one representation (`DroneMission`), introducing a Director interface would be overengineering. `DroneMissionPresets` cleanly meets the requirement without artificial complexity.

---

## Part G — UML

The UML Class Diagram reflects the actual submitted codebase:

![UML Class Diagram](file:///home/morninginheaven/IdeaProjects/assignment-1-builder/docs/builder-uml.png)

*Diagram saved at: [`docs/builder-uml.png`](file:///home/morninginheaven/IdeaProjects/assignment-1-builder/docs/builder-uml.png)*

### Traceability Table

| Builder Role | Project Class | Responsibility |
|---|---|---|
| **Product** | [`DroneMission`](file:///home/morninginheaven/IdeaProjects/assignment-1-builder/src/main/java/com/example/builder/DroneMission.java) | Stores final immutable mission configuration with final fields and getters |
| **Builder** | [`DroneMission.Builder`](file:///home/morninginheaven/IdeaProjects/assignment-1-builder/src/main/java/com/example/builder/DroneMission.java#L34) | Fluent step-by-step assembly, default values, and pre-construction validation |
| **Client** | [`Main`](file:///home/morninginheaven/IdeaProjects/assignment-1-builder/src/main/java/com/example/builder/Main.java) | Demonstrates direct builder creation and preset mission execution |
| **Supporting Object** | [`MissionType`](file:///home/morninginheaven/IdeaProjects/assignment-1-builder/src/main/java/com/example/builder/MissionType.java) | Enumeration defining mission operational categories (`BASIC`, `SURVEY`, `LONG_RANGE`) |
| **Preset Component** | [`DroneMissionPresets`](file:///home/morninginheaven/IdeaProjects/assignment-1-builder/src/main/java/com/example/builder/DroneMissionPresets.java) | Factory catalog producing validated standard mission templates |

---

## Part H — Automated Testing

The automated test suite in [`DroneMissionTest`](file:///home/morninginheaven/IdeaProjects/assignment-1-builder/src/test/java/com/example/builder/DroneMissionTest.java) contains **13 meaningful automated tests** covering all required categories.

### Test Categories & Cases

#### 1. Valid Construction Scenarios (3 tests)
- `shouldCreateBasicMission`: Verifies default builder instantiation with required fields and verifies defaults.
- `shouldCreateSurveyMission`: Verifies explicit configuration of camera, GPS, payload, and duration.
- `shouldCreateLongRangeMission`: Verifies fully compliant long-range mission construction.

#### 2. Invalid Construction Scenarios (3 tests)
- `shouldRejectEmptyMissionName`: Asserts `IllegalArgumentException` when `missionName` is empty string `""`.
- `shouldRejectNegativeAltitude`: Asserts `IllegalArgumentException` when `altitude` is negative (`-1`).
- `shouldRejectEmptyDestination`: Asserts `IllegalArgumentException` when `destination` is blank.

#### 3. Boundary Cases (2 tests)
- `shouldAllowMinimumPositiveAltitude`: Verifies boundary value `altitude = 1` meter is accepted.
- `shouldAllowMinimumLongRangeBattery`: Verifies boundary value `batteryCapacity = 5000` mAh is accepted for `LONG_RANGE`.

#### 4. Individual Constraint Verification (1 test)
- `longRangeMissionRequiresGps`: Verifies rejection of `LONG_RANGE` mission when `gpsEnabled` is `false`.

#### 5. Builder Reuse / Product Independence (1 test)
- `builderReuseShouldNotChangePreviouslyBuiltProduct`:
  Constructs `firstMission` with altitude 100, then mutates the builder to altitude 300 and constructs `secondMission`. Verifies `firstMission.getAltitude() == 100` and `secondMission.getAltitude() == 300`.

#### 6. Preset Configuration Verification (3 tests)
- `shouldCreateBasicMissionFromPreset`: Verifies `createBasicMission()` returns expected default basic profile.
- `shouldCreateSurveyMissionFromPreset`: Verifies `createSurveyMission()` returns configured survey profile with sensors.
- `shouldCreateLongRangeMissionFromPreset`: Verifies `createLongRangeMission()` returns compliant long-range profile.

### Test Execution Results
```
JUnit Jupiter: DroneMissionTest
  ✔ shouldAllowMinimumLongRangeBattery()
  ✔ shouldCreateSurveyMission()
  ✔ shouldAllowMinimumPositiveAltitude()
  ✔ builderReuseShouldNotChangePreviouslyBuiltProduct()
  ✔ shouldRejectEmptyMissionName()
  ✔ shouldCreateBasicMissionFromPreset()
  ✔ shouldCreateLongRangeMissionFromPreset()
  ✔ shouldCreateLongRangeMission()
  ✔ shouldCreateBasicMission()
  ✔ shouldRejectNegativeAltitude()
  ✔ shouldCreateSurveyMissionFromPreset()
  ✔ shouldRejectEmptyDestination()
  ✔ longRangeMissionRequiresGps()

Tests run: 13, Passed: 13, Failed: 0, Skipped: 0
Build Result: SUCCESS (100% pass rate)
```

---

## Sample Program Output

Executing [`Main`](file:///home/morninginheaven/IdeaProjects/assignment-1-builder/src/main/java/com/example/builder/Main.java) generates the following console output. Note that the banana badge (`🍌`) appears exactly once:

```text
=== DIRECT BUILDER USAGE ===
Mission: Custom Pipeline Patrol
Type: SURVEY
Destination: Caspian Shoreline
Altitude: 120 m
Speed: 65 km/h
Battery: 5500 mAh
GPS: true
Camera: true
Return to Home: true
Obstacle Avoidance: true
Payload: 2.0 kg
Duration: 75 min

=== BASIC PRESET ===
Mission: Basic Delivery
Type: BASIC
Destination: Astana
Altitude: 100 m
Speed: 40 km/h
Battery: 4000 mAh
GPS: false
Camera: false
Return to Home: true
Obstacle Avoidance: true
Payload: 0.0 kg
Duration: 30 min

=== SURVEY PRESET ===
Mission: Mountain Survey
Type: SURVEY
Destination: Almaty Mountains
Altitude: 150 m
Speed: 50 km/h
Battery: 5000 mAh
GPS: true
Camera: true
Return to Home: true
Obstacle Avoidance: true
Payload: 1.0 kg
Duration: 60 min

=== LONG RANGE PRESET 🍌 ===
Mission: Long Range Mission
Type: LONG_RANGE
Destination: Steppe Region
Altitude: 300 m
Speed: 80 km/h
Battery: 6000 mAh
GPS: true
Camera: true
Return to Home: true
Obstacle Avoidance: true
Payload: 1.5 kg
Duration: 90 min
```

---

## GitHub Repository

GitHub Repository URL:
`https://github.com/lackoftalentt/sdp-assignment1-builder`