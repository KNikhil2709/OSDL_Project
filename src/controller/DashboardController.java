package controller;

import app.MainApp;
import util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML private Label welcomeLabel;

    @FXML
    public void initialize() {
        if (welcomeLabel != null) {
            String user = SessionManager.getUser();
            welcomeLabel.setText(user != null ? "Hi, " + user : "");
        }
    }

    public void openMovies() throws Exception {
        MainApp.switchScene("movies.fxml");
    }

    public void logout() throws Exception {
        SessionManager.setUser(null);
        MainApp.switchScene("login.fxml", 480, 580);
    }
}
