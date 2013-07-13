package rs.pedjaapps.tvshowtracker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Series {
	@Expose private int id;
	@Expose @SerializedName("SeriesName") private String seriesName;
    @Expose @SerializedName("FirstAired") private String firstAired;
    @Expose @SerializedName("IMDB_ID") private String imdbId;
    @Expose @SerializedName("Overview") private String overview;
    @Expose @SerializedName("Rating") private String rating;
    @Expose @SerializedName("SeriesID") private int seriesId;
    @Expose @SerializedName("Language") private String language;
    @Expose private String banner;
    @Expose private String fanart;
    @Expose @SerializedName("Network") private String network;
    @Expose @SerializedName("Runtime") private int runtime;
    @Expose @SerializedName("Status") private String status;
    private boolean ignore;
    private boolean hide;
    private String updated;
    private String nextEpisode;
    private int nextEpisodeDays;
    private int prgWatched;
    @Expose @SerializedName("Actors") private String actors;
    
    
    
    public Series(String banner) {
		super();
		this.banner = banner;
	}

	public Series(String seriesName, String overview, int seriesId,
                  String language, String network, String firstAired) {
		super();
		this.seriesName = seriesName;
		this.overview = overview;
		this.seriesId = seriesId;
		this.language = language;
		this.network = network;
		this.firstAired = firstAired;
	}

	public Series(String seriesName, String banner, String nextEpisode, int prgWatched, int seriesId, int nextEpisodeDays){
    	this.seriesName = seriesName;
		this.banner = banner;
    	this.nextEpisode = nextEpisode;
    	this.prgWatched = prgWatched;
    	this.seriesId= seriesId;
    	this.nextEpisodeDays = nextEpisodeDays;
    }
    
	public Series(int id, String seriesName, String firstAired, String imdbId,
                  String overview, String rating, int seriesId, String language,
                  String banner, String fanart, String network, int runtime,
                  String status, boolean ignore, boolean hide, String updated, String actors) {
		this.id = id;
		this.seriesName = seriesName;
		this.firstAired = firstAired;
		this.imdbId = imdbId;
		this.overview = overview;
		this.rating = rating;
		this.seriesId = seriesId;
		this.language = language;
		this.banner = banner;
		this.fanart = fanart;
		this.network = network;
		this.runtime = runtime;
		this.status = status;
		this.ignore = ignore;
		this.hide = hide;
		this.updated = updated;
		this.actors = actors;
	}
	
	public Series(String seriesName, String firstAired, String imdbId,
                  String overview, String rating, int seriesId, String language,
                  String banner, String fanart, String network, int runtime,
                  String status, boolean ignore, boolean hide, String updated, String actors) {
		this.seriesName = seriesName;
		this.firstAired = firstAired;
		this.imdbId = imdbId;
		this.overview = overview;
		this.rating = rating;
		this.seriesId = seriesId;
		this.language = language;
		this.banner = banner;
		this.fanart = fanart;
		this.network = network;
		this.runtime = runtime;
		this.status = status;
		this.ignore = ignore;
		this.hide = hide;
		this.updated = updated;
		this.actors = actors;
	}

	public Series(){
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
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

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public int getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(int seriesId) {
		this.seriesId = seriesId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	public String getFanart() {
		return fanart;
	}

	public void setFanart(String fanart) {
		this.fanart = fanart;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public int getRuntime() {
		return runtime;
	}

	public void setRuntime(int runtime) {
		this.runtime = runtime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isIgnore() {
		return ignore;
	}

	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}

	public boolean isHide() {
		return hide;
	}

	public void setHide(boolean hide) {
		this.hide = hide;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public String getNextEpisode() {
		return nextEpisode;
	}

	public void setNextEpisode(String nextEpisode) {
		this.nextEpisode = nextEpisode;
	}

	public int getPrgWatched() {
		return prgWatched;
	}

	public void setPrgWatched(int prgWatched) {
		this.prgWatched = prgWatched;
	}

	public String getActors() {
		return actors;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}

	public int getNextEpisodeDays() {
		return nextEpisodeDays;
	}

	public void setNextEpisodeDays(int nextEpisodeDays) {
		this.nextEpisodeDays = nextEpisodeDays;
	}
    
	
    
}
