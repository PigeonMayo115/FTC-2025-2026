package org.firstinspires.ftc.teamcode.custom;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class HuskyLensCustom {
    private HuskyLens huskyLens;
    private final int readPeriod = 1;

    int id1CenterX;
    int id1CenterY;
    public HuskyLensCustom(HardwareMap hwmap, HuskyLens.Algorithm algorithm){
        huskyLens = hwmap.get(HuskyLens.class, "huskyLens");
        huskyLens.selectAlgorithm(algorithm);

    }



}
