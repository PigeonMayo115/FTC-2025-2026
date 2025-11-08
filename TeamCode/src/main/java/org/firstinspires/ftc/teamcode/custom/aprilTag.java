package org.firstinspires.ftc.teamcode.custom;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import android.util.Size;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import java.util.List;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

// I have no idea what 90% of this code does but I think it works
public class aprilTag {
    private Position cameraPosition = new Position(DistanceUnit.INCH,
            0, 0, 0, 0);
    private YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(AngleUnit.DEGREES,
            0, -90, 0, 0);

    // AprilTagProcesser actually processes the april tag for data
    AprilTagProcessor tagProcessor = new AprilTagProcessor.Builder()
            .setDrawAxes(true) // Draws xyz axes on the april tag
            .setDrawCubeProjection(true) // Draws a cube projection of the tag
            .setDrawTagID(true) // Draws a box with the tag id on it
            .setDrawTagOutline(true) // Draws a outline of the tag
            .setCameraPose(cameraPosition, cameraOrientation) // Set camera position - kinda important
            .build();

    // VisionPortal feeds images from camera to the processer (Handles video)
    VisionPortal visionPortal = new VisionPortal.Builder()
            .addProcessor(tagProcessor) // Adds a processer (god no way)
            .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1")) // idk why hardwareMap is italicized look at line 3
            .setCameraResolution(new Size(640, 480))
            .setStreamFormat(VisionPortal.StreamFormat.YUY2) // Set video stream format (YUY2 or MJPEG) ref http://localhost:63342/FTC-2025-2026/Vision-11.0.0-javadoc.jar/org/firstinspires/ftc/vision/VisionPortal.StreamFormat.html
            .build();

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
        if (currentDetections == null || tag > currentDetections.size()) return null;
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

        // Google my beloved - Couldn't figure this out
        for (AprilTagDetection detection : currentDetections) {
            if (detection.id == 20 || detection.id == 24) {
                if (detection.robotPose != null) {
                    return detection.robotPose.getPosition();
                }
            }
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

    public String getTagMeaning() {
        int id = getId();
        String type = "";
        switch (id) {
            case 21:
                type = "gpp";
            case 22:
                type = "pgp";
            case 23:
                type = "ppg";
            case 20:
                type = "blue";
            case 24:
                type = "red";
            default:
                type = null; //todo - should null be something else?
        }
        return type;
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

    /* Yeah no too hard
    public double distFromTag() {
        double actualSize = 0.206375; // (Meters) I think??? In google ai overview we trust
        double detectedSize = detection.metadata.tagsize; // Also this errors
        return dist;
    }
     */
}
