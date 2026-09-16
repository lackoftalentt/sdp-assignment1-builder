import matplotlib.pyplot as plt
import matplotlib.patches as patches
from matplotlib.patches import FancyBboxPatch

def create_uml():
    fig, ax = plt.subplots(figsize=(24, 16), dpi=300)
    ax.set_xlim(0, 225)
    ax.set_ylim(0, 155)
    ax.axis('off')

    # Color palette
    bg_color = "#F8FAFC"
    border_color = "#1E293B"
    header_bg_client = "#EEF2FF"
    header_bg_product = "#E0F2FE"
    header_bg_builder = "#FEF3C7"
    header_bg_enum = "#F1F5F9"
    header_bg_preset = "#DCFCE7"
    box_bg = "#FFFFFF"
    text_color = "#0F172A"
    subtext_color = "#475569"

    fig.patch.set_facecolor(bg_color)
    ax.set_facecolor(bg_color)

    # Title
    ax.text(112.5, 148, "Assignment 1 — Drone Mission Builder Pattern UML Class Diagram",
            fontsize=20, fontweight='bold', ha='center', va='center', color=text_color, fontfamily='DejaVu Sans')
    ax.text(112.5, 143.5, "Package: com.example.builder  |  Language: Java 21  |  Design Pattern: GoF Builder",
            fontsize=12, fontstyle='italic', ha='center', va='center', color=subtext_color, fontfamily='DejaVu Sans')

    def draw_class(x, y, w, h, stereotype, name, header_bg, attributes, methods):
        # Background box
        box = FancyBboxPatch((x, y), w, h, boxstyle="round,pad=0.2,rounding_size=1.2",
                             facecolor=box_bg, edgecolor=border_color, linewidth=1.5)
        ax.add_patch(box)

        # Header box
        header_height = 9.5
        header_box = FancyBboxPatch((x, y + h - header_height), w, header_height,
                                    boxstyle="round,pad=0.2,rounding_size=1.2",
                                    facecolor=header_bg, edgecolor=border_color, linewidth=1.5)
        ax.add_patch(header_box)

        # Stereotype and Class Name
        ax.text(x + w/2, y + h - 2.8, f"<<{stereotype}>>",
                fontsize=9, fontstyle='italic', ha='center', va='center', color="#334155", fontfamily='DejaVu Sans')
        ax.text(x + w/2, y + h - 6.6, name,
                fontsize=11.5, fontweight='bold', ha='center', va='center', color=text_color, fontfamily='DejaVu Sans')

        # Content y start
        curr_y = y + h - header_height - 2.8

        # Attributes
        if attributes:
            for attr in attributes:
                ax.text(x + 2.0, curr_y, attr, fontsize=8.2, ha='left', va='center', color=text_color, fontfamily='DejaVu Sans Mono')
                curr_y -= 2.6
        else:
            ax.text(x + 2.0, curr_y, "(none)", fontsize=8, fontstyle='italic', ha='left', va='center', color="#94A3B8", fontfamily='DejaVu Sans')
            curr_y -= 2.6

        curr_y -= 0.6
        # Divider line between attributes and methods
        ax.plot([x, x + w], [curr_y + 1.2, curr_y + 1.2], color=border_color, linewidth=1.0)

        # Methods
        curr_y -= 1.8
        if methods:
            for meth in methods:
                if meth == "":
                    curr_y -= 1.2
                    continue
                ax.text(x + 2.0, curr_y, meth, fontsize=8.2, ha='left', va='center', color=text_color, fontfamily='DejaVu Sans Mono')
                curr_y -= 2.5
        else:
            ax.text(x + 2.0, curr_y, "(none)", fontsize=8, fontstyle='italic', ha='left', va='center', color="#94A3B8", fontfamily='DejaVu Sans')

    # 1. Client: Main (Top Left)
    draw_class(6, 92, 48, 38, "Client", "Main", header_bg_client,
               [],
               [
                   "+ main(args: String[]): void",
                   "- printMission(m: DroneMission): void"
               ])

    # 2. Preset Component: DroneMissionPresets (Bottom Left)
    draw_class(6, 12, 54, 66, "Preset Component", "DroneMissionPresets", header_bg_preset,
               [],
               [
                   "+ createBasicMission(",
                   "    name: String, dest: String",
                   "  ): DroneMission",
                   "",
                   "+ createSurveyMission(",
                   "    name: String, dest: String",
                   "  ): DroneMission",
                   "",
                   "+ createLongRangeMission(",
                   "    name: String, dest: String",
                   "  ): DroneMission"
               ])

    # 3. Product: DroneMission (Middle)
    drone_attrs = [
        "- missionName: String",
        "- missionType: MissionType",
        "- destination: String",
        "- altitude: int",
        "- speed: int",
        "- batteryCapacity: int",
        "- gpsEnabled: boolean",
        "- cameraEnabled: boolean",
        "- returnToHome: boolean",
        "- obstacleAvoidance: boolean",
        "- payloadKg: double",
        "- durationMinutes: int"
    ]
    drone_methods = [
        "- DroneMission(b: Builder)",
        "+ getMissionName(): String",
        "+ getMissionType(): MissionType",
        "+ getDestination(): String",
        "+ getAltitude(): int",
        "+ getSpeed(): int",
        "+ getBatteryCapacity(): int",
        "+ isGpsEnabled(): boolean",
        "+ isCameraEnabled(): boolean",
        "+ isReturnToHome(): boolean",
        "+ isObstacleAvoidance(): boolean",
        "+ getPayloadKg(): double",
        "+ getDurationMinutes(): int"
    ]
    draw_class(76, 42, 68, 90, "Product", "DroneMission", header_bg_product,
               drone_attrs, drone_methods)

    # 4. Supporting Object: MissionType (Top Right)
    draw_class(160, 108, 44, 24, "enumeration", "MissionType", header_bg_enum,
               [
                   "+ BASIC",
                   "+ SURVEY",
                   "+ LONG_RANGE"
               ],
               [])

    # 5. Builder: DroneMission.Builder (Bottom Right)
    builder_attrs = [
        "- missionName: String",
        "- missionType: MissionType",
        "- destination: String",
        "- altitude: int = 100",
        "- speed: int = 50",
        "- batteryCapacity: int = 4000",
        "- gpsEnabled: boolean = false",
        "- cameraEnabled: boolean = false",
        "- returnToHome: boolean = true",
        "- obstacleAvoidance: boolean = true",
        "- payloadKg: double = 0.0",
        "- durationMinutes: int = 30"
    ]
    builder_methods = [
        "+ Builder(name, type, dest)",
        "+ altitude(v: int): Builder",
        "+ speed(v: int): Builder",
        "+ batteryCapacity(v: int): Builder",
        "+ enableGps(): Builder",
        "+ enableCamera(): Builder",
        "+ disableReturnToHome(): Builder",
        "+ disableObstacleAvoidance(): Builder",
        "+ payloadKg(v: double): Builder",
        "+ durationMinutes(v: int): Builder",
        "+ build(): DroneMission",
        "- validate(): void",
        "- validateRequiredFields(): void",
        "- validatePositiveValues(): void",
        "- validateLongRangeMission(): void"
    ]
    draw_class(152, 6, 62, 94, "Builder", "DroneMission.Builder", header_bg_builder,
               builder_attrs, builder_methods)

    # Relationships & Connectors

    # 1. Main -> DroneMissionPresets (uses)
    ax.annotate("", xy=(30, 78), xytext=(30, 92),
                arrowprops=dict(arrowstyle="->", linestyle="dashed", color="#334155", lw=1.6))
    ax.text(32, 85, "<<uses>>", fontsize=8.5, color="#475569", fontstyle='italic', fontfamily='DejaVu Sans')

    # 2. Main -> DroneMission.Builder (direct build: orthogonal route over top and down right outside MissionType)
    # Main top (30, 130) -> up to 139 -> right to 218 -> down to 75 -> left into Builder (214, 75)
    ax.plot([30, 30, 219, 219, 214], [130, 139, 139, 75, 75], color="#2563EB", linestyle="dashed", linewidth=1.5)
    ax.annotate("", xy=(214, 75), xytext=(217, 75),
                arrowprops=dict(arrowstyle="->", color="#2563EB", lw=1.5))
    ax.text(125, 140.5, "<<direct build: uses>>", fontsize=9, fontweight='bold', color="#2563EB",
            ha='center', va='bottom', fontfamily='DejaVu Sans',
            bbox=dict(boxstyle="round,pad=0.2", fc="#EFF6FF", ec="#93C5FD", lw=0.8))

    # 3. DroneMissionPresets -> DroneMission (creates built missions)
    ax.annotate("", xy=(76, 58), xytext=(60, 58),
                arrowprops=dict(arrowstyle="->", linestyle="dashed", color="#059669", lw=1.6))
    ax.text(68, 60.5, "<<creates>>", fontsize=8, color="#059669", ha='center', fontstyle='italic', fontfamily='DejaVu Sans')

    # 4. DroneMissionPresets -> DroneMission.Builder (configures presets via Builder - channel under DroneMission)
    ax.annotate("", xy=(152, 28), xytext=(60, 28),
                arrowprops=dict(arrowstyle="->", linestyle="dashed", color="#475569", lw=1.6))
    ax.text(106, 30.5, "<<configures preset via Builder>>", fontsize=9, color="#334155",
            ha='center', fontstyle='italic', fontfamily='DejaVu Sans',
            bbox=dict(boxstyle="round,pad=0.2", fc="#F1F5F9", ec="#CBD5E1", lw=0.8))

    # 5. DroneMission.Builder -> DroneMission (builds / instantiates Product)
    ax.annotate("", xy=(144, 72), xytext=(152, 72),
                arrowprops=dict(arrowstyle="->", linestyle="solid", color="#B45309", lw=2.0))
    ax.text(148, 74.5, "<<builds>>", fontsize=8.5, fontweight='bold', color="#B45309",
            ha='center', fontfamily='DejaVu Sans')

    # 6. Nesting notation: Builder is nested static class inside DroneMission
    ax.text(148, 68, "«static member»", fontsize=7.5, fontstyle='italic', color="#78350F",
            ha='center', fontfamily='DejaVu Sans')

    # 7. DroneMission -> MissionType (references)
    ax.annotate("", xy=(160, 120), xytext=(144, 120),
                arrowprops=dict(arrowstyle="->", linestyle="solid", color="#334155", lw=1.6))
    ax.text(146, 122, "1", fontsize=8.5, color="#334155", fontfamily='DejaVu Sans Mono')
    ax.text(157, 122, "1", fontsize=8.5, color="#334155", fontfamily='DejaVu Sans Mono')
    ax.text(152, 116.5, "-missionType", fontsize=8, color="#475569", ha='center', fontfamily='DejaVu Sans')

    plt.tight_layout()
    plt.savefig("docs/builder-uml.png", dpi=300, bbox_inches='tight', facecolor=fig.get_facecolor())
    plt.close()
    print("Regenerated docs/builder-uml.png successfully!")

if __name__ == "__main__":
    create_uml()
