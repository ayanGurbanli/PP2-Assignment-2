
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;

class MovieDatabase implements Serializable {
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
}
