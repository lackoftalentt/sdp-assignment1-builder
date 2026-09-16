# Assignment 1 — Builder Pattern

## Part A — Design Problem

### Domain

The selected domain is a Drone Mission configuration system.

A `DroneMission` object represents a planned drone mission and contains multiple required and optional configuration parameters.

### Initial Constructor-Based Implementation

The first implementation uses a conventional constructor:

```java
DroneMission mission = new DroneMission(
        "Mountain Survey",
        "LONG_RANGE",
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

The constructor accepts multiple parameters required to fully configure the mission.

Design Problems
1. Poor readability

The constructor uses positional parameters. It is difficult to understand the meaning of values such as 300, 80, 6000, or several consecutive boolean values without checking the constructor declaration.

For example:

true, true, true, true

does not clearly communicate which features are enabled.

2. Risk of incorrect parameter order

Several parameters have the same data type. For example:

int altitude
int speed
int batteryCapacity

The compiler cannot detect a situation where these values are accidentally provided in the wrong order.

3. Difficult to extend

Adding another optional property requires changing the constructor and updating every constructor call throughout the client code.

This becomes increasingly inconvenient as the number of configurable properties grows.

4. Optional properties do not have clear defaults

The constructor requires all properties to be supplied explicitly, even when the client only wants the default behavior for optional settings.

Motivation for Builder

The constructor-based implementation demonstrates that creating a complex DroneMission object becomes difficult to read, maintain, and extend.

The Builder Pattern can improve this design by allowing the object to be constructed step by step using meaningful method names and by centralizing construction-time validation.