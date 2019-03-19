package code;

import graphics_controls.GateType;

import java.util.ArrayList;

public class LogicCircuit {

    private ArrayList<LogicGate> gates;
    private ArrayList<LogicGate> outputList;
    private boolean[][] truthTable;
    private int inputs;
    private int outputs;
    private int rows;
    private ArrayList<LogicGate> inputList;
    private boolean[][] failureTable;

    public LogicCircuit() {
        this.gates = new ArrayList<>();
        this.inputs = 0;
        this.outputs = 0;
        this.rows = 0;
        outputList = new ArrayList<>();
        inputList = new ArrayList<>();
    }

    public int getInputs() {
        return inputs;
    }

    public void setInputs(int inputs) {
        this.inputs = inputs;
    }

    public int getNumberOfOutputs() {
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

    public void loadOutputGates() {
        for (LogicGate g : gates) {
            if (g.getOutput() == null) {
                g.setOutputId(outputs);
                outputList.add(g);
                outputs++;
            }
        }
    }

    public void getTruthTable() {

        rows = (int) Math.pow(2, inputs);

        loadOutputGates();

        int columns = inputs + outputs;
        // truthTable = null;
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
        this.eval();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(truthTable[i][j] + " ");
            }
            System.out.println();
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

    private boolean computeOutput(LogicGate gate, int row) {

        boolean output1 = true;
        boolean output2 = true;

        for (int connection = 0; connection < gate.getInputList().size(); connection++) {

            LogicGate input = gate.getInputList().get(connection).getStartGate();

            int inputIndex = input.getIndex();

            // It is a logic gate
            if (inputIndex == -1) {

                if (connection == 0) {
                    output1 = computeOutput(input, row);
                } else {
                    output2 = computeOutput(input, row);
                }

            } else {
                if (connection == 0) {
                    output1 = truthTable[row][inputIndex];

                } else {
                    output2 = truthTable[row][inputIndex];
                }
            }
        }
        return gate.getResult(output1, output2);
    }

    public void eval() {
        for (int row = 0; row < rows; row++) {

            for (int gate = 0; gate < outputList.size(); gate++) {

                LogicGate output = outputList.get(gate);
                boolean a = this.computeOutput(output, row);
                truthTable[row][gate + inputs] = a;
            }
        }
    }

    public void getFailureTable() {

        if(truthTable == null) {
            getTruthTable();
        }
        int gatesCount = gates.size() - inputs; //this is N
        int fRows = rows * ((int) Math.pow(2, gatesCount));
        int fColumns = gates.size() + outputs;//outputs and inputs are also in gatesList i think

        failureTable = new boolean[fRows][fColumns];

        for(int i = 0; i < rows; i++) {
            int a = i*(int) Math.pow(2, gatesCount);
            for(int j = 0; j < ((int) Math.pow(2, gatesCount)); j++) {
                failureTable[j + a] = truthTable[i];
            }

        }




        System.out.println("wda");
    }
}
