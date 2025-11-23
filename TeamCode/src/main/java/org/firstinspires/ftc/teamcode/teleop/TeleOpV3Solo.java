package org.firstinspires.ftc.teamcode.teleop;

import android.graphics.Bitmap;
import android.graphics.Camera;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.custom.AprilTag;
import org.firstinspires.ftc.teamcode.custom.AprilTagBruce;
import org.firstinspires.ftc.teamcode.custom.CheeksKicker;
import org.firstinspires.ftc.teamcode.custom.Feeder;
import org.firstinspires.ftc.teamcode.custom.Flywheel;
import org.firstinspires.ftc.teamcode.custom.Intake;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvWebcam;

@TeleOp
public class TeleOpV3Solo extends OpMode {
    MecanumDrive drive = null;
    Feeder feeder = null;
    Flywheel flywheel = null;
    Intake intake = null;
    CheeksKicker cheeksKicker = null;
    AprilTagBruce aprilTag = null;
    CameraStreamSource camera = null;
    double lx;
    double ly;
    double rx;
    boolean a;
    boolean b;

    boolean x;
    boolean y;
    boolean dPadUp;
    boolean dPadDown;
    boolean leftBump;
    boolean rightBump;
    double leftTrigger;
    double rightTrigger;
    double targetFlywheelVel = 1180;
    boolean rightTrigPress;
    boolean rightStickPress;
    boolean leftStickPress;
    boolean lastRightTrigState = false;
    boolean spinFlywheel = false;
    boolean lastLeftBumpState = false;
    boolean lastRightBumpState = false;
    boolean lastRightStickPressState = false;
    boolean lastLeftStickPressState = false;
    boolean flywheelReady = false;
    Pose2d shotTarget = null;
    boolean isShotTargetBlue = true;
    boolean useTargetingSystem = false;
    boolean lastLeftDpadState = false;
    boolean lastRightDpadState = false;
    boolean lastAState = false;
    boolean useAutoFlywheelSpeed = false;
    double targetingRX = 0.0;
    @Override
    public void init() {
        drive = new MecanumDrive(hardwareMap, new Pose2d(0,0,0));
        flywheel = new Flywheel(hardwareMap);
        intake = new Intake(hardwareMap, new ElapsedTime());
        cheeksKicker = new CheeksKicker(hardwareMap, new ElapsedTime());
        aprilTag = new AprilTagBruce(hardwareMap);

    }

    @Override
    public void start() {
        cheeksKicker.kickerRetract();
        cheeksKicker.leftUp();
        cheeksKicker.rightUp();
    }

