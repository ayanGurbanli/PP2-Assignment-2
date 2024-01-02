import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
    class User implements Serializable {
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
}



