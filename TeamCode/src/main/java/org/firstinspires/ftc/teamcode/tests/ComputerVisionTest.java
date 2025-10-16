package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.custom.VisionProcessor;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

@TeleOp
public class ComputerVisionTest extends LinearOpMode {

    VisionProcessor visionProcessor;


    @Override
    public void runOpMode() throws InterruptedException {
        visionProcessor = new VisionProcessor(
                hardwareMap,
                telemetry,
                new Position(DistanceUnit.INCH, 0, 0, 0, 0),
                new YawPitchRollAngles(AngleUnit.DEGREES, 0, -90, 0, 0)
        );
        waitForStart();

        while (opModeIsActive()){
            visionProcessor.tagTelemetry();

            telemetry.update();

            if (gamepad1.dpad_down) {
                visionProcessor.visionPortal.stopStreaming();
            } else if (gamepad1.dpad_up) {
                visionProcessor.visionPortal.resumeStreaming();
            }

            sleep(20);
        }

        visionProcessor.visionPortal.close();
        //TODO orange you glad I didn't say banana

    }

}
