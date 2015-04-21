package rs.pedjaapps.tvshowtracker.model;

import java.util.List;

import java.util.ArrayList;

public class Show {

    private long tvdb_id;
    private String title;
    private Integer year;
    private String url;
    private Long first_aired;
    private String country;
    private String overview;
    private Integer runtime;
    private String status;
    private String network;
    private String air_day;
    private String air_time;
    private String certification;
    private String imdb_id;
    private Integer tvrage_id;
    private Long last_updated;
    private Integer rating;
    private Integer votes;
    private Integer loved;
    private Integer hated;
    /** Not-null value. */
    private String username;

    private Image image;

    private List<Actor> actors;
    private List<String> genres;
    private List<Episode> episodes;

    private int watchedPercent;
    private long nextEpisodeHours;
    private Episode upcomingEpisode;
    private int posterMainColor = -1;
    private boolean showAdded;

    public Show() {
    }

    public Show(long tvdb_id) {
        this.tvdb_id = tvdb_id;
    }

    public Show(long tvdb_id, String title, Integer year, String url, Long first_aired, String country, String overview, Integer runtime, String status, String network, String air_day, String air_time, String certification, String imdb_id, Integer tvrage_id, Long last_updated, Integer rating, Integer votes, Integer loved, Integer hated, String username, Long image_id) {
        this.tvdb_id = tvdb_id;
        this.title = title;
        this.year = year;
        this.url = url;
        this.first_aired = first_aired;
        this.country = country;
        this.overview = overview;
        this.runtime = runtime;
        this.status = status;
        this.network = network;
        this.air_day = air_day;
        this.air_time = air_time;
        this.certification = certification;
        this.imdb_id = imdb_id;
        this.tvrage_id = tvrage_id;
        this.last_updated = last_updated;
        this.rating = rating;
        this.votes = votes;
        this.loved = loved;
        this.hated = hated;
        this.username = username;
    }

    public long getTvdb_id() {
        return tvdb_id;
    }

    public void setTvdb_id(long tvdb_id) {
        this.tvdb_id = tvdb_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getFirst_aired() {
        return first_aired;
    }

    public void setFirst_aired(Long first_aired) {
        this.first_aired = first_aired;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getAir_day() {
        return air_day;
    }

    public void setAir_day(String air_day) {
        this.air_day = air_day;
    }

    public String getAir_time() {
        return air_time;
    }

    public void setAir_time(String air_time) {
        this.air_time = air_time;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public Integer getTvrage_id() {
        return tvrage_id;
    }

    public void setTvrage_id(Integer tvrage_id) {
        this.tvrage_id = tvrage_id;
    }

    public Long getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(Long last_updated) {
        this.last_updated = last_updated;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public Integer getLoved() {
        return loved;
    }

    public void setLoved(Integer loved) {
        this.loved = loved;
    }

    public Integer getHated() {
        return hated;
    }

    public void setHated(Integer hated) {
        this.hated = hated;
    }

    /** Not-null value. */
    public String getUsername() {
        return username;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUsername(String username) {
        this.username = username;
    }


    /** To-one relationship, resolved on first access. */
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
            this.image = image;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Actor> getActors() {
        return actors;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetActors() {
        actors = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<String> getGenres() {
        return genres;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetGenres() {
        genres = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Episode> getEpisodes() {
        return episodes;
    }

    public int getWatchedPercent()
    {
        return watchedPercent;
    }

    public void setWatchedPercent(int watchedPercent)
    {
        this.watchedPercent = watchedPercent;
    }

    public long getNextEpisodeHours()
    {
        return nextEpisodeHours;
    }

    public void setNextEpisodeHours(long nextEpisodeHours)
    {
        this.nextEpisodeHours = nextEpisodeHours;
    }

    public Episode getUpcomingEpisode()
    {
        return upcomingEpisode;
    }

    public void setUpcomingEpisode(Episode upcomingEpisode)
    {
        this.upcomingEpisode = upcomingEpisode;
    }

    public int getPosterMainColor()
    {
        return posterMainColor;
    }

    public void setPosterMainColor(int posterMainColor)
    {
        this.posterMainColor = posterMainColor;
    }

    public boolean isShowAdded()
    {
        return showAdded;
    }

    public void setShowAdded(boolean showAdded)
    {
        this.showAdded = showAdded;
    }

    public static Show findShowWithId(long tvdbId, List<Show> shows)
    {
        for (Show show : shows)
        {
            if(show.getTvdb_id() == tvdbId)
                return show;
        }
        return null;
    }

    public static List<Episode> findEpisodes(List<Episode> fullEpisodes, List<Episode> searchEpisodes)
    {
        List<Episode> updateList = new ArrayList<>();

        if(fullEpisodes != null && searchEpisodes != null)for(Episode se : searchEpisodes)
        {
            for (Episode fe : fullEpisodes)
            {
                if(se.getSeason() == fe.getSeason() && se.getEpisode() == fe.getEpisode())
                {
                    fe.setWatched(true);
                    updateList.add(fe);
                }
            }
        }

        return updateList;
    }

    public void setActors(List<Actor> actors)
    {
        this.actors = actors;
    }

    public void setGenres(List<String> genres)
    {
        this.genres = genres;
    }

    public void setEpisodes(List<Episode> episodes)
    {
        this.episodes = episodes;
    }
}
