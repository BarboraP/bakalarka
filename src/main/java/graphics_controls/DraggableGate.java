package graphics_controls;

import code.LogicCircuit;
import code.LogicGate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.UUID;

public class DraggableGate extends AnchorPane {

    @FXML
    private AnchorPane root_pane = null;
    @FXML
    private Label title_bar = null;
    @FXML
    private Label close_button = null;
    @FXML
    private AnchorPane left_link_handle = null;
    @FXML
    private AnchorPane right_link_handle = null;
    @FXML
    private AnchorPane node_body = null;
    @FXML
    private HBox hBox = null;
    @FXML
    private VBox vBox = null;

    private EventHandler<DragEvent> contextDragOverHandler;
    private EventHandler<DragEvent> contextDragDroppedHandler;

    private EventHandler<MouseEvent> linkHandleDragDetectedHandler;//handle drag operations on the link handles of a gate
    private EventHandler<DragEvent> linkHandleDragDroppedHandler;//handle drag operations on the link handles of a gate
    private EventHandler<DragEvent> contextLinkDragOverHandler;//handle drag ops
    private EventHandler<DragEvent> contextLinkDragDroppedHandler;

    private Point2D dragOffset = new Point2D(0, 0);
    private final DraggableGate self; //for removing node // reference to the class instance stored in the member
    private Link dragLink = null;
    private AnchorPane right_pane = null; // reference to gate's parent
    private GateType type;
    private LogicCircuit circuit = null;


