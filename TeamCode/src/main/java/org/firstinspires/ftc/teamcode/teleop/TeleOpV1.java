package org.firstinspires.ftc.teamcode.teleop;


import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@TeleOp
public class TeleOpV1 extends OpMode {
    MecanumDrive drive = null;


    double lx;
    double ly;
    double rx;



    @Override
    public void init() {
        drive = new MecanumDrive(hardwareMap, new Pose2d(0,0,0));

    }

    @Override
    public void loop() {
        ly = gamepad1.left_stick_y;
        lx = gamepad1.left_stick_x;
        rx = gamepad1.right_stick_x;

        drive.setDrivePowers(drive.inputToPoseVelocity2D(lx, ly, rx));



    }
}
