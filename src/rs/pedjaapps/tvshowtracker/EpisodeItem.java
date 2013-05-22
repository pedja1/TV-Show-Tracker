package rs.pedjaapps.tvshowtracker;

public class EpisodeItem implements Episode{
	
	private int id;
	private String episodeName;
    private int episode;
    private int season;
    private String firstAired;
    private String imdbId;
    private String overview;
    private double rating;
    private boolean watched;
    private int episodeId;
    private String profile;
    
   
	public EpisodeItem(String episodeName, int episode, int season,
			String firstAired, String imdbId, String overview, double rating,
			boolean watched, int episodeId, String profile) {
		super();
		this.episodeName = episodeName;
		this.episode = episode;
		this.season = season;
		this.firstAired = firstAired;
		this.imdbId = imdbId;
		this.overview = overview;
		this.rating = rating;
		this.watched = watched;
		this.episodeId = episodeId;
		this.profile = profile;
	}

	public EpisodeItem(int id, String episodeName, int episode, int season,
			String firstAired, String imdbId, String overview, double rating,
			boolean watched, int episodeId, String profile) {
		super();
		this.id = id;
		this.episodeName = episodeName;
		this.episode = episode;
		this.season = season;
		this.firstAired = firstAired;
		this.imdbId = imdbId;
		this.overview = overview;
		this.rating = rating;
		this.watched = watched;
		this.episodeId = episodeId;
		this.profile = profile;
	}

	public EpisodeItem(){
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEpisodeName() {
		return episodeName;
	}

	public void setEpisodeName(String episodeName) {
		this.episodeName = episodeName;
	}

	public int getEpisode() {
		return episode;
	}

	public void setEpisode(int episode) {
		this.episode = episode;
	}

	public int getSeason() {
		return season;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public String getFirstAired() {
		return firstAired;
	}

	public void setFirstAired(String firstAired) {
		this.firstAired = firstAired;
	}

	public String getImdbId() {
		return imdbId;
	}

	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public boolean isWatched() {
		return watched;
	}

	public void setWatched(boolean watched) {
		this.watched = watched;
	}

	public int getEpisodeId() {
		return episodeId;
	}

	public void setEpisodeId(int episodeId) {
		this.episodeId = episodeId;
	}

	@Override
	public boolean isSection() {
		return false;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}
    
    
    
}
