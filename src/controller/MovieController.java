package controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.MovieService;
import model.Movie;
import util.SessionManager;
import app.MainApp;
import java.util.List;

public class MovieController {

    @FXML private FlowPane movieGrid;

    private MovieService service = new MovieService();
    private List<Movie> movies;

    
    private static final String[] POSTER_COLORS = {
        "#e92000", "#ff8c00", "#7c3aed", "#0891b2", "#059669"
    };

    @FXML
    public void initialize() {
        movies = service.getMovies();
        buildMovieCards();
    }

    private void buildMovieCards() {
        movieGrid.getChildren().clear();
        int idx = 0;
        for (Movie m : movies) {
            movieGrid.getChildren().add(createMovieCard(m, POSTER_COLORS[idx % POSTER_COLORS.length]));
            idx++;
        }
    }

    private VBox createMovieCard(Movie movie, String color) {
        VBox card = new VBox(0);
        card.setPrefWidth(190);
        card.setStyle(
            "-fx-background-color: #111111;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #1e1e1e;" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: 1;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, #00000088, 18, 0, 0, 6);"
        );

        
        StackPane poster = new StackPane();
        poster.setPrefHeight(240);
        poster.setStyle("-fx-background-color: " + color + "22;" +
                        "-fx-background-radius: 10 10 0 0;");

        
        Rectangle stripe = new Rectangle(190, 4);
        stripe.setFill(Color.web(color));
        StackPane.setAlignment(stripe, Pos.TOP_CENTER);

        
        Label genreBadge = new Label(movie.getGenre().toUpperCase());
        genreBadge.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 8px;" +
            "-fx-font-weight: bold;" +
            "-fx-letter-spacing: 1;" +
            "-fx-padding: 3 8;" +
            "-fx-background-radius: 4;"
        );
        StackPane.setAlignment(genreBadge, Pos.TOP_RIGHT);
        StackPane.setMargin(genreBadge, new Insets(12, 12, 0, 0));

        
        Label initial = new Label(movie.getName().substring(0, 1).toUpperCase());
        initial.setStyle(
            "-fx-font-size: 72px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + color + ";" +
            "-fx-opacity: 0.25;"
        );

        
        Label centreTitle = new Label(movie.getName().toUpperCase());
        centreTitle.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;" +
            "-fx-letter-spacing: 1;" +
            "-fx-text-alignment: center;"
        );
        centreTitle.setWrapText(true);
        centreTitle.setAlignment(Pos.CENTER);

        VBox textStack = new VBox(4, initial, centreTitle);
        textStack.setAlignment(Pos.CENTER);

        
        boolean imageLoaded = false;
        for (String ext : new String[]{"jpg", "jpeg", "png"}) {
            java.io.File imgFile = new java.io.File("src/images/" + movie.getId() + "." + ext);
            if (imgFile.exists()) {
                try {
                    javafx.scene.image.Image img = new javafx.scene.image.Image(imgFile.toURI().toString());
                    javafx.scene.image.ImageView iv = new javafx.scene.image.ImageView(img);
                    iv.setFitWidth(190);
                    iv.setFitHeight(240);
                    iv.setPreserveRatio(false);
                    Rectangle clip = new Rectangle(190, 240);
                    clip.setArcWidth(10); clip.setArcHeight(10);
                    iv.setClip(clip);
                    poster.getChildren().addAll(iv, stripe, genreBadge);
                    imageLoaded = true;
                } catch (Exception ignored) {}
                break;
            }
        }
        if (!imageLoaded) {
            poster.getChildren().addAll(stripe, textStack, genreBadge);
        }

        
        VBox info = new VBox(6);
        info.setPadding(new Insets(12, 14, 14, 14));
        info.setStyle("-fx-background-color: #0d0d0d; -fx-background-radius: 0 0 10 10;");

        Label nameLabel = new Label(movie.getName());
        nameLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: white;");
        nameLabel.setWrapText(true);

        Label priceLabel = new Label("Rs. " + movie.getPrice() + " / seat");
        priceLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #555555;");

        Label showsLabel = new Label(movie.getShowTimes().size() + " showtimes available");
        showsLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: " + color + ";");

        Button bookBtn = new Button("Book Tickets");
        bookBtn.setMaxWidth(Double.MAX_VALUE);
        bookBtn.setPrefHeight(34);
        bookBtn.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 11px;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-letter-spacing: 1;"
        );
        bookBtn.setOnAction(e -> openShowtimeDialog(movie, color));
        VBox.setMargin(bookBtn, new Insets(4, 0, 0, 0));

        info.getChildren().addAll(nameLabel, priceLabel, showsLabel, bookBtn);
        card.getChildren().addAll(poster, info);

        
        card.setOnMouseEntered(e -> card.setStyle(
            "-fx-background-color: #161616;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: " + color + "66;" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: 1;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, " + color + "44, 24, 0, 0, 8);"
        ));
        card.setOnMouseExited(e -> card.setStyle(
            "-fx-background-color: #111111;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #1e1e1e;" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: 1;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, #00000088, 18, 0, 0, 6);"
        ));

        
        card.setOnMouseClicked(e -> openShowtimeDialog(movie, color));

        return card;
    }

    private void openShowtimeDialog(Movie movie, String color) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(MainApp.primaryStage);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setTitle("Select Showtime");

        VBox root = new VBox(0);
        root.setStyle(
            "-fx-background-color: #111111;" +
            "-fx-border-color: #2a2a2a;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, #000000cc, 40, 0, 0, 12);"
        );

        
        HBox accent = new HBox();
        accent.setPrefHeight(4);
        accent.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 12 12 0 0;");

        VBox content = new VBox(16);
        content.setPadding(new Insets(22, 24, 24, 24));

        
        Label title = new Label(movie.getName());
        title.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitle = new Label("Select a showtime to continue");
        subtitle.setStyle("-fx-font-size: 11px; -fx-text-fill: #555555;");

        VBox header = new VBox(4, title, subtitle);

        
        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #1e1e1e;");

    
        VBox seatsBox = new VBox(6);
        Label seatsLbl = new Label("NUMBER OF SEATS");
        seatsLbl.setStyle("-fx-font-size: 9px; -fx-font-weight: bold; -fx-text-fill: #555555; -fx-letter-spacing: 2;");
        TextField seatsField = new TextField();
        seatsField.setPromptText("Enter 1–6");
        seatsField.setStyle(
            "-fx-background-color: #0d0d0d;" +
            "-fx-text-fill: white;" +
            "-fx-prompt-text-fill: #333333;" +
            "-fx-border-color: #2a2a2a;" +
            "-fx-border-radius: 7;" +
            "-fx-background-radius: 7;" +
            "-fx-padding: 10 14;" +
            "-fx-font-size: 13px;"
        );
        seatsBox.getChildren().addAll(seatsLbl, seatsField);

        
        Label timesLbl = new Label("AVAILABLE SHOWTIMES");
        timesLbl.setStyle("-fx-font-size: 9px; -fx-font-weight: bold; -fx-text-fill: #555555; -fx-letter-spacing: 2;");

        FlowPane timesPane = new FlowPane(8, 8);

        final String[] selectedTime = {null};
        final Button[] selectedBtn = {null};

        for (String t : movie.getShowTimes()) {
            Button timeBtn = new Button(t);
            timeBtn.setStyle(
                "-fx-background-color: #1a1a1a;" +
                "-fx-text-fill: #aaaaaa;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 9 18;" +
                "-fx-background-radius: 7;" +
                "-fx-border-color: #2a2a2a;" +
                "-fx-border-radius: 7;" +
                "-fx-border-width: 1;" +
                "-fx-cursor: hand;"
            );
            timeBtn.setOnAction(e -> {
                
                if (selectedBtn[0] != null) {
                    selectedBtn[0].setStyle(
                        "-fx-background-color: #1a1a1a; -fx-text-fill: #aaaaaa; -fx-font-size: 12px;" +
                        "-fx-font-weight: bold; -fx-padding: 9 18; -fx-background-radius: 7;" +
                        "-fx-border-color: #2a2a2a; -fx-border-radius: 7; -fx-border-width: 1; -fx-cursor: hand;"
                    );
                }
                selectedTime[0] = t;
                selectedBtn[0] = timeBtn;
                timeBtn.setStyle(
                    "-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 12px;" +
                    "-fx-font-weight: bold; -fx-padding: 9 18; -fx-background-radius: 7;" +
                    "-fx-border-color: " + color + "; -fx-border-radius: 7; -fx-border-width: 1; -fx-cursor: hand;"
                );
            });
            timesPane.getChildren().add(timeBtn);
        }

        
        Label errorLbl = new Label("");
        errorLbl.setStyle("-fx-font-size: 11px; -fx-text-fill: #e92000;");
        errorLbl.setVisible(false);

        
        Button proceedBtn = new Button("Choose Seats  →");
        proceedBtn.setMaxWidth(Double.MAX_VALUE);
        proceedBtn.setPrefHeight(44);
        proceedBtn.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;" +
            "-fx-background-radius: 8; -fx-cursor: hand;" +
            "-fx-letter-spacing: 1;" +
            "-fx-effect: dropshadow(gaussian, " + color + "66, 14, 0, 0, 4);"
        );
        proceedBtn.setOnAction(e -> {
            String time = selectedTime[0];
            if (time == null) {
                errorLbl.setText("Please select a showtime.");
                errorLbl.setVisible(true);
                return;
            }
            String countText = seatsField.getText().trim();
            if (countText.isEmpty()) {
                errorLbl.setText("Please enter the number of seats.");
                errorLbl.setVisible(true);
                return;
            }
            int count;
            try { count = Integer.parseInt(countText); }
            catch (NumberFormatException ex) {
                errorLbl.setText("Please enter a valid number.");
                errorLbl.setVisible(true);
                return;
            }
            if (count < 1 || count > 6) {
                errorLbl.setText("Please enter between 1 and 6 seats.");
                errorLbl.setVisible(true);
                return;
            }
            SessionManager.setSelectedMovieId(movie.getId());
            SessionManager.setSelectedMovieName(movie.getName());
            SessionManager.setSelectedMovieTime(time);
            SessionManager.setPricePerSeat(movie.getPrice());
            SessionManager.clearBookingData();
            SessionManager.setRequiredSeatCount(count);
            dialog.close();
            try { MainApp.switchScene("seats.fxml"); }
            catch (Exception ex) { ex.printStackTrace(); }
        });

        // Cancel
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle(
            "-fx-background-color: transparent; -fx-text-fill: #444444; -fx-font-size: 11px;" +
            "-fx-cursor: hand; -fx-border-color: #2a2a2a; -fx-border-radius: 6;" +
            "-fx-background-radius: 6; -fx-padding: 6 16;"
        );
        cancelBtn.setOnAction(e -> dialog.close());

        HBox buttons = new HBox(10, proceedBtn, cancelBtn);
        HBox.setHgrow(proceedBtn, Priority.ALWAYS);

        content.getChildren().addAll(header, sep, seatsBox, timesLbl, timesPane, errorLbl, buttons);
        root.getChildren().addAll(accent, content);

        Scene scene = new Scene(root, 360, 400);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        dialog.centerOnScreen();
        dialog.showAndWait();
    }

    public void goBack() throws Exception {
        MainApp.switchScene("dashboard.fxml");
    }

    // Keep selectMovie() stub so FXML doesn't break if referenced
    public void selectMovie() {}
}
