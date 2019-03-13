package code;

import java.util.UUID;

public class Or_Gate extends LogicGate {
    public Or_Gate(String pid) {
        super(pid);
    }

    public boolean getResult(boolean y1, boolean y2) {
        return (y1 | y2);
    }
}
