package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.custom.CheeksKicker;

@Config
@TeleOp
public class ServoPositionTest extends OpMode {
    Servo left = null;
    Servo right = null;
    Servo kick = null;
    public static double leftPos = 0.65;
    //min is 0.65
    //max is 0.35
    public static double rightPos = 0.2;
    //min is 0.2
    //max is 0.5
    public static double kickerPos = 1;
    //all the way back is 1
    //forwards is unknown
    @Override
    public void init() {
        left = hardwareMap.servo.get("leftCheek");
        right = hardwareMap.servo.get("rightCheek");
        kick = hardwareMap.servo.get("kicker");



        telemetry.addData("left pos:", left.getPosition());


    }

    @Override
    public void loop() {
        left.setPosition(leftPos);
        right.setPosition(rightPos);
        kick.setPosition(kickerPos);
    }
}
