package code;

import java.util.ArrayList;

public abstract class LogicGate {

    private boolean isWorking;
    private Connector output;
    protected ArrayList<Connector> inputs;
    private final String id;
    private int outputId;
    protected LogicCircuit circuit = null;


    public LogicGate(String pid, LogicCircuit c) {
        isWorking = true;
        id = pid;
        inputs = new ArrayList<>();
        circuit = c;

    }

    public int getOutputId() {
        return outputId;
    }

    public void setOutputId(int id) {
        outputId = id;
    }

    public ArrayList getInputsList() {
        return inputs;
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
        if (inputs.size() < 2) {
            inputs.add(c);
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
        //todo get result without param??
        return false;
    }

    public boolean getResult2() {
       //todo get input one, get input2 and get result from them
        return false;
    }
}
