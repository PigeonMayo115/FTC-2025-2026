// todo - get distance from apriltag and get field position
// todo late - am I lined up with target and what is my distance to the target (basically firing accuracy)

package org.firstinspires.ftc.teamcode.george;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import org.firstinspires.ftc.teamcode.TagType;

import java.util.List;

public class AprilTag {
    private final VisionPortal visionPortal;
    private final AprilTagProcessor tagProcessor;
    /**
     * Variables to store the position and orientation of the camera on the robot. Setting these
     * values requires a definition of the axes of the camera and robot:
     *
     * Camera axes:
     * Origin location: Center of the lens
     * Axes orientation: +x right, +y down, +z forward (from camera's perspective)
     *
     * Robot axes (this is typical, but you can define this however you want):
     * Origin location: Center of the robot at field height
     * Axes orientation: +x right, +y forward, +z upward
     *
     * Position:
     * If all values are zero (no translation), that implies the camera is at the center of the
     * robot. Suppose your camera is positioned 5 inches to the left, 7 inches forward, and 12
     * inches above the ground - you would need to set the position to (-5, 7, 12).
     *
     * Orientation:
     * If all values are zero (no rotation), that implies the camera is pointing straight up. In
     * most cases, you'll need to set the pitch to -90 degrees (rotation about the x-axis), meaning
     * the camera is horizontal. Use a yaw of 0 if the camera is pointing forwards, +90 degrees if
     * it's pointing straight left, -90 degrees for straight right, etc. You can also set the roll
     * to +/-90 degrees if it's vertical, or 180 degrees if it's upside-down.
     */
    private Position cameraPosition = new Position(DistanceUnit.INCH, -3, 1, 9.5, 0);
    private YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(AngleUnit.DEGREES, 0, -70, 0, 0);

    public AprilTag(HardwareMap hardwareMap) {
        // Constructor - configures and starts the visionPortal (Video feed) and tagProcessor (Image processing)
        tagProcessor = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setCameraPose(cameraPosition, cameraOrientation)
                // https://ftc-docs.firstinspires.org/en/latest/programming_resources/vision/camera_calibration/camera-calibration.html
                .setLensIntrinsics(941.675, 941.675, 336.763, 221.944) // Heya you kinda need to calibrate the camera
                .build();

        visionPortal = new VisionPortal.Builder()
                .addProcessor(tagProcessor)
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setCameraResolution(new Size(640, 480))
                .setStreamFormat(VisionPortal.StreamFormat.YUY2)
                .build();
    }

    private List<AprilTagDetection> currentDetections;

    public void updateTagInfo() {
        // Update the list currentDetections update with info from the processor
        currentDetections = tagProcessor.getDetections();
    }
    public AprilTagDetection getTagInfoRaw(int id) {
        return currentDetections.get(id);
    }
    public TagType getTagInfo(TagType type, int id) {
        // Overload wrapper for _getTagInfo()
        return _getTagInfo(type, id);
    }
    public TagType getTagInfo(TagType type) {
        // Overload wrapper for _getTagInfo()
        return _getTagInfo(type, 0); // Get info for tag id 0 (first)
    }
     public Position getPosition(int id) {
        // Returns pos.x, pos.y, pos.z of the bot to the april tag
        AprilTagDetection detection = getTagInfoRaw(id);
        return detection.robotPose.getPosition();
    }

    private TagType _getTagInfo(TagType type, int id) {
        // On second thought I don't think that this will work because I can't assign a TagType enumeration a number or something.
        switch (type) {
            case meaning:
                // Returns TagType.GPP or similar
                return AprilTagFunctions.getMeaning(id);
            case istrackable:
                // Returns if the tag should be tracked for position or not
                return AprilTagFunctions.isTrackable(id);
            default:
                return TagType.UNKNOWN;
        }
    }

    static class AprilTagFunctions {
        // Honestly this class kinda just holds the big functions for the _getTagInfo() method
        private static TagType getMeaning(int id) {
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

        private static TagType isTrackable(int id) {
            switch (id) {
                case 21:
                case 22:
                case 23:
                case 20:
                case 24:
                    return TagType.TRACKABLE;
                default:
                    return TagType.UNTRACKABLE;
            }
        }
    }
}