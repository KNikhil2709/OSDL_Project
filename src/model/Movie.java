package model;

import java.util.Arrays;
import java.util.List;

public class Movie {

    private String id;
    private String name;
    private String genre;
    private String showTimesRaw; // stored as "10AM|2PM|6PM"
    private int price;

    public Movie(String id, String name, String genre, String showTimesRaw, int price) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.showTimesRaw = showTimesRaw;
        this.price = price;
    }

    public String getId()           { return id; }
    public String getName()         { return name; }
    public String getGenre()        { return genre; }
    public int getPrice()           { return price; }

    // Keep getSeats() so existing code doesn't break
    public int getSeats()           { return price; }

    public String getTime() {
        return showTimesRaw.replace("|", ", ");
    }

    public List<String> getShowTimes() {
        return Arrays.asList(showTimesRaw.split("\\|"));
    }
}
