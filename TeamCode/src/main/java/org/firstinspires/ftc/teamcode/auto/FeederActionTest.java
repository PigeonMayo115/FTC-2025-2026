package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.custom.Feeder;
@Autonomous
public class FeederActionTest extends OpMode {
    Feeder feeder = null;

    @Override
    public void init() {
        ElapsedTime time = new ElapsedTime();
        feeder = new Feeder(hardwareMap,time);


    }

    @Override
    public void loop() {
        Actions.runBlocking(new SequentialAction(feeder.launch()));


    }
}
