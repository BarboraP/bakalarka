package code;

import java.util.UUID;

public class LogicGate {

    private boolean isWorking;
    private Link output;
    private Link input1;
    private Link input2;
    private UUID id;


    public LogicGate() {
        isWorking = true;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }

}
