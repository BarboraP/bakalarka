package graphics_controls;


import code.Connector;
import code.DataManager;
import code.LogicCircuit;
import code.LogicGate;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Point2D;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class RootLayout extends AnchorPane {

    @FXML
    private SplitPane base_pane = null;
    @FXML
    private AnchorPane right_pane = null;
    @FXML
    private VBox left_pane = null;

    @FXML
    private VBox right_box = null;
    @FXML
    private Button button_truthTable = null;
    @FXML
    private Button button_define_failure = null;
    @FXML
    private ScrollPane sc = null;
    @FXML
    private AnchorPane base_right_pane = null;
    @FXML
    private Button but_save = null;
    @FXML
    private Button but_load = null;

    private DragIcon dragOverIcon = null;

    private EventHandler<DragEvent> iconDragOverRootHandler = null;
    private EventHandler<DragEvent> iconDragDroppedHandler = null;
    private EventHandler<DragEvent> iconDragOverRightPaneHandler = null;

    private LogicCircuit circuit = null;
    private DataManager manager = null;


    public RootLayout() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/RootLayout.fxml"));
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

        setTopAnchor(right_box, 0.0);
        setRightAnchor(right_box, 0.0);
        setBottomAnchor(right_box, 0.0);


        base_right_pane.setTopAnchor(sc, 0.0);
        base_right_pane.setBottomAnchor(sc, 0.0);
        base_right_pane.setLeftAnchor(sc, 0.0);
        base_right_pane.setRightAnchor(sc, 0.0);


        dragOverIcon = new DragIcon();
        dragOverIcon.setVisible(false);
        dragOverIcon.setOpacity(0.65);
        getChildren().add(dragOverIcon);

        //populate left pane with multiple colored icons for testing
        //this is where we will add gates types
        for (int i = 0; i < GateType.values().length; i++) {
            DragIcon icn = new DragIcon();
            setStyleIcon(GateType.values()[i], icn);
            addDragDetection(icn);
            left_pane.getChildren().add(icn);
        }

        buildDragHandlers();
        buttonTable();
        buttonFailure();
        buttonSave();
        buttonLoad();

    }

    public void buttonTable() {
        button_truthTable.setOnAction((event) -> {
            circuit.getTruthTable();
            circuit.getFailureTable();

            Stage tabs = new Stage();

            tabs = new Stage();
            ReliabilityAnalysis ra = new ReliabilityAnalysis();
            ra.setTruthTable(circuit.returnTruthTable());
            ra.setCompTable(circuit.returnCompleteTable());


            ra.initTable(ra.getTableComp(), ra.getTable_comp());
            ra.initTable(ra.getTableTruth(), ra.getTable_truth());
            ra.setLabelRelText(circuit.getRel());

            tabs.setScene(new Scene(ra));

            tabs.initModality(Modality.WINDOW_MODAL);
            tabs.initOwner(this.getScene().getWindow());
            tabs.show();

        });


    }

    public void buttonFailure() {

        button_define_failure.setOnAction((event) -> {
            Stage edit = new Stage();

            edit = new Stage();
            DefineFailure ra = new DefineFailure();
            ra.setCircuit(circuit);

            edit.setScene(new Scene(ra));

            edit.initModality(Modality.WINDOW_MODAL);
            edit.initOwner(this.getScene().getWindow());
            edit.show();
        });

    }

    public void buttonSave() {

        but_save.setOnAction((event) -> {
            try {
                manager.saveCircuit(circuit,"test");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public void buttonLoad() {

        but_load.setOnAction((event) -> {
            try {
                manager.loadCircuit("test");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public void initCircuit(LogicCircuit c) {
        circuit = c;
    }
    public void initManager(DataManager m){
        manager = m;
    }

    public LogicCircuit getCircuit() {
        return circuit;
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
                        gate.setCircuit(circuit);
                        GateType type = GateType.valueOf(container.getValue("type"));
                        setStyleGate(type, gate);
                        right_pane.getChildren().add(gate);



                        //adds gate to the children of right_pane
                        Point2D cursorPoint = container.getValue("scene_coords");
                        gate.relocateToPoint(new Point2D(cursorPoint.getX() - 32, cursorPoint.getY() - 32));
                        circuit.addGate(type, gate.getId(), cursorPoint.getY());
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

                            LogicGate sourceGate = circuit.getGateById(sourceId);
                            LogicGate targetGate = circuit.getGateById(targetId);
                            Connector connector = new Connector(sourceGate, targetGate, link.getId());
                            circuit.addConnector(connector);

                            if (targetGate == null) {
                                link.bindEnds(source, target);
                                sourceGate.setOutput(connector);
                            } else {
                                circuit.getGateById(targetId).addInputConnector(connector);
                                link.bindEnds(source, target);
                                sourceGate.setOutput(connector);
                            }
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

    private String getStyle(GateType type) {
        String result = null;
        switch (type) {
            case and:
                result = "and";
                break;
            case or:
                result = "or";
                break;

            case nand:
                result = "nand";
                break;

            case nor:
                result = "nor";
                break;

            case xor:
                result = "xor";
                break;

            case input:
                result = "input";
                break;
            case output:
                result = "output";

            default:
                break;
        }
        return result;
    }

    private void setStyleIcon(GateType type, DragIcon icon) {
        icon.setType(type);
        icon.getStyleClass().clear();
        icon.getStyleClass().add("dragicon");
        icon.getStyleClass().add(getStyle(type));

    }

    private void setStyleGate(GateType type, DraggableGate gate) {
        gate.setType(type);
        gate.getStyleClass().clear();
        gate.getStyleClass().add("dragicon");
        gate.getStyleClass().add(getStyle(type));
        if (getStyle(type) == "input" || getStyle(type) == "output") {
            gate.setAsInput();
        }
        gate.setLabel(getStyle(type));
    }
}
