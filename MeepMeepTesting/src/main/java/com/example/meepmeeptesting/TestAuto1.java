package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class TestAuto1 {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.setDimensions(18,18);

        Pose2d startPose = new Pose2d(-39, 31, Math.toRadians(180));


        Action turnToObelisk = myBot.getDrive().actionBuilder(startPose)
                .strafeToConstantHeading(new Vector2d(-16,16))
                .turnTo(Math.toRadians(140))
                .build();
        //.strafeToSplineHeading(new Vector2d(-16,16), Math.toRadians(135)).build();

        Action moveToPickup = myBot.getDrive().actionBuilder(new Pose2d(-16, 16, Math.toRadians(140)))
                .turnTo(Math.toRadians(270))
                .strafeToConstantHeading(new Vector2d(-20, 30))
                .build();

        Action reverse = myBot.getDrive().actionBuilder(new Pose2d(-20,30,Math.toRadians(270)))
                .strafeToConstantHeading(new Vector2d(-20,48))
                .build();


        myBot.runAction(turnToObelisk);
        //myBot.runAction(moveToPickup);
        //myBot.runAction(reverse);

        /*
        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-70, 24, Math.toRadians(0)))
                //.lineToXSplineHeading(0,Math.toRadians(135))
                .strafeTo(new Vector2d(-16,16))
                .turnTo(Math.toRadians(135))
                                .turnTo(Math.toRadians(270))
                                .strafeTo(new Vector2d(-12, 32))
                        .waitSeconds(0.5)
                                .lineToY(48)

                //.splineTo(new Vector2d(0,48),Math.PI)
                .build());

         */

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}