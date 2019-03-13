package code;

import graphics_controls.GateType;

import java.util.ArrayList;
import java.util.UUID;

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

    public void addGate(GateType type, String id) {
        switch (type) {
            case and:
                gates.add(new And_Gate(id));
                break;
            case or:
                gates.add(new Or_Gate(id));
                break;

            case nand:
                gates.add(new Nand_Gate(id));
                break;

            case nor:
                gates.add(new Nor_Gate(id));
                break;

            case xor:
                gates.add(new Xor_Gate(id));
                break;

            case input:
                //TODO do inputs need to be saved as Gates or in another list or not at all?
                inputs++;
                break;
            default:
                break;
        }
    }

    public LogicGate getGateById(String id) {
        for (LogicGate g : gates) {
            if (g.getId().equals(id)) {
                return g;
            }
        }
        return null;
    }
}
