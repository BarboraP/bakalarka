package graphics_controls;

import code.LogicCircuit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DefineFailure extends AnchorPane {
    @FXML
    private ComboBox<String> combo_gate_type = null;
    @FXML
    private TableView<boolean[]> table_edit = null;
    @FXML
    private Button button_save = null;

    private LogicCircuit circuit;


    public DefineFailure() {

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/DefineFailure.fxml")
        );

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        setItemsInCombo();
        initTable();
        buttonFailure();
    }

    public void setCircuit(LogicCircuit c) {
        circuit = c;
    }


    public void initTable() {

        final ObservableList<boolean[]> data = FXCollections.observableArrayList(
                new boolean[3],
                new boolean[3],
                new boolean[3],
                new boolean[3]
        );

        for (boolean[] d : data) {
            for (int i = 0; i < 3; i++) {
                d[i] = false;
            }
        }

        table_edit.setEditable(true);
        table_edit.setFixedCellSize(25);

        for (int i = 0; i < 3; i++) {
            TableColumn column = new TableColumn();

            column.setCellValueFactory(new PropertyValueFactory<boolean[], String>(""));
            column.setCellFactory(TextFieldTableCell.forTableColumn());
            column.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<boolean[], String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<boolean[], String> event) {

                            boolean[] user = event.getRowValue();
                            user[event.getTablePosition().getColumn()] = Boolean.parseBoolean(event.getNewValue());
                        }
                    }
            );

            table_edit.setItems(data);
            table_edit.getColumns().add(column);
        }
    }

    public void setItemsInCombo() {
        ArrayList<String> list = new ArrayList<>();
        GateType[] g = GateType.values();
        for (int i = 0; i < g.length - 2; i++) {
            list.add(g[i].toString());
        }
        combo_gate_type.setItems(FXCollections.observableArrayList(list));
    }


    public void buttonFailure() {
        button_save.setOnAction((event) -> {
            String type = combo_gate_type.getValue();
            circuit.setFailure(type, convertToTable(getDataFromTable()));
        });
    }

    private List<boolean[]> getDataFromTable() {
        List<boolean[]> columnData = new ArrayList<>();
        for (boolean[] item : table_edit.getItems()) {
            columnData.add(item);
        }
        return columnData;
    }

    private boolean[][] convertToTable(List<boolean[]> columnData) {
        boolean[][] list = new boolean[4][3];
        for (int i = 0; i < 4; i++) {
            list[i] = columnData.get(i);
        }
        return list;
    }
}
