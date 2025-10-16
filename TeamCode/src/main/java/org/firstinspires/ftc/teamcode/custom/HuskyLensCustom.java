package org.firstinspires.ftc.teamcode.custom;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.lang.reflect.Array;

public class HuskyLensCustom {
    public HuskyLens huskyLens;
    private final int readPeriod = 1;

    private HuskyLens.Block[] blocks;

    Telemetry telemetry;

    public HuskyLensCustom(HardwareMap hwmap, Telemetry telemetry1){
        huskyLens = hwmap.get(HuskyLens.class, "huskyLens");
        telemetry = telemetry1;
        blocks = huskyLens.blocks();

    }

    public boolean connectionCheck(){
        if (huskyLens.knock()){
            telemetry.update();
            return true;
        } else {
            telemetry.update();
            return false;
        }
    }

    public HuskyLens.Block[] blocksList (){
        for (int i = 0; i < blocks.length; i ++) {
            telemetry.addData("Block count", blocks.length);
            telemetry.addData("Blocks", blocks.toString());
            telemetry.update();
        }
        return blocks;
    }

    public int getBlockX (int id) {
        int x = 0;
        if (blocks.length > 0) {
            for (int i = 0; i < blocks.length; i++) {

                if (blocks[i].id == id){
                    x = blocks[i].x;
                }
            }
        }
        telemetry.update();
        return x;
    }

    public int getBlockY (int id) {
        int y = 0;
        if (blocks.length > 0) {
            for (int i = 0; i < blocks.length; i++) {

                if (blocks[i].id == id){
                    y = blocks[i].y;
                }
            }
        }
        telemetry.update();
        return y;
    }



}
