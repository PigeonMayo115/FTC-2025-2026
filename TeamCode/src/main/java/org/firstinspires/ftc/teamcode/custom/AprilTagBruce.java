package org.firstinspires.ftc.teamcode.custom;

import android.util.Size;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;

public class AprilTagBruce {
    private final VisionPortal visionPortal;
    private final AprilTagProcessor tagProcessor;

    private Position cameraPosition = new Position(DistanceUnit.INCH, 4.75, -3.5, 16.5, 0);
    private YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(AngleUnit.DEGREES, 0, -70, 0, 0);

    private List<AprilTagDetection> detectedTags = new ArrayList<>();

    public AprilTagBruce(HardwareMap hardwareMap) {
        // Constructor - configures and starts the visionPortal (Video feed) and tagProcessor (Image processing)
        tagProcessor = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setCameraPose(cameraPosition, cameraOrientation)
                .setLensIntrinsics( 813.692, 813.692, 362.284,245.145)
                .setOutputUnits(DistanceUnit.INCH,AngleUnit.RADIANS)
                // https://ftc-docs.firstinspires.org/en/latest/programming_resources/vision/camera_calibration/camera-calibration.html
                .build();

        visionPortal = new VisionPortal.Builder()
                .addProcessor(tagProcessor)
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setCameraResolution(new Size(640, 480))
                .setStreamFormat(VisionPortal.StreamFormat.YUY2)
                .build();
    }

    public void update (){
        detectedTags = tagProcessor.getDetections();
    }

    public List<AprilTagDetection> getDetectedTags(){return detectedTags;}

    public AprilTagDetection getTagOfId(int id){
        for(AprilTagDetection detection : detectedTags){
            if (detection.id == id){
                return detection;
            }
        }
        return null;
    }

    public Pose2d getPositionPose2d (int id){
        AprilTagDetection detection = getTagOfId(id);
        if (detection == null){
            return null;
        } else {
            return new Pose2d(
                    detection.robotPose.getPosition().x,
                    detection.robotPose.getPosition().y,
                    detection.robotPose.getOrientation().getYaw(AngleUnit.RADIANS)
            );
        }
    }

    public CameraStreamSource cameraStreamSource(){
        return visionPortal;
    }


}
