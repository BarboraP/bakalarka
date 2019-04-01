package code;

public class Output extends LogicGate {
    private int index;

    public Output(String pid,  int index) {
        super(pid);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public boolean isOutput() {
        return true;
    }

    public LogicGate getGate(){
        return getInputList().get(0).getStartGate();
    }

    public String getType() {
        return "output";
    }

}
