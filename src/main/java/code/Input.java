package code;

public class Input extends LogicGate {
    private int index;
    private double y;

    public Input(String pid, int index, double y) {
        super(pid);
        this.index = index;
        this.y = y;
    }

    public int getIndex() {
        return index;
    }

    public String getType() {
        return "input";
    }

    public double getY() {
        return y;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
