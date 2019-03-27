package code;

import java.util.UUID;

public class Nor_Gate extends LogicGate {
    public Nor_Gate(String pid, LogicCircuit c) {
        super(pid, c);
    }

    public boolean getResult(boolean y1, boolean y2) {
        if(isWorking()) {
            return (!(y1 | y2));
        }
        return getResultFailure(y1,y2);
    }

    public String getType() {
        return "nor";
    }
}
