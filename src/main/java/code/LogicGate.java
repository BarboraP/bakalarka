package code;

import java.util.ArrayList;

public abstract class LogicGate {

    private boolean isWorking;
    private Connector output;
    protected ArrayList<Connector> inputConnectorsList;
    private final String id;
    private int outputId;
    protected LogicCircuit circuit = null;


    public LogicGate(String pid, LogicCircuit c) {
        isWorking = true;
        id = pid;
        inputConnectorsList = new ArrayList<>();
        circuit = c;
    }

    public int getOutputId() {
        return outputId;
    }

    public void setOutputId(int id) {
        outputId = id;
    }

    public ArrayList<Connector> getInputList() {
        return inputConnectorsList;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }

    public Connector getOutput() {
        return output;
    }

    public void setOutput(Connector output) {
        this.output = output;
    }

    public boolean addInputConnector(Connector c) {
        if (inputConnectorsList.size() < 2) {
            inputConnectorsList.add(c);
            return true;
        }
        return false;
    }

    public String getId() {
        return id;
    }

    public String returnOutputID() {
        return output.getId();
    }

    public boolean getResult(boolean y1, boolean y2) {
        return false;
    }

    public int getIndex() {
        return -1;
    }

    public boolean getResultFailure() {
        //TODO let user define failure state of each type of gate
        return false;
    }
}
