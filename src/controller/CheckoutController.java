package controller;

import app.MainApp;
import javafx.fxml.FXML;
import util.StyledDialog;
import javafx.scene.control.Label;
import service.BookingService;
import util.SessionManager;

public class CheckoutController {

    @FXML private Label movieLabel;
    @FXML private Label timeLabel;
    @FXML private Label seatsLabel;
    @FXML private Label seatCountLabel;
    @FXML private Label priceLabel;
    @FXML private Label totalLabel;
    @FXML private Label userLabel;

    @FXML
    public void initialize() {

        String movie  = SessionManager.getSelectedMovieName();
        String time   = SessionManager.getSelectedMovieTime();
        String seats  = String.join(", ", SessionManager.getBookedSeats());
        int count     = SessionManager.getBookedSeats().size();
        double price  = SessionManager.getPricePerSeat();
        double total  = count * price;

        movieLabel.setText(movie);
        timeLabel.setText(time);
        seatsLabel.setText(seats);
        seatCountLabel.setText(String.valueOf(count));
        priceLabel.setText("Rs." + (int) price);
        totalLabel.setText("Rs." + (int) total);
        userLabel.setText(SessionManager.getUser());
    }

    public void confirmBooking() throws Exception {

        BookingService service = new BookingService();
        String movieId  = SessionManager.getSelectedMovieId();
        String showTime = SessionManager.getSelectedMovieTime();

        // Book each seat with movieId + showtime as the key
        for (String seat : SessionManager.getBookedSeats()) {
            service.bookSeat(movieId, showTime, seat);
        }

        int total = SessionManager.getBookedSeats().size() * (int) SessionManager.getPricePerSeat();

        String confirmMsg =
            "Movie  :  " + SessionManager.getSelectedMovieName() + "\n"
            + "Time    :  " + SessionManager.getSelectedMovieTime() + "\n"
            + "Seats  :  " + String.join(", ", SessionManager.getBookedSeats()) + "\n"
            + "Total   :  Rs." + total + "\n\n"
            + "Enjoy your movie!";

        StyledDialog.show(StyledDialog.Type.INFO, confirmMsg, () -> {
            try {
                SessionManager.clearBookingData();
                MainApp.switchScene("dashboard.fxml");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void goBack() throws Exception {
        MainApp.switchScene("seats.fxml");
    }
}
