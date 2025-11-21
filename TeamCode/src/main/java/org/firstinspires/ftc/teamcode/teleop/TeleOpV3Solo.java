package org.firstinspires.ftc.teamcode.teleop;

import android.graphics.Bitmap;
import android.graphics.Camera;

import com.acmerobotics.dashboard.FtcDashboard;
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
    double targetFlywheelVel = 1500;
    boolean rightTrigPress;
    boolean rightStickPress;
    boolean leftStickPress;
    boolean lastRightTrigState = false;
    boolean spinFlywheel = false;
    int state = 0;
    boolean lastLeftBumpState = false;
    boolean lastRightBumpState = false;
    boolean lastRightStickPressState = false;
    boolean lastLeftStickPressState = false;
    boolean flywheelReady = false;
    Pose2d shotTarget = null;
    boolean isShotTargetBlue = true;
    boolean useTargetingSystem = false;
    @Override
    public void init() {
        drive = new MecanumDrive(hardwareMap, new Pose2d(0,0,0));
        flywheel = new Flywheel(hardwareMap);
        intake = new Intake(hardwareMap, new ElapsedTime());
        cheeksKicker = new CheeksKicker(hardwareMap);
        aprilTag = new AprilTagBruce(hardwareMap);

    }

    @Override
    public void start() {

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
        rightStickPress = gamepad1.dpad_right;
        leftStickPress = gamepad1.dpad_left;

        //set drive powers
        drive.setDrivePowers(drive.inputToPoseVelocity2D(lx, ly, rx, leftTrigger));

        //intake control (x = intake, y = reject)
        //if no buttons are pressed do not spin intake
        if (x){
            intake.gulp();
        } else if (y){
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
        if (leftBump && !lastLeftBumpState && state < 3) {
            state++;
            cheeksKicker.intakeState(state);
        }
        lastLeftBumpState = leftBump;

        //decrement state by one and cycle launch on right bump press
        if (rightBump && !lastRightBumpState && state > 1) {
            state--;
            cheeksKicker.launchState(state, flywheel.flywheelReady(targetFlywheelVel));
        }
        lastRightBumpState = rightBump;



        //implement code for targeting system

        //0.determine what basket we are shooting at (toggle switch)

        //if true, blue, if false, red
        if (rightStickPress && !lastRightStickPressState) {
            isShotTargetBlue = !isShotTargetBlue;  // Flip the boolean value
        }
        lastRightStickPressState = rightStickPress;

        //1.get the target (basket) x and y coordinates
        if (isShotTargetBlue){
            shotTarget = new Pose2d(-65, -67,0);
        } else {
            shotTarget = new Pose2d(-65, 67,0);
        }
        //2.get the heading relative to your robot and the distance
        //      2a. heading can be done using tangent

        double targetHeading = Math.tan(
                (shotTarget.position.y-drive.localizer.getPose().position.y)
                /(shotTarget.position.x-drive.localizer.getPose().position.x)
        );
        //      2b. distance from target can be done with pythagorean theorem
        double distanceToTarget = Math.hypot(
                shotTarget.position.x-drive.localizer.getPose().position.x,
                shotTarget.position.y-drive.localizer.getPose().position.y);
        //using distance from the basket, calculate flywheel velocity
        //TODO: determine wheel velocity based on target distance equation
        targetFlywheelVel = distanceToTarget/50 * 1350;
        //4.convert the right stick input on the joystick to be a calculated input relative to the difference in headings of the
        //current heading of the robot and the target heading of the basket

        //toggle wether or not to use targeting system
        if (leftStickPress && !lastLeftStickPressState) {
            useTargetingSystem = !useTargetingSystem;  // Flip the boolean value
        }
        lastLeftStickPressState = leftStickPress;

        //if we are using targeting system, change joystick input. if not, use regular input.
        if (useTargetingSystem) {
            rx = calculateJoystick(shotTarget, drive.localizer.getPose());
        } else {
            rx = -gamepad1.right_stick_x;
        }

        double dx = shotTarget.position.x - drive.localizer.getPose().position.x;
        double dy = shotTarget.position.y - drive.localizer.getPose().position.y;

        double idealHeading = Math.atan2(dy,dx);

        double error = targetHeading - drive.localizer.getPose().heading.real;

        double joystick = error * kP;
        //5.when the angle is correct and the flywheel are correct, launch turn light green and launch ball

        //TODO: Implement april tag position correction system

        drive.localizer.update();
        aprilTag.update();

        if (aprilTag.getTagOfId(24) != null || aprilTag.getTagOfId(20) != null){
            drive.localizer.setPose(aprilTag.getPositionPose2d(24));
        } else if (aprilTag.getTagOfId(24) != null){
            drive.localizer.setPose(aprilTag.getPositionPose2d(24));
        } else if (aprilTag.getTagOfId(20) != null){
            drive.localizer.setPose(aprilTag.getPositionPose2d(20));
        }



        telemetry.addData("Camera Position", aprilTag.getPositionPose2d(24));
        telemetry.addData("Deadwheel Position", drive.localizer.getPose());
        telemetry.addData("target heading", Math.toDegrees(idealHeading));
        telemetry.addData("headingError", Math.toDegrees(error));
        telemetry.addData("joystick output", joystick);
        telemetry.addData("is target blue?", isShotTargetBlue);
        telemetry.update();



        FtcDashboard.getInstance().startCameraStream(aprilTag.cameraStreamSource(),5);




    }
    double kP = 0.3;      // Responsiveness (0.05-0.3 usually good)
    double maxSpeed = 1.0; // Maximum joystick output

    public double calculateJoystick (Pose2d target, Pose2d current){

        double dx = target.position.x - current.position.x;
        double dy = target.position.y - current.position.y;

        double targetHeading = Math.atan2(dy,dx);

        double error = ((targetHeading - current.heading.real + 3 * Math.PI) % (2 * Math.PI)) - Math.PI;

        return error * kP;

    }


}
