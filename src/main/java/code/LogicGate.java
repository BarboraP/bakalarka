package code;

import java.util.UUID;

public abstract class LogicGate {

    private boolean isWorking;
    private Connector output;
    private Connector input1;
    private Connector input2;
    private UUID id;


    public LogicGate(UUID pid) {
        isWorking = true;
        id = pid;
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

    public Connector getInput1() {
        return input1;
    }

    public void setInput1(Connector input1) {
        this.input1 = input1;
    }

    public Connector getInput2() {
        return input2;
    }

    public void setInput2(Connector input2) {
        this.input2 = input2;
    }

    public UUID getId() {
        return id;
    }
}
