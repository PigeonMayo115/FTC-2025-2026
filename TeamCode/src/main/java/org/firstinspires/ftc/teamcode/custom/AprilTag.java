package org.firstinspires.ftc.teamcode.custom;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.TagType;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import java.util.List;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

// I have no idea what 90% of this code does but I think it works
public class AprilTag {
    private final VisionPortal visionPortal;
    private final AprilTagProcessor tagProcessor;

    private Position cameraPosition = new Position(DistanceUnit.INCH, 0, 0, 0, 0);
    private YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(AngleUnit.DEGREES, 0, -90, 0, 0);

    //<editor-fold>

    // ✅ Constructor now accepts the hardwareMap from your OpMode
    public AprilTag(HardwareMap hardwareMap) {
        tagProcessor = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setCameraPose(cameraPosition, cameraOrientation)
                .build();

        visionPortal = new VisionPortal.Builder()
                .addProcessor(tagProcessor)
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setCameraResolution(new Size(640, 480))
                .setStreamFormat(VisionPortal.StreamFormat.YUY2)
                .build();
    }

    // detection.robotPose is basically the robot position btw

    private List<AprilTagDetection> currentDetections; // Declare here so code no explode

    public void updateTagInfo() {
        // Update the list currentDetections update with info from the processor (idk ripped from sample)
        currentDetections = tagProcessor.getDetections();
    }

    public int numTagsDetected() {
        if (currentDetections == null) return 0;
        return currentDetections.size();
    }

    // I literally don't know why the return type is AprilTagDetection
    // Prob because the type of data its returning is in the type of the list...?
    public AprilTagDetection getTagInfo(int tag) {
        if (currentDetections == null || tag < 0 || tag >= currentDetections.size()) return null;
        return currentDetections.get(tag);
    }

    // I don't recommend the usage of this as this could use the obelisk tag
    public Position getPosition() {
        // Gets position from first detected tag
        AprilTagDetection detection = getTagInfo(0);
        if (detection == null || detection.robotPose == null) return null;
        return detection.robotPose.getPosition();
    }

    public Position getPositionConfident() {
        if (currentDetections == null) return null;

        try {
            TagType id = getTagMeaning();
            AprilTagDetection detection = getTagInfo(0);
            if (detection == null || detection.robotPose == null) return null;
            if (id == TagType.BLUE_GATE || id == TagType.RED_GATE) {
                return detection.robotPose.getPosition();
            }
        } catch (Exception ex) {
            // todo: log error
        }

        // If you can't find a "confident" tag (aka one of the goals)
        return null;
    }

    public YawPitchRollAngles getOrientation() {
        // Gets position from first detected tag
        AprilTagDetection detection = getTagInfo(0);
        if (detection == null || detection.robotPose == null) return null;
        return detection.robotPose.getOrientation();
    }

    public int getId() {
        AprilTagDetection detection = getTagInfo(0);
        if (detection == null) return -1; // If noting found return -1
        return detection.id;
    }

    //</editor-fold>

    public TagType getTagMeaning()  {
        int id = getId();

        return _getTagMeaning(id);
    }

    public TagType getTagMeaning(int id)  {
        return _getTagMeaning(id);
    }

    private TagType _getTagMeaning(int id) {
        switch (id) {
            case 21:
                return TagType.GPP;
            case 22:
                return TagType.PGP;
            case 23:
                return TagType.PPG;
            case 20:
                return TagType.BLUE_GATE;
            case 24:
                return TagType.RED_GATE;
            default:
                return TagType.UNKNOWN;
        }
    }

    public double getConfidence(int id) {
        // Don't ask me for help here just know bigger is better (0-1)
        for (AprilTagDetection detection : currentDetections) {
            if (detection.id == id) {
                return detection.decisionMargin;
            }
        }
        return 0.0; // no match found
    }

    public boolean isLookingAtTag(TagType tag) {
        currentDetections = tagProcessor.getDetections();
        AprilTagDetection detection = currentDetections.get(0); // Get tag info from first detected tag
        int id = detection.id;

        TagType result = this.getTagMeaning(id);

        return result == tag;
    }

    public Pose2D getPositionRelativeToTag(TagType tag) {
        if (isLookingAtTag(tag)) {
            // return position relative to the tag
            AprilTagDetection detection = currentDetections.get(0); // Get tag info from first detected tag

            Position pos = detection.robotPose.getPosition();
            // todo: figure out how to convert from a Position object to a Pose2D
//            Pose2D output = new Pose2D(pos.unit, pos.x, pos.y, );
        }

        return null;
    }

    /* Yeah no too hard
    public double distFromTag() {
        double actualSize = 0.206375; // (Meters) I think??? In google ai overview we trust
        double detectedSize = detection.metadata.tagsize; // Also this errors
        return dist;
    }
     */
}
