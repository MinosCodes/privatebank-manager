package ui;

import bank.PrivateBank;
import bank.exceptions.TransactionAttributeException;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX launcher that wires the PrivateBank domain model to the UI scenes.
 */
public class FxApplication extends Application {

    private static final String DATA_DIRECTORY = "data_json_app";
    private static final double DEFAULT_INCOMING_INTEREST = 0.02;
    private static final double DEFAULT_OUTGOING_INTEREST = 0.05;

    @Override
    public void start(Stage primaryStage) {
        try {
            PrivateBank bank = new PrivateBank(
                    "Campus PrivateBank",
                    DEFAULT_INCOMING_INTEREST,
                    DEFAULT_OUTGOING_INTEREST,
                    DATA_DIRECTORY
            );

            SceneManager sceneManager = new SceneManager(primaryStage, bank);
            sceneManager.showMainView();
        } catch (TransactionAttributeException | IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Start fehlgeschlagen");
            alert.setHeaderText("Die Anwendung konnte nicht initialisiert werden.");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
