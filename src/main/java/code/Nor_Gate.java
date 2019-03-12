package code;

import java.util.UUID;

public class Nor_Gate extends LogicGate {
    public Nor_Gate(UUID pid) {
        super(pid);
    }

    public boolean getResult(boolean y1, boolean y2) {
        return (!(y1 | y2));
    }
}
