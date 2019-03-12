package code;

import java.util.ArrayList;

public class LogicCircuit {

    private ArrayList<LogicGate> gates;
    private boolean[][] truthTable;
    private int inputs;
    private int outputs;

    public LogicCircuit() {
        this.gates = new ArrayList<>();
        this.inputs = 0;
        this.outputs = 0;
    }

    public int getInputs() {
        return inputs;
    }

    public void setInputs(int inputs) {
        this.inputs = inputs;
    }

    public int getOutputs() {
        return outputs;
    }

    public void setOutputs(int outputs) {
        this.outputs = outputs;
    }
}
