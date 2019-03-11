package graphics_controls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DragIcon extends AnchorPane {

    private DragIconType type;

    public DragIcon() {

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/DragIcon.fxml")
        );
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void initialize() {
    }

    public DragIconType getType() {
        return type;
    }

    public void setType(DragIconType type) {
        this.type = type;
    }

    public void relocateToPoint(Point2D p) {
        //relocates the object to a point that has been converted to
        //scene coordinates
        Point2D localCoords = getParent().sceneToLocal(p);
        relocate((int) (localCoords.getX() - (getBoundsInLocal().getWidth() / 2)),
                 (int) (localCoords.getY() - (getBoundsInLocal().getHeight() / 2))
        );
    }
}
