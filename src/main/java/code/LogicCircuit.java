package code;

import graphics_controls.GateType;

import java.util.ArrayList;

public class LogicCircuit {

    private ArrayList<LogicGate> gates;
    private ArrayList<LogicGate> outputList;
    private boolean[][] truthTable;
    private int inputs;
    //private int outputs;
    private int rows;
    private ArrayList<LogicGate> inputList;
    private boolean[][] failureTable;
    private int fRows;
    private boolean[][] structF;

    //todo saving and loading data //json?
    //todo reliability analysis
    //todo reloading tables with change
    /*
     * structure funct: compare 2^gate.size rows from failure table with truth table row
     * count matches
     * matches/failureRows *100 == reliability percentage
     * */


    public LogicCircuit() {
        this.gates = new ArrayList<>();
        this.inputs = 0;
        //this.outputs = 0;
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
                //      gates.add(g);
                inputList.add(g);
                inputs++;
                break;
            default:
                break;
        }
    }

    public void loadOutputGates() {
        for (LogicGate g : gates) {
            outputList.clear();

            if (g.getOutput().getEndGate() == null) {
                g.setOutputId(outputList.size());
                outputList.add(g);
                //outputs++;
            }
        }
    }

    public boolean[][] returnCompleteTable() {
        return failureTable;
    }

    public boolean[][] returnTruthTable() {
        return truthTable;
    }

    public void getTruthTable() {
        loadOutputGates();
        rows = (int) Math.pow(2, inputs);
        int columns = inputs + outputList.size();

        truthTable = new boolean[rows][columns];

        int tmp;
        for (int i = 0; i < rows; i++) {

            tmp = i;

            for (int j = 0; j < inputs; j++) {
                truthTable[i][j] = (tmp % 2 == 1);
                tmp = tmp >> 1;
            }
        }

        this.evalTrueTable();


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
        for (LogicGate input : inputList) {
            if (id.equals(input.getId())) {
                return input;
            }
        }
        return null;
    }

    public ArrayList<String> removeGateById(String id) {
        LogicGate g = getGateById(id);
        ArrayList<String> ids = null;
        if (g != null) {

            ids = g.getConnectorsID();
            for (int i = 0; i < ids.size(); i++) {
                for (LogicGate gate : gates) {
                    gate.findAndRemoveConnector(ids.get(i));
                }
            }
            gates.remove(g);
        }

        System.out.println("removed");
        return ids;
    }

    private boolean computeOutput(LogicGate gate, int row, boolean[][] table) {

        boolean output1 = true;
        boolean output2 = true;

        for (int connection = 0; connection < gate.getInputList().size(); connection++) {

            LogicGate input = gate.getInputList().get(connection).getStartGate();
            int inputIndex = input.getIndex();

            // It is a logic gate
            if (inputIndex == -1) {

                if (connection == 0) {
                    output1 = computeOutput(input, row, table);
                } else {
                    output2 = computeOutput(input, row, table);
                }

            } else {
                if (connection == 0) {
                    output1 = table[row][inputIndex];
                } else {
                    output2 = table[row][inputIndex];
                }
            }
        }
        return gate.getResult(output1, output2);
    }

    public void evalTrueTable() {

        for (LogicGate g : gates) {
            g.setWorking(true);
        }
        for (int row = 0; row < rows; row++) {

            for (int gate = 0; gate < outputList.size(); gate++) {

                LogicGate output = outputList.get(gate);
                boolean result = this.computeOutput(output, row, truthTable);
                truthTable[row][gate + inputs] = result;
            }
        }
    }

    public void evalFailureTable() {
        for (int i = 0; i < fRows; i++) {
            for (int j = 0; j < gates.size(); j++) {
                gates.get(j).setWorking(failureTable[i][inputs + j]);
            }

            for (int gate = 0; gate < outputList.size(); gate++) {
                LogicGate output = outputList.get(gate);
                boolean result = this.computeOutput(output, i, failureTable);
                failureTable[i][gate + inputs + gates.size()] = result;
            }
        }
    }

    public void getFailureTable() {
        if (truthTable == null) {
            getTruthTable();
        }
        //sets dimensions for table
        int failureRows = (int) Math.pow(2, gates.size());
        fRows = rows * failureRows;
        int fColumns = gates.size() + outputList.size() + inputs;//outputs and inputs are also in gatesList i think

        failureTable = new boolean[fRows][fColumns];
        int tmp;

        //copies input values from truth table, sets failure values
        for (int i = 0; i < rows; i++) {
            int a = i * failureRows;
            for (int j = 0; j < failureRows; j++) {
                System.arraycopy(truthTable[i], 0, failureTable[j + a], 0, inputs);

                tmp = j;
                for (int k = 0; k < gates.size(); k++) {
                    failureTable[j + a][k + inputs] = (tmp % 2 == 1);
                    tmp = tmp >> 1;
                }
            }
        }

        evalFailureTable();


        for (int i = 0; i < fRows; i++) {
            for (int j = 0; j < fColumns; j++) {
                System.out.print(failureTable[i][j] + " ");
            }
            System.out.println();
        }
    }


    public void setFailure(String type, boolean[][] table) {
        for (LogicGate g : gates) {
            if (g.getType().equals(type)) {
                g.defineCustomFunction(table);
                System.out.println("");
            }
        }
    }

    public void getStructFunction() {

        structF = new boolean[fRows][outputList.size()];

        for (int j = 0; j < fRows; j++) {
            System.arraycopy(failureTable[j], (inputs + gates.size()), structF[j], 0, outputList.size());
        }
    }

    public double getRel() {

        getStructFunction();

        int failureRows = (int) Math.pow(2, gates.size());
        int truths = 0;
        for (int i = 0; i < rows; i++) {
            int a = i * failureRows;
            for (int j = 0; j < failureRows; j++) {
                for (int k = 0; k < outputList.size(); k++) {
                    if (structF[j + a][k] == truthTable[i][inputs + k]) {
                        truths++;
                    }
                }
            }
        }
        return (((double) truths / fRows)) * 100;
    }
}
