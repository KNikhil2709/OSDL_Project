package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import util.StyledDialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import service.AuthenticationService;
import util.SessionManager;
import app.MainApp;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    AuthenticationService authService = new AuthenticationService();

    @FXML
    private void handleLogin(ActionEvent event) throws Exception {

        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password.");
            return;
        }

        if (authService.login(username, password)) {
            SessionManager.setUser(username);
            
            MainApp.switchScene("dashboard.fxml");
        } else {
            showError("Invalid username or password. Please try again.");
            passwordField.clear();
        }
    }

    private void showError(String message) {
        // Use inline label if present, fallback to StyledDialog
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
        } else {
            StyledDialog.show(StyledDialog.Type.ERROR, message);
        }
    }
}
