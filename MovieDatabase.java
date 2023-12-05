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
    
    public void deleteMovie (Movie oldMovie) {
        movies.remove(oldMovie);
    }

    public void deleteMovies(List<Movie> oldMovies ) {
        
    }

}
