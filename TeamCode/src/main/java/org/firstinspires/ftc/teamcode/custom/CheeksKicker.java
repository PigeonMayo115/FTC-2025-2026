package org.firstinspires.ftc.teamcode.custom;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class CheeksKicker {


    Servo kicker = null;
    Servo leftCheek = null;
    Servo rightCheek = null;
    HardwareMap hwMap = null;

    boolean op1 = false;
    boolean op2 = false;

    boolean op3 = false;
    boolean op4 = false;
    final double kickerMin = 0.85;
    final double kickerMax = 0.2;
    final double leftMin = 0;
    final double leftMax = 0.35;
    final double rightMin = 0.2;
    final double rightMax = 0.5;

    private double extendStart = 0;
    private double extendEnd = 0;
    private boolean hasCompletedExtend = false;
    private double startTimeRight = -1;
    private boolean hasCompletedRight = false;
    private double startTimeLeft = -1;
    private boolean hasCompletedLeft = false;
    ElapsedTime elapsedTime = null;
    boolean launchDone = false;
    boolean waitDone = false;
    int state = 1;
    boolean isLaunch = true;
    boolean lastUpState = false;
    boolean lastDownState = false;

    public CheeksKicker(HardwareMap hardwareMap, ElapsedTime elapsedTime) {
        hwMap = hardwareMap;
        kicker = hwMap.servo.get("kicker");
        leftCheek = hwMap.servo.get("leftCheek");
        rightCheek = hwMap.servo.get("rightCheek");
        this.elapsedTime = elapsedTime;
    }

    public boolean leftUp() {
        leftCheek.setPosition(leftMin);
        if (leftCheek.getPosition() == leftMin) {
            return true;
        } else {
            return false;
        }
    }

    public boolean leftDown() {
        leftCheek.setPosition(leftMax);
        if (leftCheek.getPosition() == leftMax) {
            return true;
        } else {
            return false;
        }
    }

    public boolean rightUp() {
        rightCheek.setPosition(rightMax);
        if (rightCheek.getPosition() == rightMax) {
            return true;
        } else {
            return false;
        }
    }

    public boolean rightDown() {
        rightCheek.setPosition(rightMin);
        if (rightCheek.getPosition() == rightMin) {
            return true;
        } else {
            return false;
        }
    }

    public boolean kickerExtend() {
        kicker.setPosition(kickerMax);
        if (kicker.getPosition() == kickerMax) {
            return true;
        } else {
            return false;
        }
    }

    public boolean kickerExtend(double howLong) {

        double currentTime = elapsedTime.seconds();

        if (extendEnd == 0){
            extendStart = currentTime;
            extendEnd = currentTime + howLong;
        }
        if (currentTime > extendStart + howLong){
            extendStart = 0;
            extendEnd = 0;
            kicker.setPosition(kickerMin);
            return true;
        } else{
            kicker.setPosition(kickerMax);
            return false;
        }

    }




    public boolean kickerRetract(){
        kicker.setPosition(kickerMin);
        if (kicker.getPosition() == kickerMin){
            return true;
        } else {
            return false;
        }
    }



    public void launchState(int state){
        switch (state){
            case 1:
                launchDone = kickerExtend(1);
                if (launchDone){
                    intakeState(state);
                }
                break;
            case 2:
                launchDone = kickerExtend(1);
                if (launchDone){
                    intakeState(state);
                }
                break;
            case 3:
                launchDone = kickerExtend(1);
                if (launchDone){
                    intakeState(state);
                }
                break;
        }

    }
    
    public boolean intakeState (int state){
        boolean done = false;
        switch (state){
            case 1:
                op1 = leftDown();
                op2 = rightDown();
                done = true;
                break;
            case 2:
                op1 = leftUp();
                op2 = rightDown();
                done = true;
                break;
            case 3:
                op1 = leftUp();
                op2 = rightUp();
                done = true;
                break;
            default:
                op1 = leftUp();
                op2 = rightUp();
                done = true;
                break;
        }
        return done;

    }

    public void update (boolean upValue, boolean downValue){

        if (upValue && !lastUpState && state < 3) {
            state++;
            isLaunch = false;
        }
        lastUpState = upValue;

        //decrement state by one and cycle launch on right bump press
        if (downValue && !lastDownState && state > 1) {
            state--;
            isLaunch = true;
        }
        lastDownState = downValue;

        intakeState(state);


    }




}
