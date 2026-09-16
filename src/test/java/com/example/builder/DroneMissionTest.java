package com.example.builder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DroneMissionTest {

    // 1. Valid construction scenario
    @Test
    void shouldCreateBasicMission() {
        DroneMission mission = new DroneMission.Builder(
                "Basic Mission",
                MissionType.BASIC,
                "Astana"
        ).build();

        assertEquals("Basic Mission", mission.getMissionName());
        assertEquals(MissionType.BASIC, mission.getMissionType());
        assertEquals("Astana", mission.getDestination());
    }

    // 2. Valid construction scenario
    @Test
    void shouldCreateSurveyMission() {
        DroneMission mission = new DroneMission.Builder(
                "Survey Mission",
                MissionType.SURVEY,
                "Almaty"
        )
                .enableGps()
                .enableCamera()
                .payloadKg(1.0)
                .durationMinutes(60)
                .build();

        assertTrue(mission.isGpsEnabled());
        assertTrue(mission.isCameraEnabled());
        assertEquals(1.0, mission.getPayloadKg());
        assertEquals(60, mission.getDurationMinutes());
    }

    // 3. Valid construction scenario
    @Test
    void shouldCreateLongRangeMission() {
        DroneMission mission = new DroneMission.Builder(
                "Long Range",
                MissionType.LONG_RANGE,
                "Steppe"
        )
                .batteryCapacity(6000)
                .enableGps()
                .build();

        assertEquals(MissionType.LONG_RANGE, mission.getMissionType());
        assertEquals(6000, mission.getBatteryCapacity());
        assertTrue(mission.isGpsEnabled());
    }

    // 4. Invalid construction scenario
    @Test
    void shouldRejectEmptyMissionName() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DroneMission.Builder(
                        "",
                        MissionType.BASIC,
                        "Astana"
                ).build()
        );

        assertEquals(
                "Mission name cannot be empty",
                exception.getMessage()
        );
    }

    // 5. Invalid construction scenario
    @Test
    void shouldRejectNegativeAltitude() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DroneMission.Builder(
                        "Invalid Mission",
                        MissionType.BASIC,
                        "Astana"
                )
                        .altitude(-1)
                        .build()
        );

        assertEquals(
                "Altitude must be greater than 0",
                exception.getMessage()
        );
    }

    // 6. Invalid construction scenario
    @Test
    void shouldRejectEmptyDestination() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DroneMission.Builder(
                        "Invalid Mission",
                        MissionType.BASIC,
                        ""
                ).build()
        );

        assertEquals(
                "Destination cannot be empty",
                exception.getMessage()
        );
    }

    // 7. Boundary case
    @Test
    void shouldAllowMinimumPositiveAltitude() {
        DroneMission mission = new DroneMission.Builder(
                "Boundary Mission",
                MissionType.BASIC,
                "Astana"
        )
                .altitude(1)
                .build();

        assertEquals(1, mission.getAltitude());
    }

    // 8. Boundary case
    @Test
    void shouldAllowMinimumLongRangeBattery() {
        DroneMission mission = new DroneMission.Builder(
                "Boundary Long Range",
                MissionType.LONG_RANGE,
                "Steppe"
        )
                .batteryCapacity(5000)
                .enableGps()
                .build();

        assertEquals(5000, mission.getBatteryCapacity());
    }

    // 9. Individual constraint
    @Test
    void longRangeMissionRequiresGps() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DroneMission.Builder(
                        "Long Range",
                        MissionType.LONG_RANGE,
                        "Steppe"
                )
                        .batteryCapacity(6000)
                        .build()
        );

        assertEquals(
                "Long-range missions require GPS to be enabled",
                exception.getMessage()
        );
    }

    // 10. Builder reuse / Product independence
    @Test
    void builderReuseShouldNotChangePreviouslyBuiltProduct() {
        DroneMission.Builder builder = new DroneMission.Builder(
                "Reusable Mission",
                MissionType.BASIC,
                "Astana"
        );

        DroneMission firstMission = builder
                .altitude(100)
                .build();

        builder.altitude(300);

        DroneMission secondMission = builder.build();

        assertEquals(100, firstMission.getAltitude());
        assertEquals(300, secondMission.getAltitude());
    }

    // 11. Preset configuration verification: Basic
    @Test
    void shouldCreateBasicMissionFromPreset() {
        DroneMissionPresets presets = new DroneMissionPresets();
        DroneMission mission = presets.createBasicMission(
                "Urban Delivery",
                "Astana"
        );

        assertEquals("Urban Delivery", mission.getMissionName());
        assertEquals(MissionType.BASIC, mission.getMissionType());
        assertEquals("Astana", mission.getDestination());
        assertEquals(100, mission.getAltitude());
        assertEquals(40, mission.getSpeed());
        assertEquals(4000, mission.getBatteryCapacity());
        assertFalse(mission.isGpsEnabled());
        assertFalse(mission.isCameraEnabled());
        assertTrue(mission.isReturnToHome());
        assertTrue(mission.isObstacleAvoidance());
    }

    // 12. Preset configuration verification: Survey
    @Test
    void shouldCreateSurveyMissionFromPreset() {
        DroneMissionPresets presets = new DroneMissionPresets();
        DroneMission mission = presets.createSurveyMission(
                "Forest Survey",
                "Borovoe"
        );

        assertEquals("Forest Survey", mission.getMissionName());
        assertEquals(MissionType.SURVEY, mission.getMissionType());
        assertEquals("Borovoe", mission.getDestination());
        assertEquals(150, mission.getAltitude());
        assertEquals(50, mission.getSpeed());
        assertEquals(5000, mission.getBatteryCapacity());
        assertTrue(mission.isGpsEnabled());
        assertTrue(mission.isCameraEnabled());
        assertEquals(1.0, mission.getPayloadKg());
        assertEquals(60, mission.getDurationMinutes());
    }

    // 13. Preset configuration verification: Long Range
    @Test
    void shouldCreateLongRangeMissionFromPreset() {
        DroneMissionPresets presets = new DroneMissionPresets();
        DroneMission mission = presets.createLongRangeMission(
                "Border Recon",
                "Zaisan"
        );

        assertEquals("Border Recon", mission.getMissionName());
        assertEquals(MissionType.LONG_RANGE, mission.getMissionType());
        assertEquals("Zaisan", mission.getDestination());
        assertEquals(300, mission.getAltitude());
        assertEquals(80, mission.getSpeed());
        assertEquals(6000, mission.getBatteryCapacity());
        assertTrue(mission.isGpsEnabled());
        assertTrue(mission.isCameraEnabled());
        assertEquals(1.5, mission.getPayloadKg());
        assertEquals(90, mission.getDurationMinutes());
    }
}