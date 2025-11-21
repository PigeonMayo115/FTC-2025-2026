package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.TagType;
import org.firstinspires.ftc.teamcode.george.AprilTagOld;

@TeleOp
public class AprilTagTest extends OpMode {

    private AprilTagOld tagHelper;

    @Override
    public void init() {
        tagHelper = new AprilTagOld(hardwareMap);
    }

    @Override
    public void loop() {
        tagHelper.updateTagInfo();

        telemetry.addData("Tags Detected", tagHelper.numTagsDetected());

        int id = tagHelper.getId();
        telemetry.addData("First Tag ID", id);
        telemetry.addData("Tag confidence", tagHelper.getConfidence(id));
        try {
            telemetry.addData("Tag Meaning", tagHelper.getTagMeaning());
        } catch (Exception ex) {
            telemetry.addData("Tag Meaning", "Error: " + ex);
        }

        Position pos = tagHelper.getPosition();
        if (pos != null) {
            telemetry.addData("Robot Position", "(%.1f, %.1f, %.1f) in", pos.x, pos.y, pos.z);
        }

        Position posConfident = tagHelper.getPositionConfident();
        if (posConfident != null) {
            telemetry.addData("Robot Position Confident", "(%.1f, %.1f, %.1f) in", posConfident.x, posConfident.y, posConfident.z);
        }

        YawPitchRollAngles rot = tagHelper.getOrientation();
        if (rot != null) {
            telemetry.addData("Robot Orientation", "(Yaw=%.1f°, Pitch=%.1f°, Roll=%.1f°)",
                    rot.getYaw(AngleUnit.DEGREES),
                    rot.getPitch(AngleUnit.DEGREES),
                    rot.getRoll(AngleUnit.DEGREES));
        }

        try {
            TagType meaning = tagHelper.getTagMeaning();
            telemetry.addData("Tag Meaning", meaning);
        } catch (Exception ex) {
            telemetry.addData("Tag Meaning", "Nothing found lmao:" + ex);
        }

        telemetry.update();
    }
}
