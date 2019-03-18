package code;

public class Input extends LogicGate {
    private int index;

    public Input(String pid, LogicCircuit c, int index) {
        super(pid, c);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }


}
