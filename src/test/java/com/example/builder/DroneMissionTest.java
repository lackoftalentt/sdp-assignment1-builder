package test.java.com.example.builder;

import main.java.com.example.builder.DroneMission;
import main.java.com.example.builder.MissionType;
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
}