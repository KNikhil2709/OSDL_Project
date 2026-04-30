package util;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Themed modal dialogs for CINEMAX — replaces the default JavaFX Alert.
 */
public class StyledDialog {

    public enum Type { WARNING, INFO, ERROR }

    /**
     * Show a themed dialog and block until the user dismisses it.
     */
    public static void show(Type type, String message) {
        show(type, message, null);
    }

    public static void show(Type type, String message, Runnable onClose) {

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.setResizable(false);

        // ── Accent colour & icon by type ───────────────────────────────────
        String accent, iconText, titleText;
        switch (type) {
            case WARNING -> { accent = "#f4a261"; iconText = "⚠"; titleText = "Warning"; }
            case ERROR   -> { accent = "#e92000"; iconText = "✕"; titleText = "Error";   }
            default      -> { accent = "#56cfe1"; iconText = "✓"; titleText = "Success"; }
        }

        // ── Icon circle ────────────────────────────────────────────────────
        Label icon = new Label(iconText);
        icon.setStyle(
            "-fx-font-size: 20px; -fx-text-fill: " + accent + ";"
        );
        StackPane iconCircle = new StackPane(icon);
        iconCircle.setPrefSize(48, 48);
        iconCircle.setStyle(
            "-fx-background-color: " + accent + "18;" +
            "-fx-background-radius: 24;" +
            "-fx-border-color: " + accent + "44;" +
            "-fx-border-radius: 24;" +
            "-fx-border-width: 1;"
        );

        // ── Title ──────────────────────────────────────────────────────────
        Label title = new Label(titleText);
        title.setStyle(
            "-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: white;"
        );

        // ── Message ────────────────────────────────────────────────────────
        Label msg = new Label(message);
        msg.setWrapText(true);
        msg.setMaxWidth(320);
        msg.setStyle(
            "-fx-font-size: 12px; -fx-text-fill: #888888; -fx-line-spacing: 3;"
        );

        // ── Dismiss button ─────────────────────────────────────────────────
        Button okBtn = new Button("OK");
        okBtn.setPrefWidth(120);
        okBtn.setPrefHeight(38);
        okBtn.setStyle(
            "-fx-background-color: " + accent + ";" +
            "-fx-text-fill: " + (type == Type.INFO ? "#0d0d0d" : "white") + ";" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 12px;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );
        okBtn.setOnMouseEntered(e -> okBtn.setStyle(
            "-fx-background-color: " + accent + "cc;" +
            "-fx-text-fill: " + (type == Type.INFO ? "#0d0d0d" : "white") + ";" +
            "-fx-font-weight: bold; -fx-font-size: 12px;" +
            "-fx-background-radius: 8; -fx-cursor: hand;"
        ));
        okBtn.setOnMouseExited(e -> okBtn.setStyle(
            "-fx-background-color: " + accent + ";" +
            "-fx-text-fill: " + (type == Type.INFO ? "#0d0d0d" : "white") + ";" +
            "-fx-font-weight: bold; -fx-font-size: 12px;" +
            "-fx-background-radius: 8; -fx-cursor: hand;"
        ));

        // ── Top accent bar ─────────────────────────────────────────────────
        HBox topBar = new HBox();
        topBar.setPrefHeight(3);
        topBar.setStyle("-fx-background-color: " + accent + "; -fx-background-radius: 14 14 0 0;");

        // ── Card body ──────────────────────────────────────────────────────
        VBox body = new VBox(16);
        body.setAlignment(Pos.CENTER_LEFT);
        body.setPadding(new Insets(24, 28, 28, 28));
        body.getChildren().addAll(iconCircle, title, msg, new Region(), okBtn);

        VBox.setVgrow(body.getChildren().get(3), Priority.ALWAYS);
        okBtn.setAlignment(Pos.CENTER);
        VBox.setMargin(okBtn, new Insets(4, 0, 0, 0));
        body.setAlignment(Pos.CENTER_LEFT);

        // Center the button
        HBox btnRow = new HBox(okBtn);
        btnRow.setAlignment(Pos.CENTER);
        body.getChildren().remove(okBtn);
        body.getChildren().add(btnRow);

        VBox card = new VBox(topBar, body);
        card.setMinWidth(360);
        card.setMaxWidth(380);
        card.setStyle(
            "-fx-background-color: #141414;" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: #252525;" +
            "-fx-border-radius: 14;" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.85), 60, 0, 0, 20);"
        );

        // ── Backdrop ───────────────────────────────────────────────────────
        StackPane root = new StackPane(card);
        root.setStyle("-fx-background-color: rgba(0,0,0,0.0);");
        root.setPadding(new Insets(40));

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);

        // ── Entry animation ────────────────────────────────────────────────
        card.setScaleX(0.88);
        card.setScaleY(0.88);
        card.setOpacity(0);

        dialog.show();

        ScaleTransition scale = new ScaleTransition(Duration.millis(200), card);
        scale.setToX(1.0);
        scale.setToY(1.0);
        FadeTransition fade = new FadeTransition(Duration.millis(200), card);
        fade.setToValue(1.0);
        scale.play();
        fade.play();

        // ── Close logic ────────────────────────────────────────────────────
        Runnable dismiss = () -> {
            FadeTransition out = new FadeTransition(Duration.millis(130), card);
            out.setToValue(0);
            ScaleTransition sOut = new ScaleTransition(Duration.millis(130), card);
            sOut.setToX(0.92);
            sOut.setToY(0.92);
            out.setOnFinished(ev -> {
                dialog.close();
                if (onClose != null) onClose.run();
            });
            out.play();
            sOut.play();
        };

        okBtn.setOnAction(e -> dismiss.run());

        dialog.showAndWait();
    }
}
