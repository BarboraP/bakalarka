package code;

import java.util.ArrayList;

public abstract class LogicGate {

    transient private boolean isWorking;
    transient private Connector output;
    transient protected ArrayList<Connector> inputConnectorsList;
    private  String id;
    private int outputId;
    transient private boolean customFunction[][];


    public LogicGate(String pid) {
        isWorking = true;
        id = pid;
        inputConnectorsList = new ArrayList<>();
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
        if(inputConnectorsList.isEmpty()) {
            inputConnectorsList.add(c);
            c.setPosition(0);
            return true;
        }

        if (inputConnectorsList.size() < 2) {
            if (inputConnectorsList.get(0).getPosition() == 1) {
                inputConnectorsList.add(0, c);
                c.setPosition(0);
                return true;
            } else  {
                inputConnectorsList.add(c);
                c.setPosition(1);
                return true;
            }
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
        if (customFunction != null) {


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

    public LogicGate getGate() {
        return null;
    }

    public double getY() {
        return 0;
    }

    public void setIndex(int a){

    }

   /* public void setConnectorGate(){
        for (Connector c : inputConnectorsList) {
            c.setEndGate(this);
        }

        if(output != null){
            output.setStartGate(this);
        }

    }*/

   public void addConnectorByPosition(Connector c){
       if(inputConnectorsList == null) {
           inputConnectorsList = new ArrayList<>();
       }
       if (inputConnectorsList.size() < 2) {
           if (c.getPosition() == 0) {
               inputConnectorsList.add(0, c);

           } else  {
               inputConnectorsList.add(1,c);
           }
       }
   }
}

