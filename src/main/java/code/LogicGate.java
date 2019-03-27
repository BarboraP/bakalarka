package code;

import java.util.ArrayList;

public abstract class LogicGate {

    private boolean isWorking;
    private Connector output;
    protected ArrayList<Connector> inputConnectorsList;
    private final String id;
    private int outputId;
    protected LogicCircuit circuit = null;
    private boolean customFunction[][]; // todo user defines this


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

    public boolean getResultFailure(boolean y1, boolean y2) {
       if(customFunction != null) {


           for (int i = 0; i < customFunction.length; i++) {
               if (customFunction[i][0] == y1 && customFunction[i][1] == y2) {
                   return customFunction[i][2];
               }
           }
       }
        return false;
    }

    public boolean findAndRemoveConnector(String id) {
        for (int i = 0; i < inputConnectorsList.size(); i++) {
            if (id.equals(inputConnectorsList.get(i).getId())) {
                inputConnectorsList.remove(i);
                return true;
            }
        }
        if (output != null && id.equals(output.getId())) {
            output = null;
            return true;
        }
        return false;
    }

    public ArrayList<String> getConnectorsID() {
        ArrayList<String> list = new ArrayList<>();
        if (output != null) {
            list.add(output.getId());
        }
        for (int i = 0; i < inputConnectorsList.size(); i++) {
            list.add(inputConnectorsList.get(i).getId());
        }
        return list;
    }

    public void defineCustomFunction(boolean[][] tab) {
        customFunction = tab;
    }

    public String getType() {
        return "";
    }
}

