package graphics_controls;

import code.LogicCircuit;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowFunction extends AnchorPane {
    @FXML
    private ComboBox<String> combo_gate_type2 = null;
    @FXML
    private TableView<boolean[]> table_show = null;
    @FXML
    private Button button_show = null;

    private LogicCircuit circuit;


    public ShowFunction() {

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/ShowFunction.fxml")
        );

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        setItemsInCombo();
       // initTable();
        buttonFailure();
    }

    public void setCircuit(LogicCircuit c) {
        circuit = c;
    }


   /* public void initTable(boolean[][] tab) {
        for (int i = 0; i < tab.length; i++) {

            if(tableShow == null){
                tableTruth = new  Boolean[tab.length][tab[i].length];
            }

            for (int j = 0; j < tab[i].length; j++) {
                tableTruth[i][j] = (Boolean) tab[i][j];
            }
        }
    }*/

    public void setItemsInCombo() {
        ArrayList<String> list = new ArrayList<>();
        GateType[] g = GateType.values();
        for (int i = 0; i < g.length - 2; i++) {
            list.add(g[i].toString());
        }
        combo_gate_type2.setItems(FXCollections.observableArrayList(list));
    }


    public void buttonFailure() {
        button_show.setOnAction((event) -> {
            String type = combo_gate_type2.getValue();
            initTable(circuit.getFailureFuncion(type), table_show);
        });
    }

    public void initTable(boolean[][] table, TableView<boolean[]> view) {
        ObservableList<boolean[]> data = FXCollections.observableArrayList();
        if(table != null) {
            data.addAll(Arrays.asList(table));

            for (int i = 0; i < table[0].length; i++) {
                TableColumn column = new TableColumn();
                final int colNo = i;
                column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<boolean[], String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<boolean[], String> p) {
                        return new SimpleObjectProperty(p.getValue()[colNo]);
                    }
                });
                column.setPrefWidth(90);
                view.getColumns().add(column);
            }
            view.setItems(data);
        }
    }


}
