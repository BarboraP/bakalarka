package code;

import java.util.ArrayList;
import java.util.UUID;

public abstract class LogicGate {

    private boolean isWorking;
    private Connector output;
    private ArrayList<Connector> inputs;
    private final String id;


    public LogicGate(String pid) {
        isWorking = true;
        id = pid;
        inputs = new ArrayList<>();
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

    public boolean addInputConnector(Connector c){
        if(inputs.size() < 2){
            inputs.add(c);
            return true;
        }
        return  false;
    }
    public String getId() {
        return id;
    }

    public String returnOutputID() {
       return output.getId();
    }
}
