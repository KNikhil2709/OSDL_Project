package controller;

import app.MainApp;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import util.StyledDialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import service.BookingService;
import util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class SeatController {

    @FXML private GridPane seatGrid;
    @FXML private Label movieLabel;
    @FXML private Label statusLabel;

    private static final int ROWS = 7;
    private static final int COLS = 8;
    private static final String[] ROW_LABELS = {"A","B","C","D","E","F","G"};

    // Polished seat colors
    private static final String BG_AVAILABLE  = "#0d2a1a";
    private static final String BG_SELECTED   = "#e94560";
    private static final String BG_BOOKED     = "#12121e";
    private static final String BD_AVAILABLE  = "#1d5c3a";
    private static final String BD_SELECTED   = "#ff6b82";
    private static final String BD_BOOKED     = "#1e1e2e";
    private static final String TX_AVAILABLE  = "#2d8a5a";
    private static final String TX_SELECTED   = "white";
    private static final String TX_BOOKED     = "#2a2a40";

    private List<String> selectedSeats = new ArrayList<>();
    private int requiredCount;
    private String movieId;
    private String showTime;
    private BookingService bookingService = new BookingService();

    @FXML
    public void initialize() {
        requiredCount = SessionManager.getRequiredSeatCount();
        movieId  = SessionManager.getSelectedMovieId();
        showTime = SessionManager.getSelectedMovieTime();
        movieLabel.setText(
            SessionManager.getSelectedMovieName()
            + "   ·   " + showTime
            + "   ·   Rs." + (int) SessionManager.getPricePerSeat() + " / seat"
        );
        buildSeatGrid();
        updateStatus();
    }

    private void buildSeatGrid() {
        seatGrid.getChildren().clear();

        // Column number headers
        for (int col = 0; col < COLS; col++) {
            Label lbl = new Label(String.valueOf(col + 1));
            lbl.setStyle("-fx-text-fill: #ffffff18; -fx-font-size: 9px;");
            lbl.setMinWidth(44);
            lbl.setAlignment(Pos.CENTER);
            seatGrid.add(lbl, col + 1, 0);
        }

        for (int row = 0; row < ROWS; row++) {
            Label rowLbl = new Label(ROW_LABELS[row]);
            rowLbl.setStyle("-fx-text-fill: #ffffff25; -fx-font-size: 10px; -fx-font-weight: bold;");
            rowLbl.setMinWidth(22);
            rowLbl.setAlignment(Pos.CENTER);
            seatGrid.add(rowLbl, 0, row + 1);

            for (int col = 0; col < COLS; col++) {
                String seatId = ROW_LABELS[row] + (col + 1);
                boolean booked = bookingService.isSeatBooked(movieId, showTime, seatId);
                Button btn = new Button(seatId);
                btn.setMinWidth(44);
                btn.setMinHeight(36);
                if (booked) {
                    btn.setStyle(seatStyle(BG_BOOKED, BD_BOOKED, TX_BOOKED));
                    btn.setDisable(true);
                } else {
                    btn.setStyle(seatStyle(BG_AVAILABLE, BD_AVAILABLE, TX_AVAILABLE));
                    btn.setOnAction(e -> handleSeatClick(btn, seatId));
                }
                seatGrid.add(btn, col + 1, row + 1);
            }
        }
    }

    private void handleSeatClick(Button btn, String seatId) {
        if (selectedSeats.contains(seatId)) {
            selectedSeats.remove(seatId);
            btn.setStyle(seatStyle(BG_AVAILABLE, BD_AVAILABLE, TX_AVAILABLE));
        } else {
            if (selectedSeats.size() >= requiredCount) {
                StyledDialog.show(
                    StyledDialog.Type.WARNING,
                    "You can only select " + requiredCount
                    + " seat(s).\nClick a selected seat to deselect it first."
                );
                return;
            }
            selectedSeats.add(seatId);
            btn.setStyle(seatStyle(BG_SELECTED, BD_SELECTED, TX_SELECTED));
        }
        updateStatus();
    }

    private void updateStatus() {
        int done = selectedSeats.size();
        if (done == 0) {
            statusLabel.setStyle("-fx-font-size:13px; -fx-text-fill:#ffffff30; -fx-font-weight:bold;");
            statusLabel.setText("Select " + requiredCount + " seat(s) from the grid above");
        } else if (done < requiredCount) {
            statusLabel.setStyle("-fx-font-size:13px; -fx-text-fill:#f4a261; -fx-font-weight:bold;");
            statusLabel.setText(String.join(", ", selectedSeats)
                + "   ·   " + (requiredCount - done) + " more to go");
        } else {
            statusLabel.setStyle("-fx-font-size:13px; -fx-text-fill:#56cfe1; -fx-font-weight:bold;");
            statusLabel.setText("✓  " + String.join(", ", selectedSeats) + "   ·   Ready to checkout!");
        }
    }

    public void goToCheckout() throws Exception {
        if (selectedSeats.size() < requiredCount) {
            StyledDialog.show(
                StyledDialog.Type.WARNING,
                "Please select " + requiredCount
                + " seat(s) before proceeding.\nYou've selected: " + selectedSeats.size()
            );
            return;
        }
        SessionManager.setBookedSeats(new ArrayList<>(selectedSeats));
        MainApp.switchScene("checkout.fxml");
    }

    public void goBack() throws Exception {
        SessionManager.clearBookingData();
        MainApp.switchScene("movies.fxml");
    }

    private String seatStyle(String bg, String border, String text) {
        return "-fx-background-color:" + bg + ";"
             + "-fx-text-fill:" + text + ";"
             + "-fx-font-size:10px; -fx-font-weight:bold;"
             + "-fx-background-radius:7; -fx-border-radius:7;"
             + "-fx-border-color:" + border + "; -fx-border-width:1;"
             + "-fx-cursor:hand;";
    }
}
