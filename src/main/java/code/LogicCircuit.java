package code;

import graphics_controls.GateType;

import java.util.ArrayList;

public class LogicCircuit {

    private ArrayList<LogicGate> gates;
    private ArrayList<LogicGate> outputList;
    transient private boolean[][] truthTable;
    transient private int rows;
    private ArrayList<LogicGate> inputList;
    transient private boolean[][] failureTable;
    transient private int fRows;
    transient private boolean[][] structF;

    private boolean[] structFunction;

    private ArrayList<Connector> connectors;

    //todo saving and loading data //json?
    //todo get name of file to save/load
    //todo save and load graphic components

    //todo reliability analysis for gates//struct index
    //todo show failure func for gates

    public LogicCircuit() {
        this.gates = new ArrayList<>();
        this.rows = 0;
        outputList = new ArrayList<>();
        inputList = new ArrayList<>();
        connectors = new ArrayList<>();
    }

    public void addGate(GateType type, String id, double x) {
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
                addInput(new Input(id, inputList.size(), x));
                break;
            case output:
                outputList.add(new Output(id, outputList.size()));
                break;
            default:
                break;
        }
    }

    public void addInput(Input input) {

        boolean added = false;
        if (!inputList.isEmpty()) {
            int a = inputList.size();
            for (int i = 0; i < a; i++) {
                if (input.getY() < inputList.get(i).getY()) {
                    inputList.add(i, input);
                    added = true;
                    break;
                }
            }
            if (!added) {
                inputList.add(input);
            }

        } else {
            inputList.add(input);
        }
        for (int i = 0; i < inputList.size(); i++) {
            inputList.get(i).setIndex(i);
        }
    }

    public boolean[][] returnCompleteTable() {
        return failureTable;
    }

    public boolean[][] returnTruthTable() {
        return truthTable;
    }

    public void getTruthTable() {
        rows = (int) Math.pow(2, inputList.size());
        int columns = inputList.size() + outputList.size();
        truthTable = new boolean[rows][columns];
        int tmp;
        for (int i = 0; i < rows; i++) {

            tmp = i;

            for (int j = 0; j < inputList.size(); j++) {
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
        for (LogicGate output : outputList) {
            if (id.equals(output.getId())) {
                return output;
            }
        }
        return null;
    }

    public ArrayList<String> removeGateById(String id) {
        LogicGate g = getGateById(id);
        ArrayList<String> ids = null;
        if (g != null) {

            if (g.getType().equals("output")) {

                ids = g.getConnectorsID();
                g.getGate().findAndRemoveConnector(id);
                outputList.remove(g);
            } else if (g.getType().equals("input")) {
                ids = g.getConnectorsID();
                for (int i = 0; i < ids.size(); i++) {
                    for (LogicGate gate : gates) {
                        gate.findAndRemoveConnector(ids.get(i));
                    }
                }
                inputList.remove(g);

            } else {
                ids = g.getConnectorsID();
                for (int i = 0; i < ids.size(); i++) {
                    for (LogicGate gate : gates) {
                        gate.findAndRemoveConnector(ids.get(i));
                    }
                    for (LogicGate gate : inputList) {
                        gate.findAndRemoveConnector(ids.get(i));
                    }
                    for (LogicGate gate : outputList) {
                        gate.findAndRemoveConnector(ids.get(i));
                    }
                }
                gates.remove(g);
            }
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

                LogicGate output = outputList.get(gate).getGate();
                boolean result = this.computeOutput(output, row, truthTable);
                truthTable[row][gate + inputList.size()] = result;
            }
        }
    }

    public void evalFailureTable() {
        for (int i = 0; i < fRows; i++) {
            for (int j = 0; j < gates.size(); j++) {
                gates.get(j).setWorking(failureTable[i][inputList.size() + j]);
            }

            for (int gate = 0; gate < outputList.size(); gate++) {
                LogicGate output = outputList.get(gate).getGate();
                boolean result = this.computeOutput(output, i, failureTable);
                failureTable[i][gate + inputList.size() + gates.size()] = result;
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
        int fColumns = gates.size() + outputList.size() + inputList.size();//outputs and inputs are also in gatesList i think

        failureTable = new boolean[fRows][fColumns];
        int tmp;

        //copies input values from truth table, sets failure values
        for (int i = 0; i < rows; i++) {
            int a = i * failureRows;
            for (int j = 0; j < failureRows; j++) {
                System.arraycopy(truthTable[i], 0, failureTable[j + a], 0, inputList.size());

                tmp = j;
                for (int k = 0; k < gates.size(); k++) {
                    failureTable[j + a][k + inputList.size()] = (tmp % 2 == 1);
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
        getStructFunction();
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

        boolean[][] substructureF = new boolean[fRows][outputList.size()];

        structFunction = new boolean[fRows];

        int failureRows = (int) Math.pow(2, gates.size());

        //for every collumn
        for (int k = 0; k < outputList.size(); k++) {


            //for every row
            for (int i = 0; i < rows; i++) {
                int a = i * failureRows;
                for (int j = 0; j < failureRows; j++) {

                    if (truthTable[i][k + inputList.size()] == failureTable[j + a][k + inputList.size() + outputList.size()]) {
                        substructureF[j + a][k] = true;
                    } else {
                        substructureF[j + a][k] = false;
                    }

                }
            }

        }

        int equals = 0;

        for (int i = 0; i < fRows; i++) {

            if (outputList.size() > 1) {


                equals = 0;
                for (int j = 0; j < outputList.size(); j++) {
                    if (substructureF[i][0] == substructureF[i][j]) {
                        equals++;
                    }
                }
                if (equals == outputList.size()) {
                    structFunction[i] = true;
                } else {
                    structFunction[i] = false;
                }
            } else {
                structFunction[i] = substructureF[i][0];
            }
        }
    }

    public double getRel() {

        if (gates.size() > 0) {


           // getStructFunction();

            int values = 0;

            for (int i = 0; i < fRows; i++) {
                if (structFunction[i] == true) {
                    values++;
                }
            }
            return (double) values / fRows * 100;
        }
        return 0;
    }

    public void addConnector(Connector c) {
        connectors.add(c);
    }

    public void setConnections() {

        for (Connector c : connectors) {
            LogicGate g = getGateById(c.getStartGateId());
            g.setOutput(c);
            c.setStartGate(g);
            LogicGate g1 = getGateById(c.getEndGateId());
            g1.addConnectorByPosition(c);
            c.setEndGate(g1);
        }
    }

    public String getStringStruct() {
        String s = "";
        if (structFunction != null) {
            for (int i = 0; i < structFunction.length; i++) {
                s += structFunction[i] + ", ";
            }
        }
        return s;
    }

    /**
     *
     * @param type
     * true for gate failure resulting in circuit failure
     * false for gate repair resulting in circuit failure
     */
    public void getNumOfCriticalVectors(Boolean type) {


        //TODO JUST FOR ONE GATE CIRCUITS NOW

        int failureRows = (int) Math.pow(2, gates.size());
        int rowType = -1;
        if (gates.size() == 1) {
            for (int i = 0; i < rows; i++) {
                int a = i * failureRows;
                for (int j = 0; j < failureRows; j++) {

                    if (failureTable[j + a][inputList.size()] == type) {
                        rowType = j + a;

                        if (structFunction[j + a]) {

                            if (j == 0) {
                                if (failureTable[1 + a][inputList.size()] != type) {
                                    if (!structFunction[1 + a]) {
                                        gates.get(0).addCritVector();
                                    }
                                }
                            } else {
                                if (failureTable[a][inputList.size()] != type) {
                                    if (!structFunction[a]) {
                                        gates.get(0).addCritVector();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("mau");
    }

    public void computeImportances() {
        double probability = (double) 1 / rows;

        for (LogicGate g : gates) {
            g.computeSim(gates.size());
            g.setImportance(g.getSim() * probability);
        }
    }

    public void importanceAnalysis() {
        getNumOfCriticalVectors(true);
        //getNumOfCriticalVectors(false);
        computeImportances();
        System.out.println(gates.get(0).getImportance() + "meow");

    }
}
