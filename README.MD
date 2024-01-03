# MovieAppGUI

* The `MovieAppGUI` class represents the graphical user interface
for a movie database application. It extends the `JFrame` 
class and includes various buttons and methods for 
browsing movies, managing watchlists, filtering 
movies, and user registration.
* After running the code user is able to register to the user database where all users and their passwords are stored.
Then using them get the access personal page. User can access their watchList and interact with that data.
* 


## Movie Class

The `Movie` class is a simple representation of a movie, implementing the `Serializable` interface. It has fields for title, director, release year, and running time. The class provides a constructor, getter and setter methods, and a method to retrieve movie details.
Properties of Movie class were included in the constructor:
      
      private String title;
      private String director;
      private int releaseYear;
      private int runningTime;

    public Movie(String title, String director, int releaseYear, int runningTime) {
        this.title = title;
        this.director = director;
        this.releaseYear = releaseYear;
        this.runningTime = runningTime;
    }

Additionally, setters and getters were added:


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(int runningTime) {
        this.runningTime = runningTime;
    }

For easier readable data and user convention the method similar to toString was added.
This allows to see all properties of an object:

    public String getDetails() {
        return "Title: " + title + "\nDirector: " + director +
                "\nRelease Year: " + releaseYear + "\nRunning Time: " + runningTime + " mins";
    }

## User Class

The `User` class represents a user in the movie database system. It also implements the `Serializable` interface. It has fields for username, password, and a watchlist of movies. The class provides methods for user management, including adding/removing movies from the watchlist and getter/setter methods.
Similar to Movie class the constructor involved all the properties that object had and method of setters and getters:
    
    private String username;
    private String password;
    private List<Movie> watchlist;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.watchlist = new ArrayList<>();
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }
    public List<Movie> getWatchlist(){
        return this.watchlist;
    }

    public void setName(String username){
        this.username=username;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public void setWatchlist(List<Movie> watchlist){
        this.watchlist=watchlist;
    }

The User class as being personal to each user includes the WatchList, basically database of movies that user watched or watching.
To manipulation of that data the method of addToWatchList() (avoiding any repetition in Movies list) and removeFromWatchList() methods were created:

     public boolean addToWatchlist(Movie movie) {
        if (!watchlist.contains(movie)) {
            watchlist.add(movie);
            return true;
        }
        return false;
    }

    public boolean removeFromWatchlist(Movie movie) {
        return watchlist.remove(movie);
    }



## MovieDatabase Class

The `MovieDatabase` class is a central component that manages movies and users. It contains lists of movies and users, and provides methods for adding movies, registering users, validating user credentials, saving/loading data to/from a file, and finding a user by username.
Swing interface is dependable in this class, as main functions of data manipulation are implemented here.

    private List<Movie> movies;
    private List<User> users;

    public MovieDatabase() {
        this.movies = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public boolean registerUser(String username, String password) {
        username = username.trim();
        password = password.trim();
    
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return false;
            }
        }
    
        users.add(new User(username, password));
        return true; 
    }

    public boolean validateUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public void addMovie(Movie movie) {
        if (!movies.contains(movie)) {
            movies.add(movie);
        }
    }

    public List<Movie> getMovies() {
        return new ArrayList<>(movies);
    }

The save/load methods involve Exception handling:

    public boolean saveToFile(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(this);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static MovieDatabase loadFromFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (MovieDatabase) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

## MovieAppGUI Constructor

In the constructor of `MovieAppGUI`, the movie database is loaded from a file, and if it doesn't exist, some default movies and users are added to initialize the database. The GUI components are then initialized using the `initializeUI` method.

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

## GUI Initialization

The `initializeUI` method sets up the main JFrame, a JList for displaying movies, and 
several buttons for browsing movies, adding to watchlist, removing from watchlist, 
filtering movies, and user registration.

    private void initializeUI() {
        setTitle("Movie Database");
        setSize(400, 300);
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


## Event Handling Methods

The class includes methods like `browseMovies`, `addToWatchlist`, `removeFromWatchlist`, `filterMovies`, and `registerUser` for handling user interactions with the GUI buttons.


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


## Sorting and Filtering Methods

There are methods for sorting and filtering movies based on user input, using options presented through JOptionPane dialogs.
     
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

## Selecting Movies

The `selectMovie` method allows users to select a movie from a list using a dialog.

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

## Main Method

The `main` method invokes the GUI on the Event Dispatch Thread.

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MovieAppGUI().setVisible(true));
    }


Youtube video links
Swing GUI implementation -> https://youtu.be/8nMAqL0sPO4
MovieApp explanation ->https://youtu.be/TuUrU0YaKTY
The skeleton of the code -> https://drive.google.com/drive/folders/1Qv5US5j15zy_zuxVTOHnD8yZBVn2LOjV?usp=sharing
