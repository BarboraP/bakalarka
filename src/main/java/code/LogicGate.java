package code;

import java.util.UUID;

public class LogicGate {

    private boolean isWorking;
    private Connector output;
    private Connector input1;
    private Connector input2;
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
