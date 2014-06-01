package rs.pedjaapps.tvshowtracker.model;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table EPISODE.
 */
public class Episode {

    private Long id;
    private Integer season;
    private Integer episode;
    private Integer tvdb_id;
    private String title;
    private String overview;
    private Long first_aired;
    private String url;
    private String screen;
    private Integer rating;
    private Integer votes;
    private Integer loved;
    private Integer hated;
    private boolean watched;
    private long show_id;

    public Episode() {
    }

    public Episode(Long id) {
        this.id = id;
    }

    public Episode(Long id, Integer season, Integer episode, Integer tvdb_id, String title, String overview, Long first_aired, String url, String screen, Integer rating, Integer votes, Integer loved, Integer hated, boolean watched, long show_id) {
        this.id = id;
        this.season = season;
        this.episode = episode;
        this.tvdb_id = tvdb_id;
        this.title = title;
        this.overview = overview;
        this.first_aired = first_aired;
        this.url = url;
        this.screen = screen;
        this.rating = rating;
        this.votes = votes;
        this.loved = loved;
        this.hated = hated;
        this.watched = watched;
        this.show_id = show_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public Integer getEpisode() {
        return episode;
    }

    public void setEpisode(Integer episode) {
        this.episode = episode;
    }

    public Integer getTvdb_id() {
        return tvdb_id;
    }

    public void setTvdb_id(Integer tvdb_id) {
        this.tvdb_id = tvdb_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Long getFirst_aired() {
        return first_aired;
    }

    public void setFirst_aired(Long first_aired) {
        this.first_aired = first_aired;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
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

    public boolean getWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public long getShow_id() {
        return show_id;
    }

    public void setShow_id(long show_id) {
        this.show_id = show_id;
    }

}
