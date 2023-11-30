import java.util.ArrayList;
import java.util.List;

class MovieDatabase {
    private List<Movie> movies;

    public MovieDatabase() {
        this.movies = new ArrayList<>();
    }

    public void addMovie (Movie newMovie){
        movies.add(newMovie);
    }
    public void addMovies(List<Movie> newMovies) {
        List<Movie> copy = List.copyOf(newMovies);
        movies.addAll(copy);
    }
    

}
