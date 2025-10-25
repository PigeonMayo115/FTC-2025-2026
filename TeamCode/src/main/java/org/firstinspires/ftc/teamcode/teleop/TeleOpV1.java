package org.firstinspires.ftc.teamcode.teleop;


import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.custom.Feeder;
import org.firstinspires.ftc.teamcode.custom.Flywheel;
import org.firstinspires.ftc.teamcode.custom.Intake;

@TeleOp
public class TeleOpV1 extends OpMode {
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
        a = gamepad1.a;
        b = gamepad1.b;
        x = gamepad1.x;
        y = gamepad1.y;
        dPadUp = gamepad1.dpad_up;
        dPadDown = gamepad1.dpad_down;
        leftBump = gamepad1.left_bumper;
        rightBump = gamepad1.right_bumper;


        drive.setDrivePowers(drive.inputToPoseVelocity2D(lx, ly, rx));

        //for the 6000 rpm motor, 3000 ticks per second is the max speed of 100 percent
        if (a){
            flywheel.setVelocity(targetFlywheelVel);
        } else if (b){
            flywheel.setVelocity(0);
        }

        if (targetFlywheelVel <= 0){
            if (dPadUp) {
                targetFlywheelVel += 10;
            }
        } else if (targetFlywheelVel >= 3000){
            if (dPadUp) {
            } else if (dPadDown) {
                targetFlywheelVel -= 10;
            }
        } else {
            if (dPadUp) {
                targetFlywheelVel += 10;
            } else if (dPadDown){
                targetFlywheelVel -= 10;
            }
        }

        if (x){
            intake.suck();
        } else if (y){
            intake.spit();
        } else {
            intake.hold();
        }

        if (rightBump){
            feeder.suck();
        } else if (leftBump){
            feeder.spit();
        } else {
            feeder.hold();
        }

        telemetry.addData("Controls:","");
        telemetry.addData("forward/back/left/right, rotation", "left stick, right stick" );
        telemetry.addData("Intake: in, out", "x,y");
        telemetry.addData("Feeders: launch, reject", "left bumper, right bumper");
        telemetry.addData("Flywheel: Spin Up, Stop, Increase/Decrease velocity", "a, b, Dpad up and down");
        telemetry.addData("Flywheel velocity (target/actual)", targetFlywheelVel + "/" + flywheel.getVelocity());











    }
}
