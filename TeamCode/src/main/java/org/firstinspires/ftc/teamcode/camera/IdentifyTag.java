package org.firstinspires.ftc.teamcode.camera;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.TagType;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class IdentifyTag {
    private final VisionPortal visionPortal;
    private final AprilTagProcessor tagProcessor;
    private Position cameraPosition = new Position(DistanceUnit.INCH, 0, 0, 0, 0);
    private YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(AngleUnit.DEGREES, 0, -90, 0, 0);

    public IdentifyTag(HardwareMap hardwareMap) {
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

    private List<AprilTagDetection> currentDetections;

    public void updateTagInfo() {
        currentDetections = tagProcessor.getDetections();
    }

    public AprilTagDetection getTagInfo(int tag) {
        if (currentDetections == null || tag < 0 || tag >= currentDetections.size()) return null;
        return currentDetections.get(tag);
    }

    public TagType identify(int tagId) {
        AprilTagDetection detection = getTagInfo(tagId);
        int id = detection.id;

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
        }
        return TagType.UNKNOWN;
    }
}


