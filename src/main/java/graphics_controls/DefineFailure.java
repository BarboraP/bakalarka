package graphics_controls;

import code.LogicCircuit;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DefineFailure extends AnchorPane {
    @FXML
    private ComboBox<String> combo_gate_type = null;
    @FXML
    private TableView<String[]> table_edit = null;
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

        final ObservableList<String[]> data = FXCollections.observableArrayList(
                new String[3],
                new String[3],
                new String[3],
                new String[3]
        );

        int tmp;
        for (int j = 0; j < data.size(); j++) {
            tmp = j;
            for (int i = 0; i < 3; i++) {


                if (tmp % 2 == 1) {
                    data.get(j)[i] = "true";
                } else {
                    data.get(j)[i] = "false";
                }
                tmp = tmp >> 1;
            }
        }

        table_edit.setEditable(true);
        table_edit.setFixedCellSize(25);

        for (int i = 0; i < 3; i++) {
            TableColumn column = new TableColumn();

            final int a = i;

            column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    String[] x = p.getValue();

                    return new SimpleStringProperty(x[a]);

                }
            });

            column.setCellFactory(TextFieldTableCell.forTableColumn());
            column.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<String[], String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<String[], String> event) {

                            String[] user = event.getRowValue();
                            user[event.getTablePosition().getColumn()] = event.getNewValue();
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

    private List<String[]> getDataFromTable() {
        List<String[]> columnData = new ArrayList<>();
        for (String[] item : table_edit.getItems()) {
            columnData.add(item);
        }
        return columnData;
    }

    private boolean[][] convertToTable(List<String[]> columnData) {
        boolean[][] list = new boolean[4][3];
        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 3; j++) {
                list[i][j] = Boolean.parseBoolean(columnData.get(i)[j]);
            }


        }
        return list;
    }
}
