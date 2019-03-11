package graphics_controls;

import javafx.scene.input.DataFormat;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DragContainer implements Serializable {

    private static final long serialVersionUID = -1458406119115196098L;

    private final List<Pair<String, Object>> mDataPairs = new ArrayList<Pair<String, Object>>();

    public static final DataFormat AddNode =
            new DataFormat("application.DragIcon.add");

    public static final DataFormat DragGate =
            new DataFormat("application.DraggableGate.drag");

    public static final DataFormat AddLink = new DataFormat("application.Link.add");


    public DragContainer() {
    }

    public void addData(String key, Object value) {
        mDataPairs.add(new Pair<String, Object>(key, value));
    }

    public <T> T getValue(String key) {

        for (Pair<String, Object> data : mDataPairs) {

            if (data.getKey().equals(key))
                return (T) data.getValue();
        }

        return null;
    }

    public List<Pair<String, Object>> getData() {
        return mDataPairs;
    }
}

