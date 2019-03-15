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

    public boolean getResult2() {
        //todo get input one, get input2 and get result from them
        LogicGate g1 = circuit.getGateById(inputs.get(0).getStartGateId());
        LogicGate g2 = circuit.getGateById(inputs.get(1).getStartGateId());
        if (isWorking()) {
            return (g1.getResult2() & g2.getResult2());
        }
        return getResultFailure();
    }
}
