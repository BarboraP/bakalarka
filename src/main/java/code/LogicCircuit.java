package code;

import graphics_controls.GateType;

import java.util.ArrayList;

public class LogicCircuit {

    private ArrayList<LogicGate> gates;
    private ArrayList<LogicGate> outputList;
    private boolean[][] truthTable;
    private int inputs;
    private int outputs;
    private int rows = (int) Math.pow(2, inputs);
    //need list of inputs to know index of input
    //todo input = gate with no inputs
    private ArrayList<LogicGate> inputList;

    public LogicCircuit() {
        this.gates = new ArrayList<>();
        this.inputs = 0;
        this.outputs = 0;
        outputList = new ArrayList<>();
        inputList = new ArrayList<>();
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
                gates.add(new And_Gate(id, this));
                break;
            case or:
                gates.add(new Or_Gate(id, this));
                break;

            case nand:
                gates.add(new Nand_Gate(id, this));
                break;

            case nor:
                gates.add(new Nor_Gate(id, this));
                break;

            case xor:
                gates.add(new Xor_Gate(id, this));
                break;

            case input:
                LogicGate g = new Input(id, this, inputs);
                gates.add(g);
                inputList.add(g);
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
                outputList.add(g);
                //set output id to 0 for the first gate
                //todo index in table = outputid + inputs for truthTable
                //todo for table with failures = outputid + inputs + gates
                outputs++;
            }
        }
    }

    public void getTruthTable() {

        computeOutputs();
        int columns = inputs + outputs;
        truthTable = null;
        truthTable = new boolean[rows][columns];
        int tmp = 0;
        for (int i = 0; i < rows; i++) {
            tmp = i;
            System.out.println();
            for (int j = 0; j < inputs; j++) {
                truthTable[i][j] = (tmp % 2 == 1);
                //System.out.print(truthTable[i][j] + "  ");
                tmp = tmp >> 1;
            }
        }

        this.evaluate();
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

    // todo: rename this monstrosity
    private boolean meow(LogicGate baska, int row) {

        boolean output1 = true, output2 = true;
        for (int connection = 0; connection < baska.getInputsList().size(); connection++) {
            LogicGate input = baska.getInputsList().get(connection).getStartGate();

            int inputIndex = input.getIndex();

            // It is a logic gate
            if (inputIndex == -1) {
                return this.meow(input, row);
            } else {
                if (connection == 0) {
                    output1 = truthTable[row][inputIndex];
                } else {
                    output2 = truthTable[row][inputIndex];
                }
            }
        }

        return baska.getResult(output1, output2);
    }

    public void evaluate() {

        // Start at the last logic gate.
        // Todo: What if this gate doesn't exist?
        //todo indexovanie tabule
        boolean[] q = new boolean[rows];
        LogicGate lastGate = outputList.get(0);

        for (int row = 0; row < rows; row++) {
            q[row] = this.meow(lastGate, row);
            System.out.println(q[row]);
        }
    }
}
