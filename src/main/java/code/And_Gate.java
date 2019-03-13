package code;

import java.util.UUID;

public class And_Gate extends LogicGate {

    public And_Gate(String pid) {
        super(pid);
    }

    public boolean getResult(boolean y1, boolean y2) {
        return (y1 & y2);
    }

}
