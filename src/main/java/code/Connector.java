package code;

import java.util.UUID;

public class Connector {
    private final String id;
    private final String startGateId;
    private final String endGateId;

    public Connector(String startGateId, String endGateId, String id) {
        this.startGateId = startGateId;
        this.endGateId = endGateId;
        this.id = id;
    }
}
