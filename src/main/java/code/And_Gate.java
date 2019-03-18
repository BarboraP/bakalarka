package code;

public class And_Gate extends LogicGate {

    public And_Gate(String pid, LogicCircuit c) {
        super(pid, c);
    }

    public boolean getResult(boolean y1, boolean y2) {
        if (isWorking()) {
            return (y1 & y2);
        }
        return getResultFailure();

    }

    private boolean getResultFailure() {
        //TODO user will define this function somehow
        return false;
    }


}