    public DraggableGate() {

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/DraggableGate.fxml")
        );

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        self = this;

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        setId(UUID.randomUUID().toString());
        System.out.println(getId());
    }


    public void setCircuit(LogicCircuit c) {
        circuit = c;
    }

    @FXML
    private void initialize() {

        buildNodeDragHandlers();
        buildLinkDragHandlers();

        left_link_handle.setOnDragDetected(linkHandleDragDetectedHandler);
        right_link_handle.setOnDragDetected(linkHandleDragDetectedHandler);

        left_link_handle.setOnDragDropped(linkHandleDragDroppedHandler);
        right_link_handle.setOnDragDropped(linkHandleDragDroppedHandler);

        dragLink = new Link();
        dragLink.setVisible(false);

        parentProperty().addListener(new ChangeListener<Parent>() {
            public void changed(ObservableValue<? extends Parent> observable, Parent oldValue, Parent newValue) {
                right_pane = (AnchorPane) getParent();
            }
        });


    }


    public void relocateToPoint(Point2D p) {
        //relocates the object to a point that has been converted to scene coordinates
        Point2D localCoords = getParent().sceneToLocal(p);
        relocate((int) (localCoords.getX() - dragOffset.getX()), (int) (localCoords.getY() - dragOffset.getY()));
    }


    private void contextDragDropped() {
        //dragdrop for node dragging
        contextDragDroppedHandler = new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                getParent().setOnDragOver(null);
                getParent().setOnDragDropped(null);
                event.setDropCompleted(true);
                event.consume();
            }
        };
    }

    private void labelDragDetection() {
        //drag detection for node dragging
        title_bar.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                getParent().setOnDragOver(null);
                getParent().setOnDragDropped(null);

                getParent().setOnDragOver(contextDragOverHandler);
                getParent().setOnDragDropped(contextDragDroppedHandler);

                //begin drag ops
                dragOffset = new Point2D(event.getX(), event.getY());
                relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                ClipboardContent content = new ClipboardContent();
                DragContainer container = new DragContainer();
                container.addData("type", type.toString());
                content.put(DragContainer.AddNode, container);

                startDragAndDrop(TransferMode.ANY).setContent(content);
                event.consume();
            }
        });
    }

    private void contextDragOver() {
        contextDragOverHandler = new EventHandler<DragEvent>() {
            //dragover to handle node dragging in the right pane view
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.ANY);
                relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                event.consume();
            }
        };
    }

    private void closeButton() {
        close_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                AnchorPane parent = (AnchorPane) self.getParent();
                ArrayList<String> a = circuit.removeGateById(self.getId());

                for (int i = 0; i < a.size(); i++) {
                    for (ListIterator<Node> iterNode = parent.getChildren().listIterator(); iterNode.hasNext(); ) {
                        Node node = iterNode.next();
                        if (node.getId() == null)
                            continue;
                        if (node.getId().equals(a.get(i)))
                            iterNode.remove();
                    }
                }
                parent.getChildren().remove(self);
            }
        });
    }


    private void buildNodeDragHandlers() {
        //if anything goes wrong, just copy code from methods above in the same order back here
        contextDragDropped();
        labelDragDetection();
        contextDragOver();
        closeButton();
    }


    private void linkDragDetected() {
        linkHandleDragDetectedHandler = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {

                getParent().setOnDragOver(null);
                getParent().setOnDragDropped(null);

                getParent().setOnDragOver(contextLinkDragOverHandler);
                getParent().setOnDragDropped(contextLinkDragDroppedHandler);

                //Set up user-draggable link
                right_pane.getChildren().add(0, dragLink);
                dragLink.setVisible(false);
                Point2D p = new Point2D(getLayoutX() + (getWidth() / 2.0), getLayoutY() + (getHeight() / 2.0));
                dragLink.setStart(p);

                //Drag content code
                ClipboardContent content = new ClipboardContent();
                DragContainer container = new DragContainer();
                container.addData("source", getId());
                content.put(DragContainer.AddLink, container);
                startDragAndDrop(TransferMode.ANY).setContent(content);

                event.consume();
            }
        };
    }

    private void linkDragDropped() {
        linkHandleDragDroppedHandler = new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                getParent().setOnDragOver(null);
                getParent().setOnDragDropped(null);

                //get the drag data.  If it's null, abort. This isn't the drag event we're looking for.
                DragContainer container =
                        (DragContainer) event.getDragboard().getContent(DragContainer.AddLink);

                if (container == null) {
                    return;
                }

                //hide the draggable Link and remove it from the right-hand AnchorPane's children
                dragLink.setVisible(false);
                right_pane.getChildren().remove(0);

                ClipboardContent content = new ClipboardContent();
                container.addData("target", getId());
                content.put(DragContainer.AddLink, container);
                event.getDragboard().setContent(content);
                event.setDropCompleted(true);
                event.consume();
            }
        };
    }

    private void contextLinkDragOver() {
        contextLinkDragOverHandler = new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.ANY);

                //Relocate user-draggable link
                if (!dragLink.isVisible()) {
                    dragLink.setVisible(true);
                }

                //end of curve tracks with mouse cursor
                dragLink.setEnd(new Point2D(event.getX(), event.getY()));

                event.consume();
            }
        };
    }

    private void contextLinkDragDropped() {
        contextLinkDragDroppedHandler = new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {

                System.out.println("context link drag dropped");
                getParent().setOnDragOver(null);
                getParent().setOnDragDropped(null);

                //hide the draggable Link and remove it from the right-hand AnchorPane's children
                dragLink.setVisible(false);
                right_pane.getChildren().remove(0);

                event.setDropCompleted(true);
                event.consume();
            }
        };
    }

    private void buildLinkDragHandlers() {
        linkDragDetected();
        linkDragDropped();
        contextLinkDragOver();
        contextLinkDragDropped();
    }

    public GateType getType() {
        return type;
    }

    public void setType(GateType type) {
        this.type = type;
    }

    public void setAsInput() {
        title_bar.setText(" ");
        left_link_handle.setPrefSize(0, 0);
        node_body.setPrefSize(0, 0);
        right_link_handle.setPrefSize(30, 22);
        hBox.setPrefSize(30, 22);
        vBox.setPrefSize(30, 32);
        root_pane.setPrefHeight(32.0);
        root_pane.setPrefWidth(30);
    }
}
