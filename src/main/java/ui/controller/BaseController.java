package ui.controller;

import bank.PrivateBank;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import ui.SceneManager;

/**
 * Shared base controller that exposes common helpers for all scenes.
 */
public abstract class BaseController {

    protected PrivateBank bank;
    protected SceneManager sceneManager;

    public void init(PrivateBank bank, SceneManager sceneManager) {
        this.bank = bank;
        this.sceneManager = sceneManager;
        onReady();
    }

    /**
     * Template method that allows subclasses to perform initialization after dependencies are set.
     */
    protected void onReady() {
        // optional hook
    }

    protected void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    protected boolean showConfirmation(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        return alert.showAndWait().filter(ButtonType.OK::equals).isPresent();
    }
}
