package graphics_controls;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.CubicCurve;
import javafx.geometry.Point2D;

import java.util.UUID;
import java.io.IOException;


public class Link extends AnchorPane {

    @FXML
    private CubicCurve node_link = null;

    public Link() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/Link.fxml")
        );
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        //provide a universally unique identifier for this object
        setId(UUID.randomUUID().toString());
    }

    @FXML
    private void initialize() {
        node_link.controlX1Property().bind(Bindings.add(node_link.startXProperty(), 100));
        node_link.controlX2Property().bind(Bindings.add(node_link.endXProperty(), -100));
        node_link.controlY1Property().bind(Bindings.add(node_link.startYProperty(), 0));
        node_link.controlY2Property().bind(Bindings.add(node_link.endYProperty(), 0));
    }

    public void setStart(Point2D startPoint) {
        node_link.setStartX(startPoint.getX());
        node_link.setStartY(startPoint.getY());
    }

    public void setEnd(Point2D endPoint) {
        node_link.setEndX(endPoint.getX());
        node_link.setEndY(endPoint.getY());
    }

    public void bindEnds(DraggableGate source, DraggableGate target) {
        node_link.startXProperty().bind(Bindings.add(source.layoutXProperty(), (source.getWidth() / 2.0)));
        node_link.startYProperty().bind(Bindings.add(source.layoutYProperty(), (source.getWidth() / 2.0)));
        node_link.endXProperty().bind(Bindings.add(target.layoutXProperty(), (target.getWidth() / 2.0)));
        node_link.endYProperty().bind(Bindings.add(target.layoutYProperty(), (target.getWidth() / 2.0)));
    }
}

