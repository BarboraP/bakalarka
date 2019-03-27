package graphics_controls;

import code.LogicCircuit;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DefineFailure extends AnchorPane {
    @FXML
    private ComboBox<String> combo_gate_type = null;
    @FXML
    private TableView<Boolean> table_edit = null;
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

        for (int i = 0; i < 3; i++) {
            TableColumn column = new TableColumn();
            table_edit.getColumns().add(column);
            column.setCellValueFactory(c -> new SimpleStringProperty("false"));
            column.setCellFactory(TextFieldTableCell.<Boolean>forTableColumn());
        }
        table_edit.getItems().addAll(false, false, false, false);
        table_edit.setFixedCellSize(25);
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

            System.out.println("swds");
        });

    }

    private List<String> getDataFromTable() {
        ObservableList<TableColumn<Boolean, ?>> columns = table_edit.getColumns();
        List<String> columnData = new ArrayList<>();

        for (TableColumn column : columns) {
            for (Boolean item : table_edit.getItems()) {
                columnData.add(column.getCellObservableValue(item).getValue().toString());
            }
        }
        return columnData;
    }

    private boolean[][] convertToTable(List<String> columnData) {
        boolean[][] list = new boolean[4][3];
        for (int i = 0; i < 4; i++) {
            int a = i * 3;
            for (int j = 0; j < 3; j++) {
                list[i][j] = Boolean.parseBoolean(columnData.get(j + a));
            }

        }
        return list;
    }
}
