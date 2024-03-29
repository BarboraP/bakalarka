package graphics_controls;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import javafx.scene.control.TableView;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class ReliabilityAnalysis extends AnchorPane {
    @FXML
    private TableView<Boolean[]> table_truth = null;
    @FXML
    private TableView<Boolean[]> table_comp = null;
    @FXML
    private Label label_rel = null;
    @FXML
    private Label label_struct = null;
    @FXML
    private Label label_im = null;

    private Boolean[][] tableTruth = null;
    private Boolean[][] tableComp = null;

    public ReliabilityAnalysis() {

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/ReliabilityAnalysis.fxml")
        );

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);


        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setLabelRelText(double value) {
        label_rel.setText(value + "%");
    }

    public void setLabelStructText(String value){
        label_struct.setText(value);
    }

    public void setLabelImportanceText(String value){
        label_im.setText(value);
    }



    public TableView<Boolean[]> getTable_truth() {
        return table_truth;
    }

    public TableView<Boolean[]> getTable_comp() {
        return table_comp;
    }

    private void initialize() {
    }

    public void setTruthTable(boolean[][] tab){

        for (int i = 0; i < tab.length; i++) {

            if(tableTruth == null){
                tableTruth = new  Boolean[tab.length][tab[i].length];
            }

            for (int j = 0; j < tab[i].length; j++) {
                tableTruth[i][j] = (Boolean) tab[i][j];
            }
        }
    }


    public void setCompTable(boolean[][] tab){

        for (int i = 0; i < tab.length; i++) {

            if(tableComp == null){
                tableComp = new  Boolean[tab.length][tab[i].length];
            }

            for (int j = 0; j < tab[i].length; j++) {
                tableComp[i][j] = (Boolean) tab[i][j];
            }
        }
    }

    public Boolean[][] getTableTruth() {
        return tableTruth;
    }

    public Boolean[][] getTableComp() {
        return tableComp;
    }

    public void initTable(Boolean[][] table, TableView<Boolean[]> view) {
        ObservableList<Boolean[]> data = FXCollections.observableArrayList();
        data.addAll(Arrays.asList(table));

        for (int i = 0; i < table[0].length; i++) {
            TableColumn column = new TableColumn();
            final int colNo = i;
            column.setCellValueFactory(new Callback<CellDataFeatures<Boolean[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(CellDataFeatures<Boolean[], String> p) {
                    //return new SimpleStringProperty((p.getValue()[colNo]));
                    return new SimpleStringProperty(p.getValue()[colNo].toString());
                }
            });
            column.setPrefWidth(90);
            view.getColumns().add(column);
        }
        view.setItems(data);
    }

}
