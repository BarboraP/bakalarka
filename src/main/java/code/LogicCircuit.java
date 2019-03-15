package code;

import graphics_controls.GateType;

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

    public void computeOutputs() {
        for (LogicGate g : gates) {
            if (g.getOutput() == null) {
                g.setOutputId(outputs);
                //set output id to 0 for the first gate
                //todo index in table = outputid + inputs for truthTable
                //todo for table with failures = outputid + inputs + gates
                outputs++;
            }
        }
    }

    public void getTruthTable() {

        computeOutputs();
        int rows = (int) Math.pow(2, inputs);
        int columns = inputs + outputs;
        truthTable = null;
        truthTable = new boolean[rows][columns];
        int tmp = 0;
        for (int i = 0; i < rows; i++) {
            tmp = i;
            System.out.println();
            for (int j = 0; j < inputs; j++) {
                truthTable[i][j] = (tmp % 2 == 1);
                System.out.print(truthTable[i][j] + "  ");
                tmp = tmp >> 1;
            }
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

    public void removeGateById(String id) {
        LogicGate g = getGateById(id);
        if (g != null) {
            gates.remove(g);
        }
        System.out.println("removed");
    }

    public void evaluate() {
        //TODO define number of output on gate?
    }
}
