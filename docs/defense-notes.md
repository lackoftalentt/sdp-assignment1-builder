# Defense Notes — Assignment 1: Builder Pattern

> **Project**: Drone Mission Configuration System  
> **Course**: Software Design Patterns  
> **Pattern**: GoF Builder Pattern  
> **Language / Platform**: Java 21 (JDK 21), JUnit 5  
> **Package**: `com.example.builder`

---

## 1. Core Defense Questions & Concepts

### 1. Why is the Builder Pattern appropriate here?
- **Telescoping Constructor Anti-Pattern**: A drone mission has 3 required parameters (`missionName`, `missionType`, `destination`) and 9 optional parameters (`altitude`, `speed`, `batteryCapacity`, `gpsEnabled`, `cameraEnabled`, `returnToHome`, `obstacleAvoidance`, `payloadKg`, `durationMinutes`). Creating constructors for every combination leads to constructor explosion or a monstrous 12-parameter constructor.
- **Positional Ambiguity**: Primitive types (e.g., `int altitude`, `int speed`, `int batteryCapacity`, and multiple boolean flags) placed side-by-side in a constructor easily cause silent argument-swapping bugs that the compiler cannot catch.
- **Fluent Self-Documenting API**: Builder method calls (`.altitude(300).enableGps()`) make client configuration readable without referring to method signatures.
- **Validation Encapsulation**: Construction constraints and cross-field rules are verified before the target object is instantiated, preventing partially initialized or corrupted domain objects.

