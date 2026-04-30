package util;
import java.util.ArrayList;
import java.util.List;
public class SessionManager {
    private static String currentUser;
    private static String selectedMovieId;
    private static String selectedMovieName;
    private static String selectedMovieTime;
    private static double pricePerSeat;
    private static int requiredSeatCount;
    private static List<String> bookedSeats = new ArrayList<>();
    public static void setUser(String u)             { currentUser = u; }
    public static String getUser()                   { return currentUser; }
    public static void setSelectedMovieId(String id) { selectedMovieId = id; }
    public static String getSelectedMovieId()        { return selectedMovieId; }
    public static void setSelectedMovieName(String n){ selectedMovieName = n; }
    public static String getSelectedMovieName()      { return selectedMovieName; }
    public static void setSelectedMovieTime(String t){ selectedMovieTime = t; }
    public static String getSelectedMovieTime()      { return selectedMovieTime; }
    public static void setPricePerSeat(double p)     { pricePerSeat = p; }
    public static double getPricePerSeat()           { return pricePerSeat; }
    public static void setRequiredSeatCount(int c)   { requiredSeatCount = c; }
    public static int getRequiredSeatCount()         { return requiredSeatCount; }
    public static void setBookedSeats(List<String> s){ bookedSeats = s; }
    public static List<String> getBookedSeats()      { return bookedSeats != null ? bookedSeats : new ArrayList<>(); }
    public static void clearBookingData() {
        bookedSeats = new ArrayList<>();
        requiredSeatCount = 0;
        // NOTE: We intentionally keep movieId/name/time/price so SeatController
        // can still read them after navigating back from checkout to seats.
    }
}
