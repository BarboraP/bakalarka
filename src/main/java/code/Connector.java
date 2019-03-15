package code;

public class Connector {
    private final String id;
    private final String startGateId;
    private final String endGateId;

    public Connector(String startGateId, String endGateId, String id) {
        this.startGateId = startGateId;
        this.endGateId = endGateId;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getStartGateId() {
        return startGateId;
    }

    public String getEndGateId() {
        return endGateId;
    }
}