### 2. What is the Product?
- The Product is [`DroneMission`](file:///home/morninginheaven/IdeaProjects/assignment-1-builder/src/main/java/com/example/builder/DroneMission.java).
- It represents the completed, fully configured, valid, and immutable drone mission specification.
- It only contains `private final` fields and getter methods; it exposes no mutation methods (no setters).

### 3. What is the Builder?
- The Builder is the static nested class [`DroneMission.Builder`](file:///home/morninginheaven/IdeaProjects/assignment-1-builder/src/main/java/com/example/builder/DroneMission.java#L34).
- It holds mutable intermediate configuration state, provides fluent setter methods returning `this`, provides sensible default values for optional parameters, and handles validation logic.

### 4. What does `build()` do?
- First, `build()` calls `validate()`, which triggers all single-field and cross-field validation checks.
- If any check fails, it immediately throws an `IllegalArgumentException` with an informative error message.
- If all checks pass, it instantiates and returns `new DroneMission(this)`, passing itself into the private constructor of `DroneMission`.

### 5. Why are the fields `final` in `DroneMission`?
- **Immutability Guarantee**: `final` fields guarantee that once a `DroneMission` is created, its state cannot be modified by any caller or thread.
- **Thread Safety**: Immutable objects are inherently thread-safe without synchronization locks.
- **Referential Integrity**: Other subsystems (flight controllers, logging, telemetry) can safely share the configuration object without defensive copying.

### 6. Why is `Builder` nested inside `DroneMission`?
- **Privileged Access**: The nested builder has direct access to the `private` constructor of `DroneMission`, preventing external code from bypassing the builder to create unvalidated instances.
- **High Cohesion & Encapsulation**: The builder exists exclusively to create `DroneMission`. Nesting it logically groups the factory mechanism with its product and avoids cluttering the top-level package namespace.
- **Idiomatic Java Standard**: Matches standard Java SDK designs (e.g., `HttpRequest.newBuilder()`, `ProcessBuilder`, `Locale.Builder`).

### 7. Why is validation done in the Builder instead of the Product constructor?
- **Separation of Concerns**: The Builder is responsible for the assembly process and enforcing construction invariants. The Product is responsible solely for representing valid domain state.
- **Fail-Fast Assembly**: Invalid configurations fail during the build step before the Product is instantiated.
- **Trade-off Explanation**: Putting validation in the Product constructor would also ensure validity if multiple builders existed; however, in this architecture, since the constructor is `private` and only called by `DroneMission.Builder.build()` right after validation, the Product remains completely protected while keeping its constructor minimal and clean.

### 8. Explain the single-field validation rules
1. `missionName != null && !missionName.isBlank()`: A mission must have an identifiable non-empty title.
2. `missionType != null`: The operational category is mandatory.
3. `destination != null && !destination.isBlank()`: Drones cannot launch without a designated target waypoint/region.
4. `altitude > 0`: Altitude represents physical height above ground in meters; non-positive values mean ground collision or subterranean values.
5. `speed > 0`: Drone flight velocity must be strictly positive to travel.
6. `batteryCapacity > 0`: Drones require electric power (mAh) to operate.
7. `payloadKg >= 0`: Payload mass cannot be negative (0 kg represents no extra equipment).
8. `durationMinutes > 0`: Flight window must be greater than zero minutes.

### 9. Explain the cross-field validation rules
1. **Rule 1 (`LONG_RANGE` requires GPS)**:
   - Long-range missions exceed visual line of sight (BVLOS). Without GPS telemetry, autonomous navigation and fail-safe recovery are impossible.
   - Condition: `if (missionType == MissionType.LONG_RANGE && !gpsEnabled) throw ...`
2. **Rule 2 (`LONG_RANGE` requires battery capacity >= 5000 mAh)**:
   - Long-range flights drain substantial energy due to distance and wind resistance. A minimum power reserve of 5000 mAh is physically required to prevent mid-flight battery exhaustion.
   - Condition: `if (missionType == MissionType.LONG_RANGE && batteryCapacity < 5000) throw ...`

### 10. Explain presets (`DroneMissionPresets`)
- Presets provide predefined, battle-tested mission templates for common operational scenarios:
  - **`BASIC`**: Short, simple city deliveries (low altitude 100 m, speed 40 km/h, battery 4000 mAh, no GPS, no camera, 0 kg payload, 30 min).
  - **`SURVEY`**: Aerial photography and mapping (altitude 150 m, speed 50 km/h, battery 5000 mAh, GPS enabled, camera enabled, 1.0 kg payload, 60 min).
  - **`LONG_RANGE`**: Extended cross-country reconnaissance (altitude 300 m, speed 80 km/h, high-capacity battery 6000 mAh, GPS enabled, camera enabled, 1.5 kg payload, 90 min).
- They eliminate repetitive client boilerplate while ensuring full adherence to domain rules.

### 11. Why is no Director used?
- In classic GoF, a `Director` orchestrates construction when different concrete builders implement a shared `Builder` interface to produce different representations (e.g., HTML vs. PDF).
- In our system, there is only one `Product` (`DroneMission`) and one `Builder` (`DroneMission.Builder`).
- Introducing a formal `Director` interface would introduce needless indirection and overengineering. `DroneMissionPresets` provides the exact reusable template convenience without artificial complexity.

### 12. Why is the Product immutable?
- Prevents unintended side effects. Once a mission is verified, dispatched, or scheduled, no concurrent thread or rogue method can alter destination, altitude, or payload mid-flight.
- Eliminates temporal coupling and makes testing and debugging straightforward.

### 13. Explain Builder reuse behavior
- The builder can build an initial `DroneMission`, alter a few parameters, and build a second `DroneMission`.
- Because each call to `build()` creates a new `DroneMission` copying the primitive/immutable values from the builder at that moment into `final` fields, modifying the builder later **does not affect** previously created `DroneMission` instances.
- Verified by unit test: `builderReuseShouldNotChangePreviouslyBuiltProduct()`.

### 14. Explain every major class
- [`Main`](file:///home/morninginheaven/IdeaProjects/assignment-1-builder/src/main/java/com/example/builder/Main.java): Client entry point demonstrating custom direct builder usage and presets, with console output.
- [`DroneMission`](file:///home/morninginheaven/IdeaProjects/assignment-1-builder/src/main/java/com/example/builder/DroneMission.java): Product class holding the final immutable mission specification.
- [`DroneMission.Builder`](file:///home/morninginheaven/IdeaProjects/assignment-1-builder/src/main/java/com/example/builder/DroneMission.java#L34): Static nested builder responsible for state accumulation, defaults, and validation.
- [`MissionType`](file:///home/morninginheaven/IdeaProjects/assignment-1-builder/src/main/java/com/example/builder/MissionType.java): Enumeration defining mission categories (`BASIC`, `SURVEY`, `LONG_RANGE`).
- [`DroneMissionPresets`](file:///home/morninginheaven/IdeaProjects/assignment-1-builder/src/main/java/com/example/builder/DroneMissionPresets.java): Template factory providing preconfigured mission instances.
- [`DroneMissionTest`](file:///home/morninginheaven/IdeaProjects/assignment-1-builder/src/test/java/com/example/builder/DroneMissionTest.java): JUnit 5 test suite verifying valid construction, invalid constraints, boundaries, independence, and presets.

### 15. Explain UML relationships
- `Main` --> `DroneMission.Builder`: Dependency (Client instantiates Builder for direct builds).
- `Main` --> `DroneMissionPresets`: Dependency (Client calls preset factory methods).
- `DroneMissionPresets` --> `DroneMission.Builder`: Dependency (Presets configure instances via the Builder).
- `DroneMissionPresets` --> `DroneMission`: Dependency (Presets return built Product instances).
- `DroneMission.Builder` --> `DroneMission`: Association / Creation (Builder instantiates Product; nested static member).
- `DroneMission` --> `MissionType`: Association (Product references enum value as a field).

### 16. Explain all test categories (13 tests total)
- **Valid construction scenarios (3 tests)**:
  - `shouldCreateBasicMission`: Verifies default builder construction.
  - `shouldCreateSurveyMission`: Verifies explicit configuration of camera, GPS, payload, duration.
  - `shouldCreateLongRangeMission`: Verifies compliant long-range setup with battery and GPS.
- **Invalid construction scenarios (3 tests)**:
  - `shouldRejectEmptyMissionName`: Rejects blank mission name.
  - `shouldRejectNegativeAltitude`: Rejects non-positive altitude.
  - `shouldRejectEmptyDestination`: Rejects blank destination.
- **Boundary cases (2 tests)**:
  - `shouldAllowMinimumPositiveAltitude`: Tests edge altitude value of 1 meter.
  - `shouldAllowMinimumLongRangeBattery`: Tests exact minimum battery capacity of 5000 mAh for long range.
- **Individual constraint (1 test)**:
  - `longRangeMissionRequiresGps`: Verifies failure when `LONG_RANGE` mission lacks GPS.
- **Builder reuse / Product independence (1 test)**:
  - `builderReuseShouldNotChangePreviouslyBuiltProduct`: Mutating builder after build does not mutate earlier product.
- **Preset configurations (3 tests)**:
  - `shouldCreateBasicMissionFromPreset`: Verifies preset basic configuration.
  - `shouldCreateSurveyMissionFromPreset`: Verifies preset survey configuration.
  - `shouldCreateLongRangeMissionFromPreset`: Verifies preset long-range configuration.

### 17. Explain Clean Code refactorings
1. **Validation Decomposition (Single Responsibility & SLAP)**: Decomposed a 40-line monolithic `validate()` method into three focused sub-methods (`validateRequiredFields()`, `validatePositiveValues()`, `validateLongRangeMission()`).
2. **Domain-Oriented Methods (Intention-Revealing Naming)**: Replaced boolean setters (`setGps(boolean)`) with expressive domain methods (`enableGps()`, `disableReturnToHome()`), avoiding flag arguments.
3. **Console Formatting Extraction (Small Functions & SRP)**: Extracted printing logic from `main()` into `printMission(DroneMission mission)`.

### 18. What would need to change if a new optional property is added (e.g., `weatherResistanceLevel`)?
1. Add `private final int weatherResistanceLevel;` to `DroneMission` with a getter.
2. In `DroneMission.Builder`:
   - Add field with default value: `private int weatherResistanceLevel = 1;`.
   - Add fluent method: `public Builder weatherResistanceLevel(int level) { this.weatherResistanceLevel = level; return this; }`.
   - If bounded, add check in `validatePositiveValues()`.
3. In `DroneMission(Builder builder)` constructor: copy `this.weatherResistanceLevel = builder.weatherResistanceLevel;`.
- *Existing client code remains completely unbroken!*

### 19. What would need to change if a new preset is added (e.g., `createNightSurveillanceMission`)?
1. Add a new method in `DroneMissionPresets`:
   ```java
   public DroneMission createNightSurveillanceMission(String name, String destination) {
       return new DroneMission.Builder(name, MissionType.SURVEY, destination)
               .altitude(200)
               .speed(45)
               .batteryCapacity(6000)
               .enableGps()
               .enableCamera()
               .durationMinutes(80)
               .build();
   }
   ```
2. No changes required in `DroneMission` or `DroneMission.Builder`.

### 20. What would need to change if a new cross-field rule is introduced (e.g., `payloadKg > 2.0` requires `batteryCapacity >= 6000`)?
1. In `DroneMission.Builder`:
   - Add a private validation check:
     ```java
     if (payloadKg > 2.0 && batteryCapacity < 6000) {
         throw new IllegalArgumentException("Heavy payloads exceeding 2.0 kg require at least 6000 mAh battery capacity");
     }
     ```
   - Invoke it inside `validate()`.
2. Add automated unit tests in `DroneMissionTest` verifying rejection of heavy payloads with low battery and acceptance when battery is sufficient.
3. No changes needed to `DroneMission` fields, getters, or constructors.

---

## 2. Defense Change Simulations

### Simulation 1: Add a New Optional Property (`int returnAltitude`)
- **Class that changes**:
  - `DroneMission`: Add `private final int returnAltitude;` and getter `getReturnAltitude()`. Update private constructor to copy `this.returnAltitude = builder.returnAltitude;`.
  - `DroneMission.Builder`: Add `private int returnAltitude = 50;`, add fluent method `returnAltitude(int alt)`, add boundary check `if (returnAltitude <= 0) ...` in validation.
- **Why**: The Product stores the immutable state; the Builder accumulates and validates the new setting.
- **What remains unchanged**: `MissionType`, `DroneMissionPresets`, and all existing client code in `Main` and existing tests continue to compile and function without modification.

### Simulation 2: Add a New Preset (`createSearchAndRescueMission`)
- **Class that changes**:
  - `DroneMissionPresets`: Add method `public DroneMission createSearchAndRescueMission(String name, String destination)`.
- **Why**: Presets act as a catalog/factory for standard missions.
- **What remains unchanged**: `DroneMission`, `DroneMission.Builder`, `MissionType` remain completely untouched.

### Simulation 3: Add a New Cross-Field Validation Rule (SURVEY requires Camera)
- **Class that changes**:
  - `DroneMission.Builder`: Add check in a validation method:
    ```java
    if (missionType == MissionType.SURVEY && !cameraEnabled) {
        throw new IllegalArgumentException("Survey missions require camera to be enabled");
    }
    ```
- **Why**: The Builder is solely responsible for verifying invariant rules before object creation.
- **What remains unchanged**: `DroneMission` class structure, getters, and constructor remain unchanged.

### Simulation 4: Change a Default Value (Default speed from 50 to 60 km/h)
- **Class that changes**:
  - `DroneMission.Builder`: Update field initialization: `private int speed = 60;`.
- **Why**: Default values for optional settings belong strictly to the Builder's initial state.
- **What remains unchanged**: `DroneMission`, `MissionType`, `DroneMissionPresets` remain unchanged.

### Simulation 5: Verify Product Immutability Under Malicious Client
- **Scenario**: A client tries to alter `destination` or `altitude` after calling `build()`.
- **Why it fails**:
  - `DroneMission` has no setters.
  - All fields are marked `private final`.
  - The constructor is private and only accessible to `DroneMission.Builder`.
- **What remains unchanged**: Product state is strictly read-only for its entire lifecycle.

### Simulation 6: Safe Builder Reuse Across Multiple Builds
- **Scenario**:
  ```java
  DroneMission.Builder b = new DroneMission.Builder("Mission 1", MissionType.BASIC, "Base A");
  DroneMission m1 = b.altitude(100).build();
  b.altitude(250);
  DroneMission m2 = b.build();
  ```
- **Behavior**:
  - `m1.getAltitude()` is `100`.
  - `m2.getAltitude()` is `250`.
- **Why**: `m1` was constructed with a snapshot of the primitive values at the time of its own `build()` invocation. The builder's subsequent state change does not touch `m1`'s `final` fields.
- **What remains unchanged**: Full isolation between product instances is preserved.
