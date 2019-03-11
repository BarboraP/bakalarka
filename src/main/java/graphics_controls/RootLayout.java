package graphics_controls;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Point2D;

import java.io.IOException;

public class RootLayout extends AnchorPane {

    @FXML
    private SplitPane base_pane = null;
    @FXML
    private AnchorPane right_pane = null;
    @FXML
    private VBox left_pane = null;
    private DragIcon dragOverIcon = null;

    private EventHandler<DragEvent> iconDragOverRootHandler = null;
    private EventHandler<DragEvent> iconDragDroppedHandler = null;
    private EventHandler<DragEvent> iconDragOverRightPaneHandler = null;

    public RootLayout() {

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/RootLayout.fxml")
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
        //Add one icon that will be used for the drag-drop process
        //This is added as a child to the root AnchorPane so it can be
        //visible on both sides of the split pane.

        dragOverIcon = new DragIcon();
        dragOverIcon.setVisible(false);
        dragOverIcon.setOpacity(0.65);
        getChildren().add(dragOverIcon);

        //populate left pane with multiple colored icons for testing
        //this is where we will add gates types
        for (int i = 0; i < 7; i++) {
            DragIcon icn = new DragIcon();
            setStyleIcon(DragIconType.values()[i], icn);
            addDragDetection(icn);
            left_pane.getChildren().add(icn);
        }

        buildDragHandlers();
    }

    private void iconDragOverRoot() {
        iconDragOverRootHandler = new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                Point2D p = right_pane.sceneToLocal(event.getSceneX(), event.getSceneY());

                if (!right_pane.boundsInLocalProperty().get().contains(p)) {
                    dragOverIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                    return;
                }
                event.consume();
            }
        };
    }

    private void iconDragOverRightPane() {
        iconDragOverRightPaneHandler = new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.ANY);
                dragOverIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                event.consume();
            }
        };
    }

    private void iconDragDropped() {
        iconDragDroppedHandler = new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {

                DragContainer container = (DragContainer) event.getDragboard().getContent(DragContainer.AddNode);
                container.addData("scene_coords", new Point2D(event.getSceneX(), event.getSceneY()));
                ClipboardContent content = new ClipboardContent();
                content.put(DragContainer.AddNode, container);
                event.getDragboard().setContent(content);
                event.setDropCompleted(true);
            }
        };
    }

    private void onDragDone() {
        this.setOnDragDone(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {

                right_pane.removeEventHandler(DragEvent.DRAG_OVER, iconDragOverRightPaneHandler);
                right_pane.removeEventHandler(DragEvent.DRAG_DROPPED, iconDragDroppedHandler);
                base_pane.removeEventHandler(DragEvent.DRAG_OVER, iconDragOverRootHandler);

                dragOverIcon.setVisible(false);
                DragContainer container = (DragContainer) event.getDragboard().getContent(DragContainer.AddNode);

                if (container != null) {
                    if (container.getValue("scene_coords") != null) {

                        System.out.println(container.getData().toString());
                        DraggableGate gate = new DraggableGate();
                        setStyleGate(DragIconType.valueOf(container.getValue("type").toString()), gate);
                        right_pane.getChildren().add(gate);
                        //adds gate to the children of right_pane
                        Point2D cursorPoint = container.getValue("scene_coords");
                        gate.relocateToPoint(new Point2D(cursorPoint.getX() - 32, cursorPoint.getY() - 32));
                    }
                }

                //AddLink drag operation
                container = (DragContainer) event.getDragboard().getContent(DragContainer.AddLink);

                if (container != null) {
                    //bind the ends of our link to the nodes whose id's are stored in the drag container
                    String sourceId = container.getValue("source");
                    String targetId = container.getValue("target");

                    if (sourceId != null && targetId != null) {

                        Link link = new Link();

                        //add our link at the top of the rendering order so it's rendered first
                        right_pane.getChildren().add(0, link);
                        DraggableGate source = null;
                        DraggableGate target = null;

                        for (Node n : right_pane.getChildren()) {

                            if (n.getId() == null) {
                                continue;
                            }

                            if (n.getId().equals(sourceId)) {
                                source = (DraggableGate) n;
                            }

                            if (n.getId().equals(targetId)) {
                                target = (DraggableGate) n;
                            }
                        }

                        if (source != null && target != null) {
                            link.bindEnds(source, target);
                        }
                    }
                }
                event.consume();
            }
        });
    }

    private void buildDragHandlers() {
        iconDragOverRoot();
        iconDragOverRightPane();
        iconDragDropped();
        onDragDone();
    }

    private void addDragDetection(DragIcon dragIcon) {
        dragIcon.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                // set the other drag event handles on their respective objects
                base_pane.setOnDragOver(iconDragOverRootHandler);
                right_pane.setOnDragOver(iconDragOverRightPaneHandler);
                right_pane.setOnDragDropped(iconDragDroppedHandler);

                //get a reference to the clicked DragIcon object
                DragIcon icon = (DragIcon) event.getSource();

                //begin drag operations
                setStyleIcon(icon.getType(), dragOverIcon);
                dragOverIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));

                ClipboardContent content = new ClipboardContent();
                DragContainer container = new DragContainer();

                container.addData("type", dragOverIcon.getType().toString());
                content.put(DragContainer.AddNode, container);

                dragOverIcon.startDragAndDrop(TransferMode.ANY).setContent(content);
                dragOverIcon.setVisible(true);
                dragOverIcon.setMouseTransparent(true);
                event.consume();
            }
        });
    }

    private String getStyle(DragIconType type) {
        String result = null;
        switch (type) {
            case blue:
                result = "icon-blue";
                break;
            case red:
                result = "icon-red";
                break;

            case green:
                result = "icon-green";
                break;

            case grey:
                result = "icon-grey";
                break;

            case purple:
                result = "icon-purple";
                break;

            case yellow:
                result = "icon-yellow";
                break;

            case black:
                result = "icon-black";
                break;

            default:
                break;
        }
        return result;
    }

    private void setStyleIcon(DragIconType type, DragIcon icon) {
        icon.setType(type);
        icon.getStyleClass().clear();
        icon.getStyleClass().add("dragicon");
        icon.getStyleClass().add(getStyle(type));
    }

    private void setStyleGate(DragIconType type, DraggableGate gate) {
        gate.setType(type);
        gate.getStyleClass().clear();
        gate.getStyleClass().add("dragicon");
        gate.getStyleClass().add(getStyle(type));
    }
}