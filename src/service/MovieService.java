package service;

import util.FileStorage;
import model.Movie;
import java.util.*;

public class MovieService {

    String file = "src/data/movies.txt";

    public List<Movie> getMovies() {

        List<Movie> list = new ArrayList<>();

        for (String line : FileStorage.read(file)) {

            if (line.trim().isEmpty()) continue;

            String[] parts = line.split(",");

            // format: id,name,genre,showtimes,price
            if (parts.length >= 5) {
                list.add(new Movie(
                    parts[0].trim(),
                    parts[1].trim(),
                    parts[2].trim(),
                    parts[3].trim(),
                    Integer.parseInt(parts[4].trim())
                ));
            }
        }

        return list;
    }
}
