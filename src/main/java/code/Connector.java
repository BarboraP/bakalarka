package code;

public class Connector {
    private final String id;
    transient private  LogicGate startGate;
    transient private  LogicGate endGate;
    private String endGateId;
    private String startGateId;
    private int position;

    public Connector(LogicGate startGate, LogicGate endGate, String id) {
        this.startGate = startGate;
        this.endGate = endGate;
        this.id = id;
        endGateId = endGate.getId();
        startGateId = startGate.getId();
    }

    public Connector(String id){
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public LogicGate getStartGate() {
        return startGate;
    }

    public LogicGate getEndGate() {
        return endGate;
    }

    public void setPosition(int p) {
        position = p;
    }

    public int getPosition() {
        return position;
    }

    public void setStartGate(LogicGate gate) {
        startGate = gate;
    }

    public void setEndGate(LogicGate gate) {
        endGate = gate;
    }

    public String getEndGateId() {
        return endGateId;
    }

    public String getStartGateId() {
        return startGateId;
    }
}
