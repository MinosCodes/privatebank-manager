package ui;

import bank.PrivateBank;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.controller.AccountViewController;
import ui.controller.BaseController;
import ui.controller.MainViewController;

import java.io.IOException;

/**
 * Steuert Laden und Wechsel der JavaFX-Szenen und stellt die gemeinsame PrivateBank-Instanz bereit.
 */
public class SceneManager {

    private final Stage stage;
    private final PrivateBank bank;

    public SceneManager(Stage stage, PrivateBank bank) {
        this.stage = stage;
        this.bank = bank;
    }

    public void showMainView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/main-view.fxml"));
        Parent root = loader.load();
        MainViewController controller = loader.getController();
        initializeController(controller);
        stage.setScene(new Scene(root));
        stage.setTitle("PrivateBank - Konten");
        stage.show();
    }

    public void showAccountView(String account) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/account-view.fxml"));
        Parent root = loader.load();
        AccountViewController controller = loader.getController();
        initializeController(controller);
        controller.setAccount(account);
        stage.setScene(new Scene(root));
        stage.setTitle("PrivateBank - " + account);
        stage.show();
    }

    private void initializeController(BaseController controller) {
        controller.init(bank, this);
    }
}
