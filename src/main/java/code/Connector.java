package code;

public class Connector {
    private final String id;
    private final LogicGate startGate; //make it gates??
    private final LogicGate endGate;

    public Connector(LogicGate startGate, LogicGate endGate, String id) {
        this.startGate = startGate;
        this.endGate = endGate;
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
}
