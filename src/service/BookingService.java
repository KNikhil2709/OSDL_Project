package service;

import util.FileStorage;
import util.SessionManager;
import java.util.ArrayList;
import java.util.List;

public class BookingService {

    String file = "src/data/bookings.txt";

    // Each booking key = movieId_showtime so A3 at 6PM and A3 at 9PM are different
    public boolean isSeatBooked(String movieId, String showTime, String seat) {

        String key = movieId + "_" + showTime;

        for (String line : FileStorage.read(file)) {

            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");

            if (parts.length >= 3
                    && parts[1].trim().equals(key)
                    && parts[2].trim().equals(seat)) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean bookSeat(String movieId, String showTime, String seat) {

        if (isSeatBooked(movieId, showTime, seat)) return false;

        String key = movieId + "_" + showTime;
        FileStorage.write(file,
                SessionManager.getUser() + "," + key + "," + seat);
        return true;
    }

    public List<String[]> getUserBookings(String username) {

        List<String[]> result = new ArrayList<>();

        for (String line : FileStorage.read(file)) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            if (parts.length >= 3 && parts[0].trim().equals(username))
                result.add(parts);
        }
        return result;
    }
}
