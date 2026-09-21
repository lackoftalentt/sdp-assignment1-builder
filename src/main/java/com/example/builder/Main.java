package com.example.builder;

public class Main {

    public static void main(String[] args) {

        // 1. Direct Builder usage
        DroneMission customMission = new DroneMission.Builder(
                "Custom Pipeline Patrol",
                MissionType.SURVEY,
                "Caspian Shoreline"
        )
                .altitude(120)
                .speed(65)
                .batteryCapacity(5500)
                .enableGps()
                .enableCamera()
                .payloadKg(2.0)
                .durationMinutes(75)
                .build();

        // 2. Preset configurations
        DroneMissionPresets presets = new DroneMissionPresets();

        DroneMission basic = presets.createBasicMission(
                "Basic Delivery",
                "Astana"
        );

        DroneMission survey = presets.createSurveyMission(
                "Mountain Survey",
                "Almaty Mountains"
        );

        DroneMission longRange = presets.createLongRangeMission(
                "Long Range Mission",
                "Steppe Region"
        );

        System.out.println("=== DIRECT BUILDER USAGE ===");
        printMission(customMission);

        System.out.println("\n=== BASIC PRESET ===");
        printMission(basic);

        System.out.println("\n=== SURVEY PRESET ===");
        printMission(survey);

        System.out.println("\n=== LONG RANGE PRESET ===");
        printMission(longRange);
    }

    private static void printMission(DroneMission mission) {
        System.out.println("Mission: " + mission.getMissionName());
        System.out.println("Type: " + mission.getMissionType());
        System.out.println("Destination: " + mission.getDestination());
        System.out.println("Altitude: " + mission.getAltitude() + " m");
        System.out.println("Speed: " + mission.getSpeed() + " km/h");
        System.out.println("Battery: " + mission.getBatteryCapacity() + " mAh");
        System.out.println("GPS: " + mission.isGpsEnabled());
        System.out.println("Camera: " + mission.isCameraEnabled());
        System.out.println("Return to Home: " + mission.isReturnToHome());
        System.out.println("Obstacle Avoidance: " + mission.isObstacleAvoidance());
        System.out.println("Payload: " + mission.getPayloadKg() + " kg");
        System.out.println("Duration: " + mission.getDurationMinutes() + " min");
    }
}