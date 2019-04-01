package code;

public class And_Gate extends LogicGate {


    public And_Gate(String pid) {
        super(pid);
    }

    public boolean getResult(boolean y1, boolean y2) {
        if (isWorking()) {
            return (y1 & y2);
        }
        return getResultFailure(y1,y2);
    }

    public String getType() {
        return "and";
    }
}
