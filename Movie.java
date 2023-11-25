class Movie {
    String title;
    String director;
    int releaseYear;
    int runningTime;
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
    @Override
    public String toString() {
        return "Movie: title=" + title + ", director=" + director + ", releaseYear=" + releaseYear + ", runningTime="
                + runningTime + " ";
    }

    
}
