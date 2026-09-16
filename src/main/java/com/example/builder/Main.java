package main.java.com.example.builder;

public class Main {

    public static void main(String[] args) {

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

        System.out.println("=== BASIC ===");
        printMission(basic);

        System.out.println("\n=== SURVEY ===");
        printMission(survey);

        System.out.println("\n=== LONG RANGE ===");
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
    }
}