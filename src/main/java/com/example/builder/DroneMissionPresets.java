package main.java.com.example.builder;

public class DroneMissionPresets {

    public DroneMission createBasicMission(
            String name,
            String destination
    ) {
        return new DroneMission.Builder(
                name,
                MissionType.BASIC,
                destination
        )
                .altitude(100)
                .speed(40)
                .batteryCapacity(4000)
                .build();
    }

    public DroneMission createSurveyMission(
            String name,
            String destination
    ) {
        return new DroneMission.Builder(
                name,
                MissionType.SURVEY,
                destination
        )
                .altitude(150)
                .speed(50)
                .batteryCapacity(5000)
                .enableGps()
                .enableCamera()
                .payloadKg(1.0)
                .durationMinutes(60)
                .build();
    }

    public DroneMission createLongRangeMission(
            String name,
            String destination
    ) {
        return new DroneMission.Builder(
                name,
                MissionType.LONG_RANGE,
                destination
        )
                .altitude(300)
                .speed(80)
                .batteryCapacity(6000)
                .enableGps()
                .enableCamera()
                .payloadKg(1.5)
                .durationMinutes(90)
                .build();
    }
}