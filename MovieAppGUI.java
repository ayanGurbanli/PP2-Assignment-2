import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MovieAppGUI extends JFrame {
    private MovieDatabase movieDatabase;
    private JList<String> movieList;
    private DefaultListModel<String> movieListModel;

    public MovieAppGUI() {
        movieDatabase = MovieDatabase.loadFromFile("database.ser");

        if (movieDatabase == null) {
            movieDatabase = new MovieDatabase();
            movieDatabase.addUser(new User("user1", "pass1"));
            movieDatabase.addUser(new User("user2", "pass2"));
            movieDatabase.addMovie(new Movie("Inception", "Christopher Nolan", 2010, 148));
            movieDatabase.addMovie(new Movie("The Shawshank Redemption", "Frank Darabont", 1994, 142));
        }

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Movie Database");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        movieListModel = new DefaultListModel<>();
        movieList = new JList<>(movieListModel);
        JScrollPane scrollPane = new JScrollPane(movieList);
    
        JButton browseButton = new JButton("Browse Movies");
        browseButton.addActionListener(e -> browseMovies());
    
        JButton addToWatchlistButton = new JButton("Add to Watchlist");
        addToWatchlistButton.addActionListener(e -> addToWatchlist());
    
        JButton removeFromWatchlistButton = new JButton("Remove from Watchlist");
        removeFromWatchlistButton.addActionListener(e -> removeFromWatchlist());
    
        JButton filterButton = new JButton("Filter Movies");
        filterButton.addActionListener(e -> filterMovies());
    
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> registerUser());

        
    
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(browseButton);
        buttonPanel.add(addToWatchlistButton);
        buttonPanel.add(removeFromWatchlistButton);
        buttonPanel.add(filterButton); 
        buttonPanel.add(registerButton);
        

    
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }
    
    

    private void browseMovies() {
        List<Movie> sortedMovies = sortMovies(movieDatabase.getMovies());
        movieListModel.clear();
        for (Movie movie : sortedMovies) {
            movieListModel.addElement(movie.getDetails());
        }
    }
    private void registerUser() {
    String username = JOptionPane.showInputDialog(this, "Enter your desired username:");
    String password = JOptionPane.showInputDialog(this, "Enter your desired password:");

    if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
        if (movieDatabase.registerUser(username, password)) {
            JOptionPane.showMessageDialog(this, "Registration successful! You can now log in.");
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists. Please choose a different username.");
        }
    } else {
        JOptionPane.showMessageDialog(this, "Invalid username or password. Registration failed.");
    }
}
private void filterMovies() {
    List<Movie> filteredMovies = filterMovies(movieDatabase.getMovies());
    movieListModel.clear();
    for (Movie movie : filteredMovies) {
        movieListModel.addElement(movie.getDetails());
    }
}

    private void addToWatchlist() {
        String username = JOptionPane.showInputDialog("Enter your username:");
        String password = JOptionPane.showInputDialog("Enter your password:");

        if (movieDatabase.validateUser(username, password)) {
            User currentUser = movieDatabase.findUserByUsername(username);
            Movie selectedMovie = selectMovie(movieDatabase.getMovies());
            if (currentUser.addToWatchlist(selectedMovie)) {
                JOptionPane.showMessageDialog(this, "Added to watchlist: " + selectedMovie.getTitle());
            } else {
                JOptionPane.showMessageDialog(this, "Movie is already in your watchlist.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.");
        }
    }

    private void removeFromWatchlist() {
        String username = JOptionPane.showInputDialog("Enter your username:");
        String password = JOptionPane.showInputDialog("Enter your password:");

        if (movieDatabase.validateUser(username, password)) {
            User currentUser = movieDatabase.findUserByUsername(username);
            Movie selectedMovie = selectMovie(currentUser.getWatchlist());
            if (currentUser.removeFromWatchlist(selectedMovie)) {
                JOptionPane.showMessageDialog(this, "Removed from watchlist: " + selectedMovie.getTitle());
            } else {
                JOptionPane.showMessageDialog(this, "Movie is not in your watchlist.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.");
        }
    }

    private List<Movie> sortMovies(List<Movie> movies) {
        String[] options = {"By Title", "By Release Year", "By Running Time"};
    int choice = JOptionPane.showOptionDialog(this, "Select sorting option:", "Sort Movies",
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

    switch (choice) {
        case 0:
            return movies.stream().sorted(Comparator.comparing(Movie::getTitle)).collect(Collectors.toList());
        case 1:
            return movies.stream().sorted(Comparator.comparingInt(Movie::getReleaseYear)).collect(Collectors.toList());
        case 2:
            return movies.stream().sorted(Comparator.comparingInt(Movie::getRunningTime)).collect(Collectors.toList());
        default:
            JOptionPane.showMessageDialog(this, "Invalid sort choice. Defaulting to sorting by Title.");
            return movies.stream().sorted(Comparator.comparing(Movie::getTitle)).collect(Collectors.toList());
    }
    }
    private List<Movie> filterMovies(List<Movie> movies) {
        String filterKeyword = JOptionPane.showInputDialog(this, "Enter filter keyword:");
        if (filterKeyword == null || filterKeyword.isEmpty()) {
            return movies; 
        }
    
        String[] options = {"By Title", "By Director", "By Release Year"};
        int choice = JOptionPane.showOptionDialog(this, "Select filter option:", "Filter Movies",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    
        switch (choice) {
            case 0:
                return movies.stream().filter(movie -> movie.getTitle().contains(filterKeyword))
                        .collect(Collectors.toList());
            case 1:
                return movies.stream().filter(movie -> movie.getDirector().contains(filterKeyword))
                        .collect(Collectors.toList());
            case 2:
                try {
                    int releaseYearFilter = Integer.parseInt(filterKeyword);
                    return movies.stream().filter(movie -> movie.getReleaseYear() == releaseYearFilter)
                            .collect(Collectors.toList());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid release year filter. Please enter a valid number.");
                    return filterMovies(movies); 
                }
            default:
                JOptionPane.showMessageDialog(this, "Invalid filter choice. No filtering applied.");
                return movies;
        }
    }

    private Movie selectMovie(List<Movie> movies) {
        String[] movieTitles = movies.stream().map(Movie::getTitle).toArray(String[]::new);

    int choice = JOptionPane.showOptionDialog(this, "Select a Movie:", "Movie Selection",
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, movieTitles, movieTitles[0]);

    if (choice >= 0 && choice < movies.size()) {
        return movies.get(choice);
    } else {
        JOptionPane.showMessageDialog(this, "Invalid movie selection. Please try again.");
        return selectMovie(movies);
    }
    }
    private void addNewMovie(){
        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MovieAppGUI().setVisible(true));
    }
}





