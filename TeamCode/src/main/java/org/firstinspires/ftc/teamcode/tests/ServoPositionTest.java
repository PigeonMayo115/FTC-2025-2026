package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.custom.CheeksKicker;
import org.firstinspires.ftc.teamcode.custom.WaitAuto;

@Config
@TeleOp
public class ServoPositionTest extends OpMode {
    Servo left = null;
    Servo right = null;
    Servo kick = null;
    CheeksKicker cheeksKicker = null;
    public static double leftPos = 0.65;
    //min is 0.65
    //max is 0.35
    public static double rightPos = 0.2;
    //min is 0.2
    //max is 0.5
    public static double kickerPos = 1;
    //all the forwards is 0.2
    //all the way back is 0.85
    int step = 1;
    boolean done = false;
    ElapsedTime elapsedTime;
    @Override
    public void init() {
        left = hardwareMap.servo.get("leftCheek");
        right = hardwareMap.servo.get("rightCheek");
        kick = hardwareMap.servo.get("kicker");
        cheeksKicker = new CheeksKicker(hardwareMap, new ElapsedTime());
        elapsedTime = new ElapsedTime();





        telemetry.addData("left pos:", left.getPosition());


    }

    @Override
    public void loop() {
        left.setPosition(leftPos);
        right.setPosition(rightPos);
        //kick.setPosition(kickerPos);

        switch (step) {
            case 1:
                done = cheeksKicker.kickerExtend(3);
                if (done) {
                    step = 2;
                }
                break;
            case 2:
                done = WaitAuto.wait(1, elapsedTime.seconds());
                if (done) {
                    step = 1;
                }
        }

        telemetry.addData("step",step);
    }
}
