package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class Elliot2025Tele extends OpMode {
    PoseVelocity2d drivePowers = null;
    MecanumDrive drive = null;
    CRServo rightFeeder;
    CRServo leftFeeder;
    private DcMotor mainFeeder = null;
    DcMotorEx launcher;

    // flag for which flywheel mode we're in
    // Comment out all instances of comments lookin like this to enable fully
    //!
    private final boolean dualFlywheel = false;
//    public boolean getDualFlywheel() { return dualFlywheel; } // How to get value of dualFlywheel if I need to ref outside of this class

//    DcMotorEx leftLauncher;
//    DcMotorEx rightLauncher;

    double launcherTargetVelocity = 2000;
    double launcherStandbyVelocity = 0;


    @Override
    public void init() {
        drive = new MecanumDrive(hardwareMap, new Pose2d( 0, 0, 0));
        rightFeeder = hardwareMap.crservo.get("rightFeeder");
        leftFeeder = hardwareMap.crservo.get("leftFeeder");
        mainFeeder = hardwareMap.get(DcMotor.class, "mainFeeder");

        double p = 300;
        double i = 1;
        double d = 4;
        double f = 10;
        if (!dualFlywheel) {
            launcher = hardwareMap.get(DcMotorEx.class, "launcher");
            launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            launcher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(p, i, d, f));
        } else {
//!            leftLauncher = hardwareMap.get(DcMotorEx.class, "leftLauncher");
//!            rightLauncher = hardwareMap.get(DcMotorEx.class, "leftLauncher");
//!            leftLauncher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//!            rightLauncher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//!            leftLauncher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(p, i, d, f));
//!            rightLauncher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(p, i, d, f));
        }
    }

    @Override
    public void loop() {
        // -1 == left stick forward
        // 1 == left stick backward
        double leftY = gamepad1.left_stick_y;

        // 1 == left stick right
        // -1 == left stick left
        double leftX = gamepad1.left_stick_x;

        // -1 == left stick forward
        // 1 == left stick backward
//        double rightY = gamepad1.right_stick_y;

        // 1 == left stick right
        // -1 == left stick left
        double rightX = gamepad1.right_stick_x;

        // 1 = Pressed Down
        // 0 = Not Pressed Down
//        boolean leftBump = gamepad1.left_bumper;
//        boolean rightBump = gamepad1.right_bumper;

        // Range from 0-1
        double leftTrig = gamepad1.left_trigger;
//        double rightTrig = gamepad1.right_trigger;

        // True/False based on controller button presses
        boolean a = gamepad1.a;
        boolean b = gamepad1.b;
        boolean x = gamepad1.x;
        boolean y = gamepad1.y;
//        boolean up = gamepad1.dpad_up;
//        boolean down = gamepad1.dpad_down;
//        boolean left = gamepad1.dpad_left;
//        boolean right = gamepad1.dpad_right;

        if (b) {
            rightFeeder.setPower(-1);
            leftFeeder.setPower(-1);
        } else {
            rightFeeder.setPower(0);
            leftFeeder.setPower(0);
        }

        if (x) {
            mainFeeder.setPower(1);
        } else {
            mainFeeder.setPower(0);
        }

        if (y) {
            mainFeeder.setPower(-1);
        } else {
            mainFeeder.setPower(0);
        }

        if (a) {
            if (dualFlywheel) {
//!                leftLauncher.setVelocity(launcherTargetVelocity);
//!                rightLauncher.setVelocity(launcherTargetVelocity);
            } else {
                // .setVelocity is in ticks per second
                //
                launcher.setVelocity(launcherTargetVelocity);
            }
        } else {
            if (dualFlywheel) {
//!                leftLauncher.setVelocity(launcherStandbyVelocity);
//!                rightLauncher.setVelocity(launcherStandbyVelocity);
            } else {
                launcher.setVelocity(launcherStandbyVelocity);
            }
        }

        drive.driveSimple(leftX, leftY, rightX, leftTrig);

    }
}

