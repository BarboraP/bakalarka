package code;

import java.util.ArrayList;

public abstract class LogicGate {

    private boolean isWorking;
    private Connector output;
    private ArrayList<Connector> inputs;
    private final String id;
    private int outputId;


    public LogicGate(String pid) {
        isWorking = true;
        id = pid;
        inputs = new ArrayList<>();
    }

    public void setOutputId(int id) {
        outputId = id;
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
}