    @Override
    public void loop() {
        ly = gamepad1.left_stick_y;
        lx = gamepad1.left_stick_x;
        a = gamepad1.a;
        b = gamepad1.b;
        x = gamepad1.x;
        y = gamepad1.y;
        dPadUp = gamepad1.dpad_up;
        dPadDown = gamepad1.dpad_down;
        leftBump = gamepad1.left_bumper;
        rightBump = gamepad1.right_bumper;
        leftTrigger = gamepad1.left_trigger;
        rightTrigPress = gamepad1.right_trigger >= 0.7;
        rightStickPress = gamepad1.right_stick_button;
        leftStickPress = gamepad1.left_stick_button;
        boolean leftDpad = gamepad1.dpad_left;
        boolean rightDpad = gamepad1.dpad_right;

        //set drive powers
        drive.setDrivePowers(drive.inputToPoseVelocity2D(lx, ly, rx, leftTrigger));

        //intake control (x = intake, y = reject)
        //if no buttons are pressed do not spin intake
        if (y){
            intake.gulp();
        } else if (x){
            intake.belch();
        } else {
            intake.hold();
        }

        //toggle switch for right trigger
        //if right trigger is pressed beyond 0.7, spin up flywheel
        //toggle if pressed again.
        if (rightTrigPress && !lastRightTrigState) {
            spinFlywheel = !spinFlywheel;
        }
        lastRightTrigState = rightTrigPress;

        if (spinFlywheel){
            flywheel.setVelocity(targetFlywheelVel);
        } else {
            flywheel.setVelocity(0);
        }

        //cycle the intake system
        //lb launch a ball and cycle to the next and increment the state up by one
        //rb is cycle the intake to get ready for the next ball and decrement by one


        //increment state by one and cycle intake on left bump press
        /*
        if (leftDpad && !lastLeftDpadState && state < 3) {
            state++;
            cheeksKicker.intakeState(state);
        }
        lastLeftDpadState = leftDpad;

        //decrement state by one and cycle launch on right bump press
        if (rightDpad && !lastRightDpadState && state > 1) {
            state--;
            cheeksKicker.launchState(state);
        }
        lastRightDpadState = rightDpad;
        */

        cheeksKicker.update(gamepad1.dpad_left,gamepad1.dpad_right);
        if (b){
            cheeksKicker.kickerExtend();
        } else {
            cheeksKicker.kickerRetract();
        }
/*
        if (a && !lastAState) {
            useAutoFlywheelSpeed = !useAutoFlywheelSpeed;
        }
        lastAState = a;
*/
        if (isShotTargetBlue){
            shotTarget = new Pose2d(-65, 67,0);
        } else {
            shotTarget = new Pose2d(65, 67,0);
        }

        double distanceToTarget = Math.hypot(
                shotTarget.position.x-drive.localizer.getPose().position.x,
                shotTarget.position.y-drive.localizer.getPose().position.y);

        if (useAutoFlywheelSpeed){
            targetFlywheelVel = distanceToTarget/50 * 1350;
        } else {
            if (rightBump && !lastRightBumpState) {
                targetFlywheelVel += 20;
            }
            lastRightBumpState = rightBump;

            if (leftBump && !lastLeftBumpState) {
                targetFlywheelVel -= 20;
            }
            lastLeftBumpState = leftBump;
        }



        //implement code for targeting system

        //0.determine what basket we are shooting at (toggle switch)

        //if true, blue, if false, red
        if (rightStickPress && !lastRightStickPressState) {
            isShotTargetBlue = !isShotTargetBlue;  // Flip the boolean value
        }
        lastRightStickPressState = rightStickPress;

        //1.get the target (basket) x and y coordinates

        //2.get the heading relative to your robot and the distance
        //      2a. heading can be done using tangent

        //      2b. distance from target can be done with pythagorean theorem
        //using distance from the basket, calculate flywheel velocity
        //TODO: determine wheel velocity based on target distance equation

        if (leftStickPress && !lastLeftStickPressState) {
            useTargetingSystem = !useTargetingSystem;  // Flip the boolean value
        }
        lastLeftStickPressState = leftStickPress;


        //4.convert the right stick input on the joystick to be a calculated input relative to the difference in headings of the
        //current heading of the robot and the target heading of the basket



        //if we are using targeting system, change joystick input. if not, use regular input.

        double dx = shotTarget.position.x - drive.localizer.getPose().position.x;
        double dy = shotTarget.position.y - drive.localizer.getPose().position.y;

        double targetHeading = Math.atan2(dy,dx);

        double error = targetHeading - drive.localizer.getPose().heading.toDouble();

        error = Math.atan2(Math.sin(error), Math.cos(error));

        double kP = 1.5;

        targetingRX = error * kP;
        telemetry.addData("current heading", Math.toDegrees(drive.localizer.getPose().heading.toDouble()));
        telemetry.addData("target heading", Math.toDegrees(targetHeading));
        telemetry.addData("headingError", Math.toDegrees(error));
        telemetry.addData("joystick output", targetingRX);
        if (useTargetingSystem) {
            rx = targetingRX;
        } else {
            rx = -gamepad1.right_stick_x;

        }

        //5.when the angle is correct and the flywheel are correct, launch turn light green and launch ball

        //TODO: Implement april tag position correction system

        drive.localizer.update();
        aprilTag.update();

        /*
        if (aprilTag.getTagOfId(24) != null && aprilTag.getTagOfId(20) != null){
            drive.localizer.setPose(aprilTag.getPositionPose2d(24));
        } else if (aprilTag.getTagOfId(24) != null){
            drive.localizer.setPose(aprilTag.getPositionPose2d(24));
        } else if (aprilTag.getTagOfId(20) != null){
            drive.localizer.setPose(aprilTag.getPositionPose2d(20));
        }
*/
        if (dPadDown){
            drive.localizer.setPose(new Pose2d(0,0,drive.localizer.getPose().heading.toDouble()));
        }

        telemetry.addData("Camera Position", aprilTag.getPositionPose2d(24));
        telemetry.addData("Deadwheel Position", drive.localizer.getPose());
        telemetry.addData("is target blue?", isShotTargetBlue);
        telemetry.addData("flywheel velocity", targetFlywheelVel);
        telemetry.update();

        TelemetryPacket heading = new TelemetryPacket();

        heading.put("current Heading", Math.toDegrees(drive.localizer.getPose().heading.toDouble()));

        FtcDashboard dashboard = FtcDashboard.getInstance();
        TelemetryPacket packet = new TelemetryPacket();
        Canvas fieldOverlay = packet.fieldOverlay();

        fieldOverlay.strokeCircle(drive.localizer.getPose().position.x, drive.localizer.getPose().position.y, 9);

        fieldOverlay.strokeCircle(shotTarget.position.x, shotTarget.position.y, 7);


        dashboard.sendTelemetryPacket(packet);


        FtcDashboard.getInstance().startCameraStream(aprilTag.cameraStreamSource(),5);




    }


}
