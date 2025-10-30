package org.firstinspires.ftc.teamcode.teleop;


import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.custom.Feeder;
import org.firstinspires.ftc.teamcode.custom.Flywheel;
import org.firstinspires.ftc.teamcode.custom.Intake;

@TeleOp
public class TeleOpV2Duo extends OpMode {
    MecanumDrive drive = null;
    Feeder feeder = null;
    Flywheel flywheel = null;
    Intake intake = null;
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





    @Override
    public void init() {
        drive = new MecanumDrive(hardwareMap, new Pose2d(0,0,0));
        feeder = new Feeder(hardwareMap);
        flywheel = new Flywheel(hardwareMap);
        intake = new Intake(hardwareMap);


    }

    @Override
    public void loop() {
        ly = gamepad1.left_stick_y;
        lx = gamepad1.left_stick_x;
        rx = gamepad1.right_stick_x;
        a = gamepad2.a;
        b = gamepad2.b;
        x = gamepad1.x;
        y = gamepad1.y;
        dPadUp = gamepad1.dpad_up;
        dPadDown = gamepad1.dpad_down;
        leftBump = gamepad2.left_bumper;
        rightBump = gamepad2.right_bumper;
        leftTrigger = gamepad1.left_trigger;


        drive.setDrivePowers(drive.inputToPoseVelocity2D(lx, ly, rx, leftTrigger));

        //for the 6000 rpm motor, 3000 ticks per second is the max speed of 100 percent
        if (rightTrigger >= 0.3){
            flywheel.setVelocity(targetFlywheelVel);
        } else if (b){
            flywheel.setVelocity(0);
        }

        if (targetFlywheelVel <= 0){
            if (rightBump) {
                targetFlywheelVel += 1;
            }
        } else if (targetFlywheelVel >= 3000){
            if (leftBump) {
            } else if (rightBump) {
                targetFlywheelVel -= 1;
            }
        } else {
            if (rightBump) {
                targetFlywheelVel += 1;
            } else if (leftBump){
                targetFlywheelVel -= 1;
            }
        }
        if (gamepad2.right_stick_button){
            targetFlywheelVel = 1500;
        }

        if (x){
            intake.suck();
        } else if (y){
            intake.spit();
        } else {
            intake.hold();
        }

        if (b){
            feeder.suck();
        } else if (a){
            feeder.spit();
        } else {
            feeder.hold();
        }

        telemetry.addData("Controls:","");
        telemetry.addData("forward/back/left/right, rotation", "left stick, right stick \n left trigger for slowdown" );
        telemetry.addData("Intake: in, out", "x,y");
        telemetry.addData("Feeders: launch, reject", "a, b");
        telemetry.addData("Flywheel: Spin Up, Increase/Decrease velocity", "rt, right bumper/left bumper");
        telemetry.addData("Flywheel velocity (target/actual)", targetFlywheelVel + "/" + flywheel.getVelocity());











    }
}
