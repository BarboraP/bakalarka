package code;


import graphics_controls.RootLayout;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        LogicCircuit circuit = new LogicCircuit();
        RootLayout rootLayout = new RootLayout();
        rootLayout.initCircuit(circuit);

        try {
            Scene scene = new Scene(root, 640, 480);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        root.setCenter(rootLayout);

    }

    public static void main(String[] args) {
        launch(args);
    }
}

